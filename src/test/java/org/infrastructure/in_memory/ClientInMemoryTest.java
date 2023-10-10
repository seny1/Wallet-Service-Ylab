package org.infrastructure.in_memory;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.domain.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class ClientInMemoryTest {

    private ClientInMemory clientInMemory;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        clientInMemory = new ClientInMemory();
        Map<String, Client> tempMap = new HashMap<>();
        tempMap.put("Ivan123", new Client("Ivan123", "123", 0));
        Field clientMap = clientInMemory.getClass().getDeclaredField("clientMap");
        clientMap.setAccessible(true);
        clientMap.set(clientInMemory, tempMap);
    }

    @Test
    void checkClientTrue() {
        boolean test = clientInMemory.checkClient(new Client("Ivan123", "123", 0));
        Assertions.assertTrue(test);
    }

    @Test
    void checkClientFalse() {
        boolean test = clientInMemory.checkClient(new Client("Ivan123", "1233", 0));
        Assertions.assertFalse(test);
    }

    @Test
    void addClient() throws NoSuchFieldException, IllegalAccessException {
        ClientInMemory clientInMemory1 = new ClientInMemory();

        Map<String, Client> expected = new HashMap<>();
        Client client = new Client("Ivan124", "123", 0);
        expected.put(client.getLogin(), client);

        clientInMemory1.addClient(client);

        Field clientMap = clientInMemory1.getClass().getDeclaredField("clientMap");
        clientMap.setAccessible(true);

        Object actual = clientMap.get(clientInMemory1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findClientByLoginTrue() {
        boolean test = clientInMemory.findClientByLogin("Ivan123");
        Assertions.assertTrue(test);
    }

    @Test
    void findClientByLoginFalse() {
        boolean test = clientInMemory.findClientByLogin("Serega154");
        Assertions.assertFalse(test);
    }

    @Test
    void findClientByLoginReturnClientEquals() {
        Client actual = clientInMemory.findClientByLoginReturnClient("Ivan123");
        Assertions.assertEquals(new Client("Ivan123", "123", 0), actual);
    }

    @Test
    void findClientByLoginReturnClientNotEquals() {
        Client actual = clientInMemory.findClientByLoginReturnClient("Serega543");
        Assertions.assertNotEquals(new Client("Ivan123", "123", 0), actual);
    }

    @Test
    void addInAudit() throws NoSuchFieldException, IllegalAccessException {
        MultiValuedMap<String, String> expected = new ArrayListValuedHashMap<>();
        expected.put("Ivan123", "registration");

        clientInMemory.addInAudit("Ivan123", "registration");

        Field audit = clientInMemory.getClass().getDeclaredField("audit");
        audit.setAccessible(true);

        Object actual = audit.get(clientInMemory);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void printAudit() throws NoSuchFieldException, IllegalAccessException {
        Field audit = clientInMemory.getClass().getDeclaredField("audit");
        audit.setAccessible(true);

        MultiValuedMap<String, String> tempMap = new ArrayListValuedHashMap<>();
        tempMap.put("Ivan123", "registration");

        audit.set(clientInMemory, tempMap);

        PrintStream standardOut = System.out;
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        clientInMemory.printAudit("Ivan123");

        Assertions.assertEquals("registration", outputStreamCaptor.toString().trim());
    }
}