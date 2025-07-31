package com.clecio.orderhub.dto.upskill;

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
        private UpskillChargeMetadataDTO metadata;
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

