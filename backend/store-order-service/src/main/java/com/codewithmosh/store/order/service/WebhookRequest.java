package com.codewithmosh.store.order.service;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebhookRequest {
    private Map<String, String> header;
    private String payload;
}
