package me.easylearnz.lb.strategy;

import java.util.HashMap;
import java.util.Map;

public class LBStrategyFactory {
    private static final Map<String, LBStrategy> strategies = new HashMap<>();

    static {
        strategies.put("round-robin", new RoundRobinStrategy());
        strategies.put("random", new RandomSelectionStrategy());
    }

    public static LBStrategy getStrategy(String algorithm) {
        return strategies.getOrDefault(algorithm, strategies.get("round-robin"));
    }

    public static LBStrategy getStrategy() {
        return strategies.get("round-robin");
    }

    public static String getStrategyName(LBStrategy strategy) {

        for (Map.Entry<String, LBStrategy> entry : strategies.entrySet()) {
            if (entry.getValue().equals(strategy)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
