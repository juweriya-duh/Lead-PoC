package com.leadpoc.backend.service;

import com.leadpoc.backend.model.Lead;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class LeadStore {

    private final LinkedList<Lead> leads = new LinkedList<>();

    public synchronized void add(Lead lead) {
        leads.addFirst(lead);
    }

    public synchronized List<Lead> getAll() {
        return Collections.unmodifiableList(new LinkedList<>(leads));
    }
}
