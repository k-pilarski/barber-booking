package com.example.barberbooking.service;

import com.example.barberbooking.exception.PaymentGatewayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalServiceClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ExternalServiceClient externalServiceClient;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        externalServiceClient = new ExternalServiceClient("test-service", "8080", "/api/payment", webClientBuilder);
    }

    @Test
    void shouldThrowPaymentGatewayExceptionOnApiFailure() {
        // given
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(anyString(), anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(Mono.class), any(Class.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        WebClientResponseException apiException = WebClientResponseException.create(
                HttpStatus.BAD_GATEWAY.value(), "Bad Gateway", null, null, null);
                
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(apiException));

        // when & then
        PaymentGatewayException exception = assertThrows(PaymentGatewayException.class, () -> {
            externalServiceClient.callService("Test Request", String.class);
        });

        assertEquals("Awaria zewnętrznej bramki płatności. Spróbuj ponownie później.", exception.getMessage());
    }
}
