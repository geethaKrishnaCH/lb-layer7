package me.easylearnz.lb;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.easylearnz.lb.health.HeartBeat;
import me.easylearnz.lb.strategy.LBStrategy;
import me.easylearnz.lb.strategy.LBStrategyFactory;

public class LoadBalancer {
    private List<String> servers = new CopyOnWriteArrayList<>();
    private List<String> unhealthyServers = new CopyOnWriteArrayList<>();
    private LBStrategy strategy;

    private final Logger LOGGER = LogManager.getLogger(getClass());

    public LoadBalancer(List<String> servers) {
        this.servers.addAll(servers);
        this.strategy = LBStrategyFactory.getStrategy();
    }

    public LoadBalancer(List<String> servers, String strategy) {
        this.servers.addAll(servers);
        this.strategy = LBStrategyFactory.getStrategy(strategy);
    }

    public void setStrategy(String newStrategy) {
        this.strategy = LBStrategyFactory.getStrategy(newStrategy);
    }

    public String getNextServer() {
        if (this.servers.size() == 0) {
            return null;
        }
        return strategy.selectServer(this.servers);
    }

    public void addServer(String serverUrl) {
        if (this.servers.contains(serverUrl) || this.unhealthyServers.contains(serverUrl)) {
            LOGGER.debug("Server {} already exists!", serverUrl);
            return;
        }
        this.servers.add(serverUrl);
        LOGGER.info("Server added: {}", serverUrl);
    }

    public void removeServer(String serverUrl) {
        boolean removed = false;
        if (this.servers.contains(serverUrl)) {
            removed = this.servers.remove(serverUrl);
        } else {
            removed = this.unhealthyServers.remove(serverUrl);
        }
        if (removed) {
            LOGGER.info("Server removed: {}", serverUrl);
        } else {
            LOGGER.debug("Unknown Server: {}", serverUrl);
        }
    }

    public void addUnhealthyServer(String serverUrl) {
        this.unhealthyServers.add(serverUrl);
    }

    public void removeUnhealthyServer(String serverUrl) {
        this.unhealthyServers.remove(serverUrl);
    }

    public void healthCheck() {
        for (String server : servers) {
            if (!HeartBeat.isServerHealthy(server)) {
                removeServer(server);
                addUnhealthyServer(server);
            }
        }
    }

    public void checkUnhealthyServers() {
        for (String server : unhealthyServers) {
            if (HeartBeat.isServerHealthy(server)) {
                System.out.println("Server: " + server + " is back up");
                removeUnhealthyServer(server);
                addServer(server);
            }
        }
    }

    public List<String> getServers() {
        return servers;
    }

    public List<String> getUnhealthyServers() {
        return unhealthyServers;
    }

    public LBStrategy getStrategy() {
        return strategy;
    }

}
