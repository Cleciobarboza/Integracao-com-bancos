package com.clecio.orderhub.dto.upskill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

