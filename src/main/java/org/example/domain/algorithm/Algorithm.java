package org.example.domain.algorithm;

public enum Algorithm {
    GREEDY("Greedy Algorithm"),
    ANT_COLONY("Ant Colony Optimization");

    private final String displayName;

    Algorithm(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Algorithm fromChoice(int choice) {
        return switch (choice) {
            case 1 -> GREEDY;
            case 2 -> ANT_COLONY;
            default -> ANT_COLONY;
        };
    }
}

