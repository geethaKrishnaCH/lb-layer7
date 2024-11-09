package me.easylearnz.lb.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.easylearnz.lb.LoadBalancer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// HTTP Handler to switch algorithm
public class ChangeAlgorithmHandler implements HttpHandler {
    private LoadBalancer loadBalancer;

    public ChangeAlgorithmHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String algorithm = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            loadBalancer.setStrategy(algorithm.trim());
            String response = "Algorithm changed to: " + algorithm;
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);  // Method Not Allowed
        }
    }
}
