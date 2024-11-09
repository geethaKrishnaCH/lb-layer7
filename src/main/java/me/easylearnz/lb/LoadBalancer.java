package me.easylearnz.lb;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import me.easylearnz.lb.strategy.LBStrategy;
import me.easylearnz.lb.strategy.LBStrategyFactory;

public class LoadBalancer {
    private List<String> servers = new CopyOnWriteArrayList<>();
    private LBStrategy strategy;

    public LoadBalancer(List<String> servers) {
        this.servers.addAll(servers);
        this.strategy = LBStrategyFactory.getStrategy();
    }

    public LoadBalancer(List<String> servers, LBStrategy strategy) {
        this.servers = servers;
        this.strategy = strategy;
    }

    public void setStrategy(String newStrategy) {
        this.strategy = LBStrategyFactory.getStrategy(newStrategy);
    }

    public String getNextServer() {
        return strategy.selectServer(this.servers);
    }

    public void addServer(String serverUrl) {
        this.servers.add(serverUrl);
    }

    public void removeServer(String serverUrl) {
        servers.remove(serverUrl);
        System.out.println(servers);
    }

    public void healthCheck() {
        for (String server : servers) {
            try {
                URL url = new URL(server);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                if (!(responseCode >= 200 && responseCode < 400)) {
                    removeServer(server);
                    System.out.println("Removed unhealthy server: " + server);
                }
            } catch (IOException e) {
                removeServer(server);
                System.out.println("Removed unreachable server: " + server);
            }
        }
    }
}
