package com.demo.vertx.verticle;

import com.demo.vertx.util.EventBusAddress;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystemOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Checkpoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.vertx.junit5.VertxTestContext;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class HttpServerVerticleTest {
//    @Mock
//    private RoutingContext mockRoutingContext;
//
//    @Mock
//    private HttpServerResponse mockResponse;
//
//    @Mock
//    private AsyncResult<Message<Object>> mockAsyncResult;
//
//    private HttpServerVerticle httpServerVerticle;

    Vertx vertx;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        httpServerVerticle = new HttpServerVerticle();
//        httpServerVerticle.vertx = mock(Vertx.class);
//
//
//        EventBus mockEventBus = mock(EventBus.class);
//        when(httpServerVerticle.vertx.eventBus()).thenReturn(mockEventBus);
//    }
//
//    @Test
//    public void testSaveStudent() {
//        JsonObject student = new JsonObject()
//                .put("name", "John Doe")
//                .put("age", 25);
//
//        when(mockRoutingContext.getBodyAsJson()).thenReturn(student);
//        when(mockRoutingContext.response()).thenReturn(mockResponse);
//        when(mockRoutingContext.vertx()).thenReturn(httpServerVerticle.vertx);
//
//        // Simulate a failed result with a non-null cause
//        Throwable failureCause = new RuntimeException("Some error occurred");
//        when(mockAsyncResult.succeeded()).thenReturn(false);
//        when(mockAsyncResult.cause()).thenReturn(failureCause);
//
//        when(httpServerVerticle.vertx.eventBus().request(eq(EventBusAddress.NAME_SAVE_ADDRESS), eq(student), any(Handler.class)))
//                .thenAnswer(invocation -> {
//                    Handler<AsyncResult<Message<Object>>> handler = invocation.getArgument(2);
//                    handler.handle(mockAsyncResult);
//                    return null;
//                });
//
//        // Call the method under test
//        httpServerVerticle.saveStudent(mockRoutingContext);
//
//        // Verify the interaction with the event bus
//        verify(httpServerVerticle.vertx.eventBus()).request(
//                eq(EventBusAddress.NAME_SAVE_ADDRESS),
//                eq(student),
//                any(Handler.class)
//        );
//
//        // Verify the response in the failure case
//        verify(mockResponse).end(failureCause.getMessage());
//
//        // Other assertions or verifications for the test case
//    }

}
