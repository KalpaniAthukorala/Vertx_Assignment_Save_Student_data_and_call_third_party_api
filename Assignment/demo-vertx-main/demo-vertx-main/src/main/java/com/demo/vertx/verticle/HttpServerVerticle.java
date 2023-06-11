package com.demo.vertx.verticle;

import com.demo.vertx.util.EventBusAddress;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServerVerticle extends AbstractVerticle {
    public Vertx vertx;
    private WebClient webClient;
    private static final Logger logger = LogManager.getLogger(ThirdPartyVerticle.class);
    @Override
    public void start() {
        webClient = WebClient.create(vertx);
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/demo/student").handler(this::saveStudent);
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    void saveStudent(RoutingContext ctx) {
        JsonObject student = ctx.getBodyAsJson();
        vertx.eventBus().request(EventBusAddress.NAME_SAVE_ADDRESS, student, reply -> {
            if (reply.succeeded()) {
                ctx.response().end((String) reply.result().body());
            } else {
                ctx.response().end(reply.cause().getMessage());
            }
        });
    }

}
