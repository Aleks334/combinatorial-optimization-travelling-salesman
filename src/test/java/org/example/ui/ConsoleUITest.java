package org.example.ui;

import org.example.ui.console.ConsoleUI;
import org.example.ui.console.exception.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConsoleUITest {

    @Test
    void shouldGetStringValueProvidedByUser() throws Exception {
        // given
        InputStream input = new ByteArrayInputStream("hello console\n".getBytes());
        ConsoleUI ui = new ConsoleUI(input);

        // when
        String result = ui.getString("Enter text: ");

        // then
        assertThat(result).isEqualTo("hello console");
    }

    @Test
    void shouldGetIntegerValueWhenInputIsValid() throws Exception {
        // given
        InputStream input = new ByteArrayInputStream("123\n".getBytes());
        ConsoleUI ui = new ConsoleUI(input);

        // when
        int result = ui.getInt("Enter number: ");

        // then
        assertThat(result).isEqualTo(123);
    }

    @Test
    void shouldThrowInvalidInputExceptionWhenIntegerInputIsInvalid() {
        // given
        InputStream input = new ByteArrayInputStream("abc\n".getBytes());
        ConsoleUI ui = new ConsoleUI(input);

        // when & then
        assertThatThrownBy(() -> ui.getInt("Enter number: "))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("enter a valid integer");
    }

    @Test
    public void shouldInitializeWithEmptyLogs() {
        // given
        ConsoleUI ui = new ConsoleUI();

        // then
        String logs = ui.getLogs();
        assertThat(logs).isEmpty();
    }

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

