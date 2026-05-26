package ui;

import java.util.List;

public class MenuRenderer {

    // Rysuje ozdobny nagłówek lokacji lub sytuacji.
    public static void renderHeader(String title) {
        String coloredTitle = ColorManager.colorize(title.toUpperCase(), ColorManager.BOLD_YELLOW);
        System.out.println(ColorManager.colorize("==================================================", ColorManager.CYAN));
        System.out.println("  " + coloredTitle);
        System.out.println(ColorManager.colorize("==================================================", ColorManager.CYAN));
    }

    // Wyświetla listę opcji wyboru dla gracza.
    public static void renderMenuOptions(List<String> options) {
        System.out.println("\nCo chcesz zrobić?");
        for (int i = 0; i < options.size(); i++) {
            String optionNumber = ColorManager.colorize("[" + (i + 1) + "]", ColorManager.GREEN);
            System.out.println(optionNumber + " " + options.get(i));
        }
        System.out.print(ColorManager.colorize("\nWybór: ", ColorManager.BOLD_GREEN));
    }
}