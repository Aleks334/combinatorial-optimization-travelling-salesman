package org.example.ui;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleUITest {

    @Test
    public void shouldCapturePrintedLogs() {
        // given
        ConsoleUI ui = new ConsoleUI();

        // when
        ui.print("Hello");
        ui.println("World");
        ui.printf("Number: %d", 5);

        // then
        String logs = ui.getLogs();
        assertThat(logs).contains("Hello");
        assertThat(logs).contains("World");
        assertThat(logs).contains("Number: 5");
    }
}

