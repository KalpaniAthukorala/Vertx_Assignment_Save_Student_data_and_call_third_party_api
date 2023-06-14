package com.demo.vertx.verticle;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import com.demo.vertx.util.EventBusAddress;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ThirdPartyVerticleTest {

    private Vertx vertx;
    private WebClient webClient;
    private ThirdPartyVerticle thirdPartyVerticle;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        webClient = WebClient.create(vertx);
        thirdPartyVerticle = new ThirdPartyVerticle();
        vertx.deployVerticle(thirdPartyVerticle, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testSendRequestToThirdParty(TestContext context) {
        // Prepare test data
        JsonObject student = new JsonObject()
                .put("u_id", "123")
                .put("lastname", "Smith");

        Async async = context.async();

        // Subscribe to the response from the ThirdPartyVerticle
        vertx.eventBus().consumer(EventBusAddress.CALL_THIRD_PARTY_SERVICE, message -> {
            JsonObject receivedStudent = (JsonObject) message.body();
            context.assertEquals(student, receivedStudent);
            async.complete();
        });

        // Call the method
        thirdPartyVerticle.sendRequestToThirdParty(student).onComplete(context.asyncAssertSuccess());
    }
}
