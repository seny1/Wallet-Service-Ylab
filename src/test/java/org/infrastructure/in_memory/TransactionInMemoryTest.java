package org.infrastructure.in_memory;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.domain.Client;
import org.domain.Transaction;
import org.domain.TypeOfTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class TransactionInMemoryTest {

    private TransactionInMemory transactionInMemory;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        transactionInMemory = new TransactionInMemory();
        Map<Integer, Transaction> tempMap = new HashMap<>();
        tempMap.put(1, new Transaction(1, TypeOfTransaction.Debit, 1000));
        Field transactionMap = transactionInMemory.getClass().getDeclaredField("transactionMap");
        transactionMap.setAccessible(true);
        transactionMap.set(transactionInMemory, tempMap);
    }

    @Test
    void validateIdTrue() {
        boolean test = transactionInMemory.validateId(2);
        Assertions.assertTrue(test);
    }

    @Test
    void validateIdFalse() {
        boolean test = transactionInMemory.validateId(1);
        Assertions.assertFalse(test);
    }

    @Test
    void validateAmountTrue() {
        Client client = new Client("Ivan123", "123", 50);
        boolean test = transactionInMemory.validateAmount(TypeOfTransaction.Debit, 25, client);
        Assertions.assertTrue(test);
    }

    @Test
    void validateAmountFalse() {
        Client client = new Client("Ivan123", "123", 50);
        boolean test = transactionInMemory.validateAmount(TypeOfTransaction.Debit, 100, client);
        Assertions.assertFalse(test);
    }

    @Test
    void addTransaction() throws NoSuchFieldException, IllegalAccessException {
        TransactionInMemory transactionInMemory1 = new TransactionInMemory();
        Map<Integer, Transaction> expected = new HashMap<>();
        Transaction transaction = new Transaction(5, TypeOfTransaction.Debit, 500);
        expected.put(transaction.getId(), transaction);

        transactionInMemory1.addTransaction(transaction);

        Field clientMap = transactionInMemory1.getClass().getDeclaredField("transactionMap");
        clientMap.setAccessible(true);

        Object actual = clientMap.get(transactionInMemory1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void addTransactionHistory() throws NoSuchFieldException, IllegalAccessException {
        MultiValuedMap<String, Transaction> expected = new ArrayListValuedHashMap<>();
        Transaction transaction = new Transaction(10, TypeOfTransaction.Debit, 500);
        expected.put("Ivan123", transaction);

        transactionInMemory.addTransactionHistory(new Client("Ivan123", "123", 1000), transaction);

        Field audit = transactionInMemory.getClass().getDeclaredField("history");
        audit.setAccessible(true);

        Object actual = audit.get(transactionInMemory);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void printHistory() throws NoSuchFieldException, IllegalAccessException {
        Field audit = transactionInMemory.getClass().getDeclaredField("history");
        audit.setAccessible(true);

        MultiValuedMap<String, Transaction> tempMap = new ArrayListValuedHashMap<>();
        Transaction transaction = new Transaction(10, TypeOfTransaction.Debit, 500);
        tempMap.put("Ivan123", transaction);

        audit.set(transactionInMemory, tempMap);

        PrintStream standardOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        transactionInMemory.printHistory(new Client("Ivan123", "123", 500));

        Assertions.assertEquals("10 Debit 500", outputStreamCaptor.toString().trim());
    }
}
