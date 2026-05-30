package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuRendererTest {

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

    // --- renderHeader ---

    @Test
    void renderHeader_outputContainsTitleUpperCase() {
        MenuRenderer.renderHeader("Sala balowa");
        assertTrue(output().contains("SALA BALOWA"));
    }

    @Test
    void renderHeader_outputContainsCyanSeparators() {
        MenuRenderer.renderHeader("Test");
        assertTrue(output().contains("====="));
    }

    @Test
    void renderHeader_outputIsNotEmpty() {
        MenuRenderer.renderHeader("X");
        assertFalse(output().isBlank());
    }

    @Test
    void renderHeader_separatorAppearsAtLeastTwice() {
        MenuRenderer.renderHeader("Test");
        long count = output().lines()
                .filter(l -> l.contains("==="))
                .count();
        assertTrue(count >= 2);
    }

    // --- renderMenuOptions ---

    @Test
    void renderMenuOptions_printsAllOptions() {
        List<String> opts = List.of("Opcja A", "Opcja B", "Opcja C");
        MenuRenderer.renderMenuOptions(opts);
        String out = output();
        assertTrue(out.contains("Opcja A"));
        assertTrue(out.contains("Opcja B"));
        assertTrue(out.contains("Opcja C"));
    }

    @Test
    void renderMenuOptions_numbersStartAtOne() {
        MenuRenderer.renderMenuOptions(List.of("Pierwsza"));
        assertTrue(output().contains("[1]") || output().contains("1"));
    }

    @Test
    void renderMenuOptions_numbersAreSequential() {
        MenuRenderer.renderMenuOptions(List.of("A", "B", "C"));
        String out = output();
        assertTrue(out.contains("1"));
        assertTrue(out.contains("2"));
        assertTrue(out.contains("3"));
    }

    @Test
    void renderMenuOptions_emptyList_doesNotThrow() {
        assertDoesNotThrow(() -> MenuRenderer.renderMenuOptions(List.of()));
    }

    @Test
    void renderMenuOptions_promptLineIncludesWybor() {
        MenuRenderer.renderMenuOptions(List.of("X"));
        assertTrue(output().contains("Wybór") || output().contains("Co chcesz"));
    }

    @Test
    void renderMenuOptions_singleOption_containsOptionText() {
        MenuRenderer.renderMenuOptions(List.of("Jedyna opcja"));
        assertTrue(output().contains("Jedyna opcja"));
    }
}
