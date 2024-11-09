package me.easylearnz.lb.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.easylearnz.lb.LoadBalancer;
import me.easylearnz.lb.health.dto.LBStatusDTO;
import me.easylearnz.lb.strategy.LBStrategyFactory;

public class StatusHandler implements HttpHandler {
    private LoadBalancer loadBalancer;

    public StatusHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            List<String> healthyServers = loadBalancer.getServers();
            List<String> unHealthyServers = loadBalancer.getUnhealthyServers();
            String currenStrategy = LBStrategyFactory.getStrategyName(loadBalancer.getStrategy());
            LBStatusDTO serverStatus = new LBStatusDTO(healthyServers, unHealthyServers, currenStrategy);
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(serverStatus);

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, -1); // Invalid input
        }
        exchange.close();
    }
}