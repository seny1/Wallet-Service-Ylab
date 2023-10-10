package org.app;

import org.app.logic.ClientService;
import org.app.logic.TransactionService;
import org.domain.Client;
import org.domain.Transaction;
import org.domain.TypeOfTransaction;
import org.infrastructure.in_memory.TransactionInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.repository_interface.ClientRepository;
import org.repository_interface.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void debitTrue() {
        Transaction transaction = new Transaction(1, TypeOfTransaction.Debit, 1000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateId(transaction.getId())).thenReturn(true);
        Mockito.when(transactionRepository.validateAmount(TypeOfTransaction.Debit, transaction.getAmount(), client)).thenReturn(true);

        boolean test = transactionService.debit(1, 1000, "Ivan123");

        Assertions.assertTrue(test);
    }

    @Test
    void debitFalseOfAmount() {
        Transaction transaction = new Transaction(1, TypeOfTransaction.Debit, 5000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateId(transaction.getId())).thenReturn(true);
        Mockito.when(transactionRepository.validateAmount(TypeOfTransaction.Debit, transaction.getAmount(), client)).thenReturn(false);

        boolean test = transactionService.debit(1, 5000, "Ivan123");

        Assertions.assertFalse(test);
    }

    @Test
    void debitFalseOfId() {
        Transaction transaction = new Transaction(1, TypeOfTransaction.Debit, 5000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateId(transaction.getId())).thenReturn(false);

        boolean test = transactionService.debit(1, 5000, "Ivan123");

        Assertions.assertFalse(test);
    }

    @Test
    void creditTrue() {
        Transaction transaction = new Transaction(1, TypeOfTransaction.Credit, 5000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateId(transaction.getId())).thenReturn(true);

        boolean test = transactionService.credit(1, 5000, "Ivan123");

        Assertions.assertTrue(test);
    }

    @Test
    void creditFalse() {
        Transaction transaction = new Transaction(1, TypeOfTransaction.Credit, 5000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateId(transaction.getId())).thenReturn(false);

        boolean test = transactionService.credit(1, 5000, "Ivan123");

        Assertions.assertFalse(test);
    }
}
