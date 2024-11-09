package me.easylearnz.lb.strategy;

import java.util.List;
import java.util.Random;

public class RandomSelectionStrategy implements LBStrategy {
    private Random random = new Random();

    @Override
    public String selectServer(List<String> servers) {
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
