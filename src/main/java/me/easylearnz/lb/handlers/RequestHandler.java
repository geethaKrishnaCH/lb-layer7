package me.easylearnz.lb.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.easylearnz.lb.LoadBalancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestHandler implements HttpHandler {

    private final LoadBalancer loadBalancer;

    public RequestHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String backendServer = loadBalancer.getNextServer();
        try {
            URL url = new URL(backendServer + httpExchange.getRequestURI().toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // set the request method
            connection.setRequestMethod(httpExchange.getRequestMethod());

            // set the headers
            httpExchange.getRequestHeaders().forEach((key, values) -> {
                for (String value : values) {
                    connection.setRequestProperty(key, value);
                }
            });

            // forward the request body
            if (httpExchange.getRequestMethod().equals("PUT")
                    || httpExchange.getRequestMethod().equals("POST")) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    httpExchange.getRequestBody().transferTo(os);
                }
            }

            // send back the response to the client
            int responseCode = connection.getResponseCode();
            httpExchange.sendResponseHeaders(responseCode, connection.getContentLength());

            try (InputStream is = connection.getInputStream()) {
                is.transferTo(httpExchange.getResponseBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to forward request to backend server: " + backendServer);
            httpExchange.sendResponseHeaders(503, -1);
        } finally {
            httpExchange.close();
        }
    }
}