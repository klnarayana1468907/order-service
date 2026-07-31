package com.company.ecommerce.order_service.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.resilience4j.retry.event.RetryOnErrorEvent;
import io.github.resilience4j.retry.event.RetryOnSuccessEvent;

@Component
public class RetryEventListener {

    @EventListener
    public void onRetry(RetryOnErrorEvent event) {
        System.out.println("Retry Attempt : "
                + event.getNumberOfRetryAttempts());
    }

    @EventListener
    public void onSuccess(RetryOnSuccessEvent event) {
        System.out.println("Request succeeded after retries.");
    }
}
