package main.engine;

import java.util.Scanner;

/**
 * Obsługuje wejście użytkownika z klawiatury.
 * Waliduje dane wejściowe zanim trafi do logiki gry.
 */
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Czyta liczbę całkowitą z zakresu [min, max].
     * Powtarza pytanie dopóki użytkownik nie poda poprawnej wartości.
     */
    public int readInt(int min, int max) {
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Podaj liczbę od " + min + " do " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Niepoprawny znak. Podaj numer opcji.");
            }
        }
    }

    // Czyta dowolną linię tekstu (np. do wyszukiwania, oskarżenia).
    public String readLine() {
        return scanner.nextLine().trim();
    }

    // Czeka na dowolny klawisz (Enter) – używane przy komunikatach "naciśnij Enter".
    public void waitForEnter() {
        scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}