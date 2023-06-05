package com.altiparmakov.apigateway.filter;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CacheFilter implements GlobalFilter {

    private final CacheManager cacheManager;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Cache cache = cacheManager.getCache("responseCache");

        CachedRequest cachedRequest = getCachedRequest(exchange.getRequest());
        if (nonNull(cache) && nonNull(cache.get(cachedRequest))) {
            log.info("Return cached response for request.");
            CachedResponse cachedResponse = cache.get(cachedRequest, CachedResponse.class);

            ServerHttpResponse httpResponse = exchange.getResponse();
            httpResponse.setStatusCode(cachedResponse.getStatus());
            httpResponse.getHeaders().addAll(cachedResponse.getHeaders());
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(cachedResponse.body);
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }

        ServerHttpResponse mutatedHttpResponse = getServerHttpResponse(exchange, cache, cachedRequest);
        return chain.filter(exchange.mutate().response(mutatedHttpResponse).build());
    }

    private ServerHttpResponse getServerHttpResponse(ServerWebExchange exchange, Cache cache,
                                                     CachedRequest cachedRequest) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();

        return new ServerHttpResponseDecorator(originalResponse) {

            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(flux.buffer().map(dataBuffers -> {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] responseContent = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(responseContent);
                            try {
                                outputStream.write(responseContent);
                            } catch (IOException e) {
                                throw new RuntimeException("Error while reading response stream", e);
                            }
                        });
                        if (Objects.requireNonNull(getStatusCode()).is2xxSuccessful()) {
                            CachedResponse cachedResponse = new CachedResponse(getStatusCode(), getHeaders(),
                                                                               outputStream.toByteArray());
                            log.debug("Request {} Cached response {}", cachedRequest.getPath(),
                                      new String(cachedResponse.getBody(), UTF_8));
                            cache.put(cachedRequest, cachedResponse);
                        }
                        return dataBufferFactory.wrap(outputStream.toByteArray());
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

    private CachedRequest getCachedRequest(ServerHttpRequest request) {
        return CachedRequest.builder()
                .path(request.getPath())
                .method(request.getMethod())
                .queryParams(request.getQueryParams())
                .build();
    }

    @Value
    @Builder
    private static class CachedRequest {
        RequestPath path;
        HttpMethod method;
        MultiValueMap<String, String> queryParams;
    }

    @Value
    private static class CachedResponse {
        HttpStatus status;
        HttpHeaders headers;
        byte[] body;
    }
}
