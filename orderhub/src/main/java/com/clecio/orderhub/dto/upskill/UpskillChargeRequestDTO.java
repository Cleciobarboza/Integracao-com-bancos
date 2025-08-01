package com.clecio.orderhub.dto.upskill;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpskillChargeRequestDTO {
    private String frequency = "ONE_TIME";
    private List<String> methods;
    private List<UpskillProductDTO> products;

    @JsonProperty("returnUrl")
    private String returnUrl;

    @JsonProperty("completionUrl")
    private String completionUrl;

    private UpskillCustomerDTO customer;

    @JsonProperty("customerId")
    private String upskillCustomerId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpskillProductDTO {
        @JsonProperty("externalId")
        private String externalId;
        private String name;
        private Integer quantity;
        private Integer price;
        private String description;
    }

    public void setProducts(List<UpskillProductDTO> singletonList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProducts'");
    }

    public void setReturnUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setReturnUrl'");
    }

    public void setCompletionUrl(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCompletionUrl'");
    }

    public void setCustomer(UpskillCustomerDTO metadata) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCustomer'");
    }

    public void setAbacateCustomerId(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAbacateCustomerId'");
    }

    public LocalDateTime getProducts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProducts'");
    }

    public String getFrequency() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFrequency'");
    }

    public List<String> getMethods() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMethods'");
    }

    public void setFrequency(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFrequency'");
    }

    public void setMethods(List<String> asList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setMethods'");
    }
}

