package me.easylearnz.lb.strategy;

import java.util.List;

public interface LBStrategy {
    String selectServer(List<String> servers);
}
