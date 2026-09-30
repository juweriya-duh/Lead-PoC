package com.leadpoc.backend.controller;

import com.leadpoc.backend.model.Lead;
import com.leadpoc.backend.service.LeadStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "*")
public class LeadController {

    private final LeadStore leadStore;

    public LeadController(LeadStore leadStore) {
        this.leadStore = leadStore;
    }

    @GetMapping
    public List<Lead> getAll() {
        return leadStore.getAll();
    }
}
