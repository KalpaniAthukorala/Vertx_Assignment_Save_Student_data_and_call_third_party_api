package com.demo.vertx.verticle;

import com.demo.vertx.util.EventBusAddress;
import com.demo.vertx.util.SQLConstants;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(DBVerticle.class);
    private JDBCPool jdbcPool;

    @Override
    public void start() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("url", "jdbc:mysql://localhost:3306/vertxdemo");
        jsonObject.put("driver_class", "com.mysql.cj.jdbc.Driver");
        jsonObject.put("max_pool_size", 30);
        jsonObject.put("user", "root");
        jdbcPool = JDBCPool.pool(
                vertx, jsonObject
        );

        vertx.eventBus().consumer(EventBusAddress.NAME_SAVE_ADDRESS, message -> {
            JsonObject student = (JsonObject) message.body();
            saveStudentData(student)
                    .onSuccess(success -> {
                        message.reply(success);
                        callThirdParty(student);
                    }).onFailure(throwable -> {
                        logger.error("Error while saving student data to database");
                        message.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), "Error while saving student data to database");
                    });
        });

    }

    public Future<String> saveStudentData(JsonObject student) {
        Promise<String> promise = Promise.promise();
        String username = student.getString("u_id");
        String lastname = student.getString("lastname");
        jdbcPool.preparedQuery(SQLConstants.INSERT_STUDENT)
                .execute(Tuple.of(username, lastname))
                .onFailure(throwable -> {
                    logger.error("Failure: {}", throwable.getCause().getMessage());
                    promise.fail(throwable.getCause().getMessage());
                })
                .onSuccess(rows -> {
                    if (rows.rowCount() > 0) {
                        promise.complete("Student data saved successfully");
                    } else {
                        promise.fail("Failed to save student data");
                    }
                });
        return promise.future();
    }

    void callThirdParty(JsonObject student) {
        vertx.eventBus().request(EventBusAddress.CALL_THIRD_PARTY_SERVICE, student);
    }
}
