package com.yamankwefati.webshopapi.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.stripe.Stripe;

@Configuration
public class StripeConfig {

    private final Environment environment;

    public StripeConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = environment.getProperty("stripe.api.key");
    }
}
