package me.easylearnz.lb;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpServer;

import me.easylearnz.lb.handlers.AddServerHandler;
import me.easylearnz.lb.handlers.ChangeAlgorithmHandler;
import me.easylearnz.lb.handlers.RequestHandler;

public class LoadBalancerServer {
    private final LoadBalancer loadBalancer;

    public LoadBalancerServer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public void start(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new RequestHandler(loadBalancer));
            server.createContext("/addServer", new AddServerHandler(loadBalancer));
            server.createContext("/changeAlgorithm", new ChangeAlgorithmHandler(loadBalancer));
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();
            System.out.println("Load Balancer listening on port: " + port);
            initializeHealthCheck(loadBalancer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeHealthCheck(LoadBalancer loadBalancer) {
        // Schedule health check every 10 seconds
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            try {
                loadBalancer.healthCheck();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
