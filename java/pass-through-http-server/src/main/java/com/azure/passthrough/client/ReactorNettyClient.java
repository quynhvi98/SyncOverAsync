package com.azure.passthrough.client;

import io.netty.handler.codec.http.HttpMethod;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.atomic.AtomicLong;

public class ReactorNettyClient implements SyncHttpClient, AsyncHttpClient {
    private HttpClient client;

    public ReactorNettyClient() {
        client = HttpClient.create()
                ;
//                .tcpConfiguration(tcpClient -> tcpClient.proxy(ts -> ts.type(Proxy.HTTP).address(new InetSocketAddress("localhost", 8888))));
    }

    public Mono<String> sendAsync(String uri, AtomicLong sent) {
        return client.request(HttpMethod.GET).uri(uri)
                .send((req, out) -> {
                    sent.incrementAndGet();
                    return out;
                })
                .responseContent().aggregate().asString();
    }

    // not recommended
    public String send(String uri, AtomicLong sent) {
        return sendAsync(uri, sent).block();
    }
}
