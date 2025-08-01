package com.clecio.orderhub.dto.upskill;

import java.time.LocalDateTime;
import java.util.List;

import com.clecio.orderhub.dto.upskill.UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UpskillChargeResponseDTO {

    private String error;
    private UpskillChargeDataDTO data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpskillChargeDataDTO {
        private String id;
        private Integer amount;
        private String status;
        private String frequency;
        @JsonProperty("devMode")
        private Boolean devMode;
        private List<String> methods;
        @JsonProperty("allowCoupons")
        private Boolean allowCoupons;
        private List<Object> coupons;
        @JsonProperty("couponsUsed")
        private List<Object> couponsUsed;
        private String url;
        @JsonProperty("createdAt")
        private LocalDateTime createdAt;
        @JsonProperty("updatedAt")
        private LocalDateTime updatedAt;
        private List<UpskillProductResponseDTO> products;
        private UpskillCustomerMetadataDTO metadata;
        private UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO customer;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UpskillProductResponseDTO {
            private String id;
            private String externalId;
            private Integer quantity;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AbacateChargeMetadataDTO {
            private Integer fee;
            private String returnUrl;
            private String completionUrl;
        }
    }
}

