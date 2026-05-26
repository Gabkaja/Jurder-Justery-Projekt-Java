package ui;

public class ScreenCleaner {
    // Czyści konsolę terminala i resetuje pozycję kursora.
    public static void clear() {
        // Sekwencja ANSI: \033[H (kursor na górę), \033[2J (wyczyść ekran)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}