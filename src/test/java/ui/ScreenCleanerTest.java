package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ScreenCleanerTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void captureOut() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    void restoreOut() {
        System.setOut(originalOut);
    }

    @Test
    void clear_doesNotThrow() {
        assertDoesNotThrow(ScreenCleaner::clear);
    }

    @Test
    void clear_writesAnsiEscapeSequence() {
        ScreenCleaner.clear();
        String out = captured.toString();
        // Sekwencja ANSI \033[H\033[2J
        assertTrue(out.contains("\033[H") || out.contains("\033[2J"));
    }

    @Test
    void clear_calledTwice_doesNotThrow() {
        assertDoesNotThrow(() -> {
            ScreenCleaner.clear();
            ScreenCleaner.clear();
        });
    }
}
