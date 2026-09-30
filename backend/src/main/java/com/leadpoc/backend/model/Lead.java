package com.leadpoc.backend.model;

import java.util.LinkedHashMap;
import java.util.Map;


public class Lead {

    private String leadgenId;
    private String formId;
    private String pageId;
    private String createdTime;
    private Map<String, String> fields = new LinkedHashMap<>();

    public Lead() {
    }

    public Lead(String leadgenId, String formId, String pageId, String createdTime, Map<String, String> fields) {
        this.leadgenId = leadgenId;
        this.formId = formId;
        this.pageId = pageId;
        this.createdTime = createdTime;
        this.fields = fields;
    }

    public String getLeadgenId() {
        return leadgenId;
    }

    public void setLeadgenId(String leadgenId) {
        this.leadgenId = leadgenId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
