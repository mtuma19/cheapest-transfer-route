package com.example.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TransferServiceTest {

    private TransferService transferService;

    @BeforeEach
    public void setUp() {
        transferService = new TransferService();  // Set up the service before each test
    }

    @Test
    public void testFindOptimalTransfers_whenTransfersAreValid() {
        // Arrange
        List<Transfer> transfers = Arrays.asList(
                new Transfer(10, 5),   // weight, cost
                new Transfer(15, 10),
                new Transfer(5, 3)
        );
        TransferRequest request = new TransferRequest(20, transfers); // Max weight = 20

        // Act
        TransferResponse response = transferService.findOptimalTransfers(request);

        // Assert
        assertNotNull(response);
        assertEquals(13, response.getTotalCost());
        assertEquals(20, response.getTotalWeight());
        assertEquals(2, response.getSelectedTransfers().size());
        assertTrue(response.getSelectedTransfers().contains(transfers.get(0)));
        assertTrue(response.getSelectedTransfers().contains(transfers.get(1)));
    }

    @Test
    public void testFindOptimalTransfers_whenMaxWeightExceeded() {

        List<Transfer> transfers = Arrays.asList(
                new Transfer(10, 5),
                new Transfer(20, 15)
        );
        TransferRequest request = new TransferRequest(10, transfers); // Max weight = 10

        TransferResponse response = transferService.findOptimalTransfers(request);

        assertNotNull(response);
        assertEquals(5, response.getTotalCost());
        assertEquals(10, response.getTotalWeight());
        assertEquals(1, response.getSelectedTransfers().size());
        assertTrue(response.getSelectedTransfers().contains(transfers.get(0)));
    }

    @Test
    public void testFindOptimalTransfers_whenNoTransfersFit() {

        List<Transfer> transfers = Arrays.asList(
                new Transfer(20, 15),
                new Transfer(25, 20)
        );
        TransferRequest request = new TransferRequest(10, transfers); // Max weight = 10

        TransferResponse response = transferService.findOptimalTransfers(request);

        assertNotNull(response);
        assertEquals(0, response.getTotalCost());
        assertEquals(0, response.getTotalWeight());
        assertTrue(response.getSelectedTransfers().isEmpty());
    }

    @Test
    public void testFindOptimalTransfers_whenEmptyList() {

        TransferRequest request = new TransferRequest(10, Arrays.asList()); // Empty list

        TransferResponse response = transferService.findOptimalTransfers(request);

        assertNotNull(response);
        assertEquals(0, response.getTotalCost());
        assertEquals(0, response.getTotalWeight());
        assertTrue(response.getSelectedTransfers().isEmpty());
    }
}
