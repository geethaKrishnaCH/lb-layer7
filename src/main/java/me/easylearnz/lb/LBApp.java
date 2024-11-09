package me.easylearnz.lb;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LBApp {
    public static void main(String[] args) {
        List<String> servers = Arrays.asList(
                "http://localhost:8081",
                "http://localhost:8082",
                "http://localhost:8083"
        );
        LoadBalancer loadBalancer = new LoadBalancer(servers);
        LoadBalancerServer loadBalancerServer = new LoadBalancerServer(loadBalancer);
        new Thread(() -> loadBalancerServer.start(8000)).start();
    }
}