package org.example.ui.console;

import org.example.application.port.out.OutputPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {
    private final String title;
    private final List<MenuItem> items;
    private final OutputPort output;

    private Menu(Builder builder) {
        this.title = builder.title;
        this.items = new ArrayList<>(builder.items);
        this.output = builder.output;
    }

    public static Builder builder(String title) {
        return new Builder(title);
    }

    public void display() {
        int maxLabelLength = 38;
        String border = "+" + "-".repeat(42) + "+";

        output.println("\n" + border);
        output.printf("| %-40s |%n", title);
        output.println(border);

        for (int i = 0; i < items.size(); i++) {
            String label = items.get(i).getLabel();
            if (label.length() > maxLabelLength) {
                label = label.substring(0, maxLabelLength);
            }
            output.printf("| %d. %-38s |%n", i + 1, label);
        }

        output.println(border);
    }

    public void handleChoice(int choice) {
        if (choice < 1 || choice > items.size()) {
            throw new IllegalArgumentException("Invalid choice: " + choice);
        }
        items.get(choice - 1).execute();
    }

    public static class Builder {
        private final String title;
        private final List<MenuItem> items;
        private OutputPort output;

        private Builder(String title) {
            this.title = title;
            this.items = new ArrayList<>();
        }

        public Builder addItem(String label, Runnable action) {
            items.add(new MenuItem(label, action));
            return this;
        }

        public Builder setOutput(OutputPort output) {
            this.output = output;
            return this;
        }

        public Menu build() {
            if (items.isEmpty()) {
                throw new IllegalStateException("Menu must have at least one item");
            }
            Objects.requireNonNull(output, "OutputPort must be set");
            return new Menu(this);
        }
    }
}