package com.leadpoc.backend.service;

import com.leadpoc.backend.model.Lead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




@Service
public class GraphApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${meta.page-access-token}")
    private String pageAccessToken;

    @Value("${meta.graph-api-version:v19.0}")
    private String graphApiVersion;

    @SuppressWarnings("unchecked")
    public Lead fetchLead(String leadgenId) {
        String url = String.format(
                "https://graph.facebook.com/%s/%s?fields=field_data,created_time,form_id,ad_id&access_token=%s",
                graphApiVersion, leadgenId, pageAccessToken);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null) {
            throw new IllegalStateException("Empty response from Graph API for leadgen_id=" + leadgenId);
        }

        Map<String, String> fields = new LinkedHashMap<>();
        List<Map<String, Object>> fieldData = (List<Map<String, Object>>) response.getOrDefault("field_data", List.of());
        for (Map<String, Object> entry : fieldData) {
            String name = (String) entry.get("name");
            List<String> values = (List<String>) entry.getOrDefault("values", List.of());
            fields.put(name, values.isEmpty() ? "" : values.get(0));
        }

        Lead lead = new Lead();
        lead.setLeadgenId(leadgenId);
        lead.setFormId((String) response.get("form_id"));
        lead.setCreatedTime((String) response.get("created_time"));
        lead.setFields(fields);
        return lead;
    }
}
