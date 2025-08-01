package com.clecio.orderhub.dto.upskill;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpskillWebhookDTO {


    private String event;
    private UpskillWebhookDataDTO data;

    @JsonProperty("devMode")
    private Boolean devMode;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpskillWebhookDataDTO {
        private UpskillPaymentDTO payment;
        private UpskillBillingDTO billing;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UpskillPaymentDTO {
            private Integer amount;
            private Integer fee;
            private String method;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UpskillBillingDTO {
            private String id;
            private Integer amount;
            private String status;
            private String frequency;
            private UpskillCustomerDTO customer;
        }
  
}

    public Object getEnent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEnent'");
    }
}

