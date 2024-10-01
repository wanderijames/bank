package com.example.bank.domain.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record VectorClocks(Map<String, Long> clocks) {
    VectorClocks() {
        this(new HashMap<>());
    }

    public VectorClocks {
        Objects.requireNonNull(clocks);
    }

    /**
     * Mark an identifiers clock as latest
     * @param key an identifier to a service
     */
    public void incrementClock(String key) {
        Objects.requireNonNull(key);
        clocks.put(key, clocks.getOrDefault(key, 0L) + 1);
    }

    /**
     * Update our local clock from another clock
     * @param vectorClocks other vector to compare the local clock with
     */
    public void updateClocks(VectorClocks vectorClocks) {
        Objects.requireNonNull(vectorClocks);

        for (Map.Entry<String, Long> entry : vectorClocks.clocks.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            Long otherValue = clocks.getOrDefault(key, 0L);
            clocks.put(key, Math.max(value, otherValue));
        }
    }

    /**
     * Can be used to check if we have a recent event
     * @param vectorClocks other vector to compare the local clock with
     * @return true if local clock is later than the one being compared to
     */
    public boolean isGreaterThan(VectorClocks vectorClocks) {
        Objects.requireNonNull(vectorClocks);
        for (Map.Entry<String, Long> entry : vectorClocks.clocks.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            Long otherValue = clocks.getOrDefault(key, 0L);
            if (value < otherValue) {
                return false;
            }
        }
        return true;
    }

}
