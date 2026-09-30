package com.leadpoc.backend.controller;

import com.leadpoc.backend.model.Lead;
import com.leadpoc.backend.service.GraphApiService;
import com.leadpoc.backend.service.LeadStore;
import com.leadpoc.backend.websocket.LeadWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final GraphApiService graphApiService;
    private final LeadWebSocketHandler webSocketHandler;
    private final LeadStore leadStore;

    @Value("${meta.verify-token}")
    private String verifyToken;

    public WebhookController(GraphApiService graphApiService,
                              LeadWebSocketHandler webSocketHandler,
                              LeadStore leadStore) {
        this.graphApiService = graphApiService;
        this.webSocketHandler = webSocketHandler;
        this.leadStore = leadStore;
    }

    @GetMapping
    public ResponseEntity<String> verify(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String token,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token) && challenge != null) {
            return ResponseEntity.ok(challenge);
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    @SuppressWarnings("unchecked")
    @PostMapping
    public ResponseEntity<String> receive(@RequestBody Map<String, Object> payload) {

        List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.getOrDefault("entry", List.of());

        for (Map<String, Object> entry : entries) {
            List<Map<String, Object>> changes = (List<Map<String, Object>>) entry.getOrDefault("changes", List.of());
            for (Map<String, Object> change : changes) {
                if (!"leadgen".equals(change.get("field"))) {
                    continue;
                }
                Map<String, Object> value = (Map<String, Object>) change.get("value");
                String leadgenId = String.valueOf(value.get("leadgen_id"));
                String pageId = String.valueOf(value.get("page_id"));
                processLeadAsync(leadgenId, pageId);
            }
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    @Async
    public void processLeadAsync(String leadgenId, String pageId) {
        try {
            Lead lead = graphApiService.fetchLead(leadgenId);
            lead.setPageId(pageId);
            leadStore.add(lead);
            webSocketHandler.broadcastLead(lead);
        } catch (Exception e) {
            System.err.println("Failed to resolve leadgen_id=" + leadgenId + ": " + e.getMessage());
        }
    }
}
