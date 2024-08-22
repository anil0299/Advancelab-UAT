package com.Advanceelab.cdacelabAdvance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResult {
    private List<HashMap<String, String>> list;
    private List<Map<String, Object>> formattedResult;

    public SearchResult() {
    }

    public SearchResult(List<HashMap<String, String>> list, List<Map<String, Object>> formattedResult) {
        this.list = list;
        this.formattedResult = formattedResult;
    }

    public List<HashMap<String, String>> getList() {
        return list;
    }

    public void setList(List<HashMap<String, String>> list) {
        this.list = list;
    }

    public List<Map<String, Object>> getFormattedResult() {
        return formattedResult;
    }

    public void setFormattedResult(List<Map<String, Object>> formattedResult) {
        this.formattedResult = formattedResult;
    }
}

