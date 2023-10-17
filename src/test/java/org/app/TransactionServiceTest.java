package org.app;

import org.elSasen.app.logic.TransactionService;
import org.elSasen.domain.Client;
import org.elSasen.domain.Transaction;
import org.elSasen.domain.TypeOfTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.elSasen.repository_interface.ClientRepository;
import org.elSasen.repository_interface.TransactionRepository;

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
        Transaction transaction = new Transaction(1L, TypeOfTransaction.Debit, 1000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateAmount(TypeOfTransaction.Debit, transaction.getAmount(), client)).thenReturn(true);

        boolean test = transactionService.debit(1000, "Ivan123");

        Assertions.assertTrue(test);
    }

    @Test
    void debitFalseOfAmount() {
        Transaction transaction = new Transaction(1L, TypeOfTransaction.Debit, 5000);
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);
        Mockito.when(transactionRepository.validateAmount(TypeOfTransaction.Debit, transaction.getAmount(), client)).thenReturn(false);

        boolean test = transactionService.debit(5000, "Ivan123");

        Assertions.assertFalse(test);
    }


    @Test
    void credit() {
        Client client = new Client("Ivan123", "123", 1000);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);

        boolean test = transactionService.credit(5000, "Ivan123");

        Assertions.assertTrue(test);
    }
}
