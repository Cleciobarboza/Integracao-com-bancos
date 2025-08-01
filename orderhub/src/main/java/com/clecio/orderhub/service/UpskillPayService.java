package com.clecio.orderhub.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clecio.orderhub.client.UpskilldevClient;
import com.clecio.orderhub.dto.upskill.UpskillChargeRequestDTO;
import com.clecio.orderhub.dto.upskill.UpskillChargeResponseDTO;
import com.clecio.orderhub.dto.upskill.UpskillCustomerDTO;
import com.clecio.orderhub.dto.upskill.UpskillCustomerResponseDTO;
import com.clecio.orderhub.entity.Order;
import com.clecio.orderhub.entity.OrderStatus;
import com.clecio.orderhub.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpskillPayService {
    private final UpskilldevClient upskillPayClient;
    private final OrderRepository orderRepository;

    @Value("${upskill.api.mock-enabled:true}")
    private boolean mockEnabled;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public String createPayment(Order order) {
        try {
            UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO upskillCustomer = getOrCreateAbacateCustomer(order);
            UpskillChargeRequestDTO billingRequest = getAbacateChargeRequestDTO(order, upskillCustomer);

            UpskillChargeResponseDTO.UpskillChargeDataDTO billingResponse = mockEnabled
                ? createMockBilling(billingRequest)
                : upskillPayClient.createBilling(billingRequest).getData();

            log.info("Billing {} criado para pedido {}: {}", mockEnabled ? "mockado" : "real", order.getId(), billingResponse.getId());

            order.setUpskillTransactionId(billingResponse.getId());
            return billingResponse.getId();

        } catch (Exception e) {
            log.error("Erro ao criar pagamento no Upskill Pay para pedido {}: {}", order.getId(), e.getMessage());
            throw new RuntimeException("Falha na integração com gateway de pagamento", e);
        }
    }

    private UpskillChargeRequestDTO getAbacateChargeRequestDTO(Order order, UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO upskillCustomer) {
        UpskillChargeRequestDTO billingRequest = new UpskillChargeRequestDTO();
        billingRequest.setFrequency("ONE_TIME");
        billingRequest.setMethods(Arrays.asList("PIX"));

        UpskillChargeRequestDTO.UpskillProductDTO product = new UpskillChargeRequestDTO.UpskillProductDTO();
        product.setExternalId(order.getId().toString());
        product.setName("Pedido #" + order.getId());
        product.setQuantity(1);
        product.setDescription("Pagamento do pedido #" + order.getId());
        product.setPrice(order.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue());

        billingRequest.setProducts(Collections.singletonList(product));
        billingRequest.setReturnUrl(baseUrl + "/orders/" + order.getId());
        billingRequest.setCompletionUrl(baseUrl + "/orders/" + order.getId() + "/success");
        billingRequest.setCustomer(upskillCustomer.getMetadata());
        billingRequest.setAbacateCustomerId(upskillCustomer.getId());
        return billingRequest;
    }

    private UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO getOrCreateAbacateCustomer(Order order) {
        if (mockEnabled) {
            UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO mockCustomer = new UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO();
            mockCustomer.setId("cust_" + UUID.randomUUID().toString().substring(0, 8));

            UpskillCustomerDTO metadata = new UpskillCustomerDTO();
            metadata.setName(order.getCustomer().getName());
            metadata.setEmail(order.getCustomer().getEmail());
            metadata.setCellphone(order.getCustomer().getPhone());
            metadata.setTaxId("");

            mockCustomer.setMetadata(metadata);
            return mockCustomer;
        } else {
            try {
                UpskillCustomerDTO customerRequest = new UpskillCustomerDTO();
                customerRequest.setName(order.getCustomer().getName());
                customerRequest.setEmail(order.getCustomer().getEmail());
                customerRequest.setCellphone(order.getCustomer().getPhone());
                customerRequest.setTaxId(order.getCustomer().getDocument());

                log.info("Criando cliente no Abacate Pay: name={}, email={}, cellphone={}, taxId={}",
                        customerRequest.getName(), customerRequest.getEmail(),
                        customerRequest.getCellphone(), customerRequest.getTaxId());

                return upskillPayClient.createCustomer(customerRequest).getData();
            } catch (Exception e) {
                log.error("Erro ao criar/buscar cliente no Abacatepay: {}", e.getMessage());
                throw new RuntimeException("Falha ao processar cliente no gateway de pagamento", e);
            }
        }
    }

    private UpskillChargeResponseDTO.UpskillChargeDataDTO createMockBilling(UpskillChargeRequestDTO request) {
        UpskillChargeResponseDTO.UpskillChargeDataDTO response = new UpskillChargeResponseDTO.UpskillChargeDataDTO();
        response.setId("bill_" + UUID.randomUUID().toString().substring(0, 8));
        response.setAmount(request.getProducts().get(0).getPrice());
        response.setStatus("PENDING");
        response.setFrequency(request.getFrequency());
        response.setMethods(request.getMethods());
        response.setCreatedAt(LocalDateTime.now());
        response.setCustomer(new UpskillCustomerResponseDTO.UpskillCustomerMetadataDTO("cust_13455", request.getCustomer()));
        response.setProducts(Collections.singletonList(
                new UpskillChargeResponseDTO.UpskillChargeDataDTO.UpskillProductResponseDTO(
                        "prod_" + UUID.randomUUID().toString().substring(0, 8),
                        request.getProducts().get(0).getExternalId(),
                        request.getProducts().get(0).getQuantity()
                )
        ));

        String paymentLink = baseUrl + "/mock-payment/" + response.getId();
        response.setUrl(paymentLink);

        return response;
    }

    @Transactional
    public void processWebhook(String billingId, String event, String status) {
        try {
            log.info("Processando webhook do Upskillpay - Billing: {}, Event: {}, Status: {}",
                    billingId, event, status);

            Optional<Order> orderOpt = orderRepository.findByUpskillTransactionId(billingId);

            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                OrderStatus newStatus = mapUpskillStatusToOrderStatus(status, event);

                if (newStatus != null && !newStatus.equals(order.getStatus())) {
                    order.setStatus(newStatus);
                    if (newStatus == OrderStatus.PAID) {
                        order.setPaidAt(LocalDateTime.now());
                    }
                    orderRepository.save(order);

                    log.info("Pedido {} atualizado via webhook do Upskill Pay para status: {}", order.getId(), newStatus);
                }
            } else {
                log.warn("Pedido não encontrado para billing ID: {}", billingId);
            }
        } catch (Exception e) {
            log.error("Erro ao processar webhook do Upskillpay: {}", e.getMessage(), e);
        }
    }

    private OrderStatus mapUpskillStatusToOrderStatus(String status, String event) {
        if ("billing.paid".equals(event) || "PAID".equals(status)) {
            return OrderStatus.PAID;
        } else if ("billing.failed".equals(event) || "billing.cancelled".equals(event) || "FAILED".equals(status)) {
            return OrderStatus.FAILED;
        } else if ("PENDING".equals(status)) {
            return OrderStatus.PENDING_PAYMENT;
        }
        return null;
    }
}