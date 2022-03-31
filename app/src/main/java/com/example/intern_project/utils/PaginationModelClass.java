package com.example.intern_project.utils;

import com.example.intern_project.models.ApiModelClass;
import com.example.intern_project.models.MetadataModel;

import java.util.List;

public class PaginationModelClass {
    private MetadataModel metadataModel;
    private List<ApiModelClass> data;

    public MetadataModel getMetadataModel() {
        return metadataModel;
    }

    public void setMetadataModel(MetadataModel metadataModel) {
        this.metadataModel = metadataModel;
    }

    public List<ApiModelClass> getData() {
        return data;
    }

    public void setData(List<ApiModelClass> data) {
        this.data = data;
    }
}
