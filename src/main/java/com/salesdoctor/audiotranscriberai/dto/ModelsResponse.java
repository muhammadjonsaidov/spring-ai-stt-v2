package com.salesdoctor.audiotranscriberai.dto;

import java.util.List;

public class ModelsResponse {
    private List<String> models;

    public ModelsResponse(List<String> models) {
        this.models = models;
    }

    public List<String> getModels() { return models; }
    public void setModels(List<String> models) { this.models = models; }
}
