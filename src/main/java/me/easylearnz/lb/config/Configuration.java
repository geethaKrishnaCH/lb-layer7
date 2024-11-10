package me.easylearnz.lb.config;

import java.util.List;

import me.easylearnz.lb.dto.ServerInfo;

public class Configuration {
  private List<ServerInfo> servers;
  private String algorithm;

  public List<ServerInfo> getServers() {
    return servers;
  }

  public void setServers(List<ServerInfo> servers) {
    this.servers = servers;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

}
