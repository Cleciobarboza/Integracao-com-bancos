package com.clecio.orderhub.dto.upskill;

public class UpskillCustomerResponseDTO {

    private UpskillCustomerMetadataDTO data;
    private String error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpskillCustomerMetadataDTO {
        private String id;
        private UpskillCustomerDTO metadata;
    }
}

