package org.app;

import org.elSasen.app.logic.ClientService;
import org.elSasen.domain.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.elSasen.repository_interface.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void authorizationTrue() {
        Client client = new Client("Ivan123", "123", 0);
        Mockito.when(clientRepository.checkClient(client)).thenReturn(true);

        boolean test = clientService.authorization("Ivan123", "123");
        Assertions.assertTrue(test);
    }

    @Test
    void authorizationFalse() {
        Client client = new Client("Ivan123", "123", 0);
        Mockito.when(clientRepository.checkClient(client)).thenReturn(false);

        boolean test = clientService.authorization("Ivan123", "123");
        Assertions.assertFalse(test);
    }

    @Test
    void registrationTrue() {
        Mockito.when(clientRepository.findClientByLogin("Ivan123")).thenReturn(false);

        boolean test = clientService.registration("Ivan123", "123");
        Assertions.assertTrue(test);
    }

    @Test
    void registrationFalse() {
        Mockito.when(clientRepository.findClientByLogin("Ivan123")).thenReturn(true);

        boolean test = clientService.registration("Ivan123", "123");
        Assertions.assertFalse(test);
    }

    @Test
    void checkBalance() {
        Client client = new Client("Ivan123", "123", 500);
        Mockito.when(clientRepository.findClientByLoginReturnClient("Ivan123")).thenReturn(client);

        int test = clientService.checkBalance("Ivan123");
        Assertions.assertEquals(500, test);
    }
}
