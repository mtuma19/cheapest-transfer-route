package com.example.Entity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/optimize")
    public TransferResponse optimizeTransfers(@RequestBody TransferRequest request) {
        return transferService.findOptimalTransfers(request);
    }
}
