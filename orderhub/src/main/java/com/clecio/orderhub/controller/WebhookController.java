package com.clecio.orderhub.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final OrderService orderService;

    @Value("${abacate.webhook.secret:default-secret}")
    private String webhookSecret;

    @Value("${abacate.webhook.signature.enabled:false}")
    private boolean signatureValidationEnabled;

    @PostMapping("/abacate")
    public ResponseEntity<String> handleAbacateWebhook(
            @RequestBody UpskillWebhookDTO webhook,
            @RequestHeader(value = "X-Abacate-Signature", required = false) String signature) {

        try {
            String billingId = webhook.getData() != null && webhook.getData().getBilling() != null ?
                    webhook.getData().getBilling().getId() : "unknown";
            String status = webhook.getData() != null && webhook.getData().getBilling() != null ?
                    webhook.getData().getBilling().getStatus() : "unknown";

            log.info("Recebido webhook do Abacate Pay: event={}, billingId={}, status={}",
                    webhook.getEvent(), billingId, status);

            if (signatureValidationEnabled && !validateSignature(webhook, signature)) {
                log.warn("Assinatura inválida no webhook do Upskill Pay");
                return ResponseEntity.badRequest().body("Invalid signature");
            }

            if (webhook.getData() != null && webhook.getData().getBilling() != null) {
                billingId = webhook.getData().getBilling().getId();

                switch (webhook.getEvent().toLowerCase()) {
                    case "billing.paid":
                        orderService.updateOrderFromUpskillWebhook(billingId, "PAID");
                        break;

                    case "billing.failed":
                    case "billing.cancelled":
                        orderService.updateOrderFromUpskillWebhook(billingId, "FAILED");
                        break;

                    case "billing.pending":
                        orderService.updateOrderFromUpskillWebhook(billingId, "PENDING");
                        break;

                    default:
                        log.info("Evento de webhook não processado: {}", webhook.getEvent());
                        break;
                }
            } else {
                log.warn("Webhook recebido sem dados de billing válidos");
            }

            log.info("Webhook do UpskillDev Pay processado com sucesso: billingId={}", billingId);
            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            log.error("Erro ao processar webhook do UpskillDev  Pay: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error processing webhook");
        }
    }

    private boolean validateSignature(UpskillWebhookDTO webhook, String receivedSignature) {
        if (receivedSignature == null || receivedSignature.isEmpty()) {
            return false;
        }

        try {
            String billingId = webhook.getData() != null && webhook.getData().getBilling() != null ?
                    webhook.getData().getBilling().getId() : "";
            String status = webhook.getData() != null && webhook.getData().getBilling() != null ?
                    webhook.getData().getBilling().getStatus() : "";
            String payload = webhook.getEvent() + billingId + status;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String calculatedSignature = HexFormat.of().formatHex(hash);

            return calculatedSignature.equals(receivedSignature.toLowerCase());

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Erro ao validar assinatura do webhook: {}", e.getMessage());
            return false;
        }
    }
}