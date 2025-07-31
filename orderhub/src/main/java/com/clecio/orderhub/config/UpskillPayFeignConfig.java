package com.clecio.orderhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class UpskillPayFeignConfig {

    @Value("${upskill.api.token:}")
    private String abacateApiToken;

    @Bean
    public RequestInterceptor UpskillPayRequestInterceptor() {
        return new UpskillPayRequestInterceptor(upskillApiToken);
    }

    public static class UpskillPayRequestInterceptor implements RequestInterceptor {
        private final String apiToken;

        public UpskillPayRequestInterceptor(String apiToken) {
            this.apiToken = apiToken;
        }

        @Override
        public void apply(RequestTemplate template) {
            if (StringUtils.hasText(apiToken) && !"mock-token".equals(apiToken)) {
                template.header("Authorization", "Bearer " + apiToken);
                log.debug("Adicionado Bearer token para requisição Upskilltepay");
            } else {
                log.warn("Token da API Upskillpay não configurado ou usando token mock");
            }

            template.header("Content-Type", "application/json");
            template.header("Accept", "application/json");
        }
    }
}

