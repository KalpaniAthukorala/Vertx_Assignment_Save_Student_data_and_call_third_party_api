package com.demo.vertx.verticle;

import com.demo.vertx.util.EventBusAddress;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class ThirdPartyVerticle extends AbstractVerticle {
    private WebClient webClient;

    @Override
    public void start() {
        webClient = WebClient.create(vertx);
        vertx.eventBus().consumer(EventBusAddress.CALL_THIRD_PARTY_SERVICE, message -> {
            JsonObject student = (JsonObject) message.body();
            sendRequestToThirdParty(student)
                    .onSuccess(response -> {
                        System.out.println("Successfully sent request to the third-party service");
                    })
                    .onFailure(cause -> {
                        System.out.println("Failed to send request to the third-party service");
                    });
        });
    }

    public Future<HttpResponse<Buffer>> sendRequestToThirdParty(JsonObject student) {
        Promise<HttpResponse<Buffer>> promise = Promise.promise();
        webClient.post(8081, "localhost", "/demo/service")
                .sendJsonObject(student)
                .onSuccess(response -> {
                    promise.complete(response);
                })
                .onFailure(cause -> {
                    promise.fail(cause);
                });
        return promise.future();
    }

}
