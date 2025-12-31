package org.example.ui;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String title;
    private final List<MenuItem> items;

    private Menu(Builder builder) {
        this.title = builder.title;
        this.items = new ArrayList<>(builder.items);
    }

    public static Builder builder(String title) {
        return new Builder(title);
    }

    public void display() {
        int maxLabelLength = 38;

        System.out.println("\n+" + "-".repeat(42) + "+");
        System.out.printf("| %-40s |%n", title);
        System.out.println("+" + "-".repeat(42) + "+");

        for (int i = 0; i < items.size(); i++) {
            String label = items.get(i).getLabel();
            if (label.length() > maxLabelLength) {
                label = label.substring(0, maxLabelLength);
            }
            System.out.printf("| %d. %-38s |%n", i + 1, label);
        }

        System.out.println("+" + "-".repeat(42) + "+");
    }

    public void handleChoice(int choice) throws InvalidMenuItemChoiceException {
        if (choice < 1 || choice > items.size()) {
            throw new InvalidMenuItemChoiceException("Invalid menu item choice: " + choice);
        }

        items.get(choice - 1).execute();
    }

    public int size() {
        return items.size();
    }

    public static class Builder {
        private final String title;
        private final List<MenuItem> items;

        private Builder(String title) {
            this.title = title;
            this.items = new ArrayList<>();
        }

        public Builder addItem(String label, Runnable action) {
            items.add(new MenuItem(label, action));
            return this;
        }

        public Menu build() {
            if (items.isEmpty()) {
                throw new IllegalStateException("Menu must have at least one item");
            }
            return new Menu(this);
        }
    }
}
