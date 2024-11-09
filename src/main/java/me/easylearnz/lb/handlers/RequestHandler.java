package me.easylearnz.lb.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.easylearnz.lb.LoadBalancer;

public class RequestHandler implements HttpHandler {

    private final LoadBalancer loadBalancer;

    public RequestHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(HttpExchange exchange) {
        CompletableFuture.runAsync(() -> forwardRequest(exchange));
    }

    private void forwardRequest(HttpExchange exchange) {
        String backendServer = loadBalancer.getNextServer();
        try {
            URL url = new URL(backendServer + exchange.getRequestURI().toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set the request method
            connection.setRequestMethod(exchange.getRequestMethod());

            // set the headers
            exchange.getRequestHeaders().forEach((key, values) -> {
                for (String value : values) {
                    connection.setRequestProperty(key, value);
                }
            });

            // forward the request body
            if (exchange.getRequestMethod().equals("PUT")
                    || exchange.getRequestMethod().equals("POST")) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    exchange.getRequestBody().transferTo(os);
                }
            }

            // send back the response to the client
            int responseCode = connection.getResponseCode();
            exchange.sendResponseHeaders(responseCode, connection.getContentLength());

            try (InputStream is = connection.getInputStream()) {
                is.transferTo(exchange.getResponseBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to forward request to backend server: " + backendServer);
            try {
                exchange.sendResponseHeaders(503, -1);
            } catch (IOException io) {
                io.printStackTrace();
            }
        } finally {
            exchange.close();
        }
    }
}