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

    @BeforeEach
    void prepare() {
        vertx = Vertx.vertx(new VertxOptions()
                .setMaxEventLoopExecuteTime(1000)
                .setPreferNativeTransport(true)
                .setFileSystemOptions(new FileSystemOptions().setFileCachingEnabled(true)));
    }

    @Test
    @DisplayName("Deploy SampleVerticle")
    public void deploySampleVerticle() {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();
        vertx.deployVerticle(new HttpServerVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    @DisplayName("Make a HTTP client request to SampleVerticle")
    public void httpRequest() {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();
        WebClient webClient = WebClient.create(vertx);
        vertx.deployVerticle(new HttpServerVerticle(), testContext.succeeding(id -> {
            webClient.get(8081, "localhost", "/yo")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(resp -> {
                        testContext.verify(() -> {
                            testContext.completeNow();
                        });
                    }));
        }));
    }

    @AfterEach
    void cleanup() {
        vertx.close();
    }

    @Test
    @DisplayName("Deploy a HTTP service verticle and make 10 requests")
    public void useSampleVerticle() {
        Vertx vertx = Vertx.vertx();
        VertxTestContext testContext = new VertxTestContext();
        WebClient webClient = WebClient.create(vertx);
        Checkpoint deploymentCheckpoint = testContext.checkpoint();
        Checkpoint requestCheckpoint = testContext.checkpoint(10);

        vertx.deployVerticle(new HttpServerVerticle(), testContext.succeeding(id -> {
            deploymentCheckpoint.flag();

            for (int i = 0; i < 10; i++) {
                webClient.get(11981, "localhost", "/")
                        .as(BodyCodec.string())
                        .send(testContext.succeeding(resp -> {
                            testContext.verify(() -> {
                                requestCheckpoint.flag();
                            });
                        }));
            }
        }));
    }
}
