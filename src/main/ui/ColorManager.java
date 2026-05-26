package ui;

public class ColorManager {
    // Standardowe kolory tekstowe ANSI
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // Pogrubione wersje kolorów do nagłówków i ważnych dowodów
    public static final String BOLD_RED = "\033[1;31m";
    public static final String BOLD_GREEN = "\033[1;32m";
    public static final String BOLD_YELLOW = "\033[1;33m";

    // Otacza tekst kodami koloru, aby terminal wyświetlił go barwnie.
    public static String colorize(String text, String color) {
        return color + text + RESET;
    }
}