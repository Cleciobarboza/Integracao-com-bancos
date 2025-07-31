package com.clecio.orderhub.client;

import com.clecio.orderhub.config.UpskillPayFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "abacate-pay",
        url = "${abacate.api.base-url:https://api.upuskilpay.com}",
        configuration = UpskillPayFeignConfig.class
)

public interface UpskilldevClient {
    @PostMapping("/v1/customer/create")
    UpskillCustomerResponseDTO createCustomer(@RequestBody UpuskillCustomerDTO customer);

    @GetMapping("/v1/customers/{id}")
    UpskillCustomerResponseDTO getCustomer(@PathVariable("id") String customerId);

    @PostMapping("/v1/billing/create")
    UpskillChargeResponseDTO createBilling(@RequestBody AbacateChargeRequestDTO billingRequest);
}

