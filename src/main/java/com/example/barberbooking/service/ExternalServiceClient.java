package com.example.barberbooking.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class ExternalServiceClient {

    private final WebClient webClient;
    private final String apiPath;

    // Konstruktor wstrzykuje wartości i buduje klienta raz przy starcie aplikacji
    public ExternalServiceClient(
            @Value("${_service.name}") String name,
            @Value("${_service.port}") String port,
            @Value("${_service.api_path}") String apiPath,
            WebClient.Builder wcBuilder) {

        this.apiPath = apiPath;
        String serverUrl = "http://" + name + ":" + port;
        
        this.webClient = wcBuilder.baseUrl(serverUrl).build();
        
        log.info("ExternalServiceClient initialized: URL={}, API_PATH={}", serverUrl, apiPath);
    }

    /**
     * Wywołuje zewnętrzny serwis.
     */
    public <T, R> R callService(T request, Class<R> responseType) {
        try {
            // WebClient jest thread-safe, nie potrzebujemy synchronized
            R response = webClient.post()
                    .uri(apiPath)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(request), (Class<T>) request.getClass())
                    .retrieve()
                    .bodyToMono(responseType)
                    .block(); // Blokujemy, bo aplikacja jest synchroniczna

            log.info("Service call successful: {}", response);
            return response;

        } catch (WebClientResponseException e) {
            log.error("Service call failed: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            log.error("Service call exception", e);
            return null;
        }
    }

    public String testCall() {
        return callService("Hello from client", String.class);
    }
}