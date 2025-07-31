package com.clecio.orderhub.dto.upskill;

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
}

