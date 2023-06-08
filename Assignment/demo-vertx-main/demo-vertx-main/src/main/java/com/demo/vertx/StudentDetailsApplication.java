package com.demo.vertx;

import com.demo.vertx.verticle.DBVerticle;
import com.demo.vertx.verticle.HttpServerVerticle;
import com.demo.vertx.verticle.ThirdPartyVerticle;
import io.vertx.core.Vertx;

import java.util.logging.Logger;

public class StudentDetailsApplication {
    static Logger log = Logger.getLogger(StudentDetailsApplication.class.getName());
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new HttpServerVerticle());
        vertx.deployVerticle(new DBVerticle());
        vertx.deployVerticle(new ThirdPartyVerticle());
        log.info("Verticle deployed successfully");
    }
}
