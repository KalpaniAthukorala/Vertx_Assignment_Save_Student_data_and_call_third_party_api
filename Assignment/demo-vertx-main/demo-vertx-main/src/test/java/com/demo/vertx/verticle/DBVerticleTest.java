package com.demo.vertx.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.jdbcclient.JDBCPool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static java.util.UUID.randomUUID;

@RunWith(VertxUnitRunner.class)
public class DBVerticleTest {

    private Vertx vertx;
    private DBVerticle dbVerticle;
    private JDBCPool jdbcPool;

    @Before
    public void setUp(TestContext context) {
        vertx = Vertx.vertx();
        dbVerticle = new DBVerticle();

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("url", "jdbc:mysql://localhost:3306/vertxdemo");
        jsonObject.put("driver_class", "com.mysql.cj.jdbc.Driver");
        jsonObject.put("max_pool_size", 30);
        jsonObject.put("user", "root");
        jdbcPool = JDBCPool.pool(vertx, jsonObject);

        vertx.deployVerticle(dbVerticle, context.asyncAssertSuccess());
    }

    @Test
    public void testSaveStudentData(TestContext context) {
        // Prepare test data
        JsonObject student = new JsonObject()
                .put("u_id", randomUUID().toString())
                .put("lastname", randomUUID().toString());

        Async async = context.async();

        // Call the method
        Future<String> future = dbVerticle.saveStudentData(student);

        // Verify the result
        future.onComplete(result -> {
            if (result.succeeded()) {
                context.assertEquals("Student data saved successfully", result.result());
                async.complete();
            } else {
                context.fail(result.cause());
            }
        });
    }
}
