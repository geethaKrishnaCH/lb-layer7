package me.easylearnz.lb.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LBStrategy {
    private AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public String selectServer(List<String> servers) {
        if (servers.isEmpty())
            return null;
        int index = currentIndex.getAndUpdate(i -> (i + 1) % servers.size());
        return servers.get(index);
    }
}
