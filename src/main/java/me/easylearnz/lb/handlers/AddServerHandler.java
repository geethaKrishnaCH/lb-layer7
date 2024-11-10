package me.easylearnz.lb.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.easylearnz.lb.LoadBalancer;

public class AddServerHandler implements HttpHandler {
    private LoadBalancer loadBalancer;

    public AddServerHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            // Read the new server URL from the request body
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);
                String serverUrl = node.get("url").asText();
                loadBalancer.addServer(serverUrl);
                String response = "Server added: " + serverUrl;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                exchange.sendResponseHeaders(400, -1); // Invalid input
            }

        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
        exchange.close();
    }
}