package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerminalUITest {

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

    private String output() {
        return captured.toString();
    }

    // --- showLocationScreen ---

    @Test
    void showLocationScreen_outputContainsLocationName() {
        TerminalUI.showLocationScreen("Gabinet", "Opis gabinetu.", List.of("Opcja 1"));
        assertTrue(output().toUpperCase().contains("GABINET"));
    }

    @Test
    void showLocationScreen_outputContainsDescription() {
        TerminalUI.showLocationScreen("Loc", "Klimatyczny opis.", List.of("A"));
        assertTrue(output().contains("Klimatyczny opis."));
    }

    @Test
    void showLocationScreen_outputContainsOptions() {
        TerminalUI.showLocationScreen("L", "D", List.of("Idz dalej", "Wróc"));
        assertTrue(output().contains("Idz dalej"));
        assertTrue(output().contains("Wróc"));
    }

    @Test
    void showLocationScreen_emptyDescription_doesNotThrow() {
        assertDoesNotThrow(() -> TerminalUI.showLocationScreen("L", "", List.of("X")));
    }

    @Test
    void showLocationScreen_emptyOptions_doesNotThrow() {
        assertDoesNotThrow(() -> TerminalUI.showLocationScreen("L", "D", List.of()));
    }

    @Test
    void showLocationScreen_outputIsNotEmpty() {
        TerminalUI.showLocationScreen("X", "Y", List.of("Z"));
        assertFalse(output().isBlank());
    }

    // --- showNotification ---

    @Test
    void showNotification_outputContainsMessage() {
        TerminalUI.showNotification("Znalazłeś dowód!");
        assertTrue(output().contains("Znalazłeś dowód!"));
    }

    @Test
    void showNotification_outputContainsExclamationMarks() {
        TerminalUI.showNotification("Uwaga");
        assertTrue(output().contains("!!!"));
    }

    @Test
    void showNotification_emptyMessage_doesNotThrow() {
        assertDoesNotThrow(() -> TerminalUI.showNotification(""));
    }

    @Test
    void showNotification_outputIsNotEmpty() {
        TerminalUI.showNotification("Test");
        assertFalse(output().isBlank());
    }
}
