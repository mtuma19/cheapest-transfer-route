package com.example.Entity;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    public TransferResponse findOptimalTransfers(TransferRequest request) {
        List<Transfer> availableTransfers = request.getAvailableTransfers();
        int maxWeight = request.getMaxWeight();

        // Sort transfers by cost per weight unit (Descending order)
        availableTransfers.sort((a, b) -> Double.compare(b.getCost() / (double)b.getWeight(), a.getCost() / (double)a.getWeight()));

        List<Transfer> selectedTransfers = new ArrayList<>();
        int totalWeight = 0;
        int totalCost = 0;

        for (Transfer transfer : availableTransfers) {
            if (totalWeight + transfer.getWeight() <= maxWeight) {
                selectedTransfers.add(transfer);
                totalWeight += transfer.getWeight();
                totalCost += transfer.getCost();
            }
        }

        return new TransferResponse(selectedTransfers, totalCost, totalWeight);
    }
}
