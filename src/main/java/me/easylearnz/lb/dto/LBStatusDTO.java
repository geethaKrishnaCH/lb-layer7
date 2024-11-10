package me.easylearnz.lb.dto;

import java.util.List;

public class LBStatusDTO {
  private List<String> healthyServers;
  private List<String> unHealthyServers;
  private String algorithm;

  public LBStatusDTO(List<String> healthyServers, List<String> unHealthyServers, String algorithm) {
    this.healthyServers = healthyServers;
    this.unHealthyServers = unHealthyServers;
    this.algorithm = algorithm;
  }

  public List<String> getHealthyServers() {
    return healthyServers;
  }

  public void setHealthyServers(List<String> healthyServers) {
    this.healthyServers = healthyServers;
  }

  public List<String> getUnHealthyServers() {
    return unHealthyServers;
  }

  public void setUnHealthyServers(List<String> unHealthyServers) {
    this.unHealthyServers = unHealthyServers;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

}
