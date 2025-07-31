package com.clecio.orderhub.dto.upskill;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpskillCustomerDTO {

    private String name;
    private String email;

    @JsonProperty("cellphone")
    private String cellphone;

    @JsonProperty("taxId")
    private String taxId;
}

