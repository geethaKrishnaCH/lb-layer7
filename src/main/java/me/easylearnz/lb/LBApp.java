package me.easylearnz.lb;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.easylearnz.lb.config.ConfigLoader;
import me.easylearnz.lb.config.Configuration;
import me.easylearnz.lb.config.ServerInfo;

public class LBApp {

    private static final Logger LOGGER = LogManager.getLogger(LBApp.class);

    public static void main(String[] args) {

        String configFilePath = "config.json";
        ConfigLoader configLoader = new ConfigLoader(configFilePath);
        Configuration configuration = configLoader.loadConfig();
        List<String> servers = configuration.getServers().stream()
                .map(ServerInfo::getUrl)
                .toList();
        LoadBalancer loadBalancer = new LoadBalancer(servers, configuration.getAlgorithm());
        LoadBalancerServer loadBalancerServer = new LoadBalancerServer(loadBalancer);
        loadBalancerServer.start(8000);
    }
}