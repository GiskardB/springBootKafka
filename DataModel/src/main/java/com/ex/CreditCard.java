package com.ex;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "expiration",
        "number",
        "pin",
        "security"
})
public class CreditCard {

    @JsonProperty("expiration")
    private String expiration;
    @JsonProperty("number")
    private String number;
    @JsonProperty("pin")
    private Integer pin;
    @JsonProperty("security")
    private Integer security;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("expiration")
    public String getExpiration() {
        return expiration;
    }

    @JsonProperty("expiration")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("pin")
    public Integer getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(Integer pin) {
        this.pin = pin;
    }

    @JsonProperty("security")
    public Integer getSecurity() {
        return security;
    }

    @JsonProperty("security")
    public void setSecurity(Integer security) {
        this.security = security;
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
