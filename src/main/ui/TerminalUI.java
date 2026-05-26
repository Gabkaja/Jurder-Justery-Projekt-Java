package ui;

import java.util.List;

public class TerminalUI {

    /**
     * Wyświetla pełny ekran lokacji: czyści stary widok, pokazuje gdzie gracz jest,
     * wypisuje klimatyczny opis i listę akcji.
     */
    public static void showLocationScreen(String locationName, String description, List<String> options) {
        // Czyszczenie ekranu przed nową sceną
        ScreenCleaner.clear();

        // Nagłówek z nazwą pokoju
        MenuRenderer.renderHeader(locationName);

        // Opis otoczenia
        System.out.println("\n" + description);

        // Menu wyboru dedykowane dla tej sytuacji
        MenuRenderer.renderMenuOptions(options);
    }

    // Pomocnicza metoda do wyświetlania pilnych komunikatów (np. znalezienie dowodu).
    public static void showNotification(String message) {
        System.out.println("\n" + ColorManager.colorize("!!! " + message + " !!!", ColorManager.BOLD_RED));
    }
}