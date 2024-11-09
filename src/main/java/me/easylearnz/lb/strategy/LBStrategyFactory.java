package me.easylearnz.lb.strategy;

import java.security.KeyStore.Entry;
import java.util.HashMap;
import java.util.Map;

public class LBStrategyFactory {
    private static final Map<String, LBStrategy> strategies = new HashMap<>();

    static {
        strategies.put("ROUND_ROBIN", new RoundRobinStrategy());
        strategies.put("RANDOM_SELECTION", new RandomSelectionStrategy());
    }

    public static LBStrategy getStrategy(String algorithm) {
        return strategies.getOrDefault(algorithm, strategies.get("ROUND_ROBIN"));
    }

    public static LBStrategy getStrategy() {
        return strategies.get("ROUND_ROBIN");
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
