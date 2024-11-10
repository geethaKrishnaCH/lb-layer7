package me.easylearnz.lb;

import java.util.List;

import me.easylearnz.lb.config.ConfigLoader;
import me.easylearnz.lb.config.Configuration;
import me.easylearnz.lb.dto.ServerInfo;

public class LBApp {

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