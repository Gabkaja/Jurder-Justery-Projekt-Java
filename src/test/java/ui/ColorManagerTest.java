package ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorManagerTest {

    // --- Stale ANSI ---

    @Test
    void reset_startsWithEscape() {
        assertTrue(ColorManager.RESET.startsWith("\033["));
    }

    @Test
    void red_containsAnsiCode() {
        assertTrue(ColorManager.RED.contains("31m"));
    }

    @Test
    void green_containsAnsiCode() {
        assertTrue(ColorManager.GREEN.contains("32m"));
    }

    @Test
    void yellow_containsAnsiCode() {
        assertTrue(ColorManager.YELLOW.contains("33m"));
    }

    @Test
    void blue_containsAnsiCode() {
        assertTrue(ColorManager.BLUE.contains("34m"));
    }

    @Test
    void cyan_containsAnsiCode() {
        assertTrue(ColorManager.CYAN.contains("36m"));
    }

    @Test
    void boldRed_containsBoldAndRedCode() {
        assertTrue(ColorManager.BOLD_RED.contains("1;31m"));
    }

    @Test
    void boldGreen_containsBoldAndGreenCode() {
        assertTrue(ColorManager.BOLD_GREEN.contains("1;32m"));
    }

    @Test
    void boldYellow_containsBoldAndYellowCode() {
        assertTrue(ColorManager.BOLD_YELLOW.contains("1;33m"));
    }

    // --- colorize ---

    @Test
    void colorize_wrapsTextWithColorAndReset() {
        String result = ColorManager.colorize("hello", ColorManager.RED);
        assertTrue(result.startsWith(ColorManager.RED));
        assertTrue(result.endsWith(ColorManager.RESET));
        assertTrue(result.contains("hello"));
    }

    @Test
    void colorize_emptyText_stillWrapsWithColorAndReset() {
        String result = ColorManager.colorize("", ColorManager.GREEN);
        assertEquals(ColorManager.GREEN + ColorManager.RESET, result);
    }

    @Test
    void colorize_textPreservedBetweenCodes() {
        String text = "Ważna poszlaka!";
        String result = ColorManager.colorize(text, ColorManager.BOLD_RED);
        assertTrue(result.contains(text));
    }

    @Test
    void colorize_differentColors_produceDifferentResults() {
        String r1 = ColorManager.colorize("test", ColorManager.RED);
        String r2 = ColorManager.colorize("test", ColorManager.GREEN);
        assertNotEquals(r1, r2);
    }

    @Test
    void colorize_resultLongerThanOriginalText() {
        String text = "abc";
        String result = ColorManager.colorize(text, ColorManager.CYAN);
        assertTrue(result.length() > text.length());
    }
}
