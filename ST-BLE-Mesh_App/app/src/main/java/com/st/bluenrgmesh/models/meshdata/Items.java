
package com.st.bluenrgmesh.models.meshdata;

import java.util.List;
import com.google.gson.annotations.Expose;

public class Items {

    @Expose
    private Boolean additionalProperties;
    @Expose
    private Properties properties;
    @Expose
    private List<String> required;
    @Expose
    private String type;

    public Boolean getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Boolean additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
