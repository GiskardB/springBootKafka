package com.ex;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "dmy",
        "mdy",
        "raw"
})
public class Birthday {

    @JsonProperty("dmy")
    private String dmy;
    @JsonProperty("mdy")
    private String mdy;
    @JsonProperty("raw")
    private Integer raw;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dmy")
    public String getDmy() {
        return dmy;
    }

    @JsonProperty("dmy")
    public void setDmy(String dmy) {
        this.dmy = dmy;
    }

    @JsonProperty("mdy")
    public String getMdy() {
        return mdy;
    }

    @JsonProperty("mdy")
    public void setMdy(String mdy) {
        this.mdy = mdy;
    }

    @JsonProperty("raw")
    public Integer getRaw() {
        return raw;
    }

    @JsonProperty("raw")
    public void setRaw(Integer raw) {
        this.raw = raw;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
