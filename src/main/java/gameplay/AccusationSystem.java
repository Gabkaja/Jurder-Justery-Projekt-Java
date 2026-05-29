package gameplay;

import characters.Suspect;
import engine.GameEngine;
import engine.InputHandler;
import ui.ColorManager;
import ui.MenuRenderer;
import ui.ScreenCleaner;
import world.Location;
import world.MurderCase;
import world.WeaponData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AccusationSystem {

    private final GameEngine engine;
    private final InputHandler inputHandler;

    public AccusationSystem(GameEngine engine) {
        this.engine = engine;
        this.inputHandler = new InputHandler();
    }

    public void makeAccusation() {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Finałowe Oskarżenie");
        System.out.println("Komendant naciska, prasa czeka pod drzwiami. To ten moment.");
        System.out.println(ColorManager.colorize("Pamiętaj: masz tylko jedną szansę. Jeśli się pomylisz, gra się kończy!", ColorManager.BOLD_RED));
        System.out.println("\nCzy na pewno chcesz wysunąć oskarżenie już teraz?");

        List<String> confirmOptions = List.of("Tak, jestem gotów.", "Nie, muszę jeszcze poszukać dowodów.");
        MenuRenderer.renderMenuOptions(confirmOptions);

        int choice = inputHandler.readInt(1, 2);
        if (choice == 2) {
            return; // Powrót do gry
        }

        String guessedKiller = askWho();
        String guessedLocation = askWhere();
        String guessedWeapon = askWhat();
        String guessedMotive = askWhy();

        verifyAccusation(guessedKiller, guessedLocation, guessedWeapon, guessedMotive);
    }

    private String askWho() {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Krok 1: Kto zabił?");
        List<String> options = new ArrayList<>();

        for (Suspect s : engine.getSuspects()) {
            options.add(s.getName());
        }

        MenuRenderer.renderMenuOptions(options);
        int choice = inputHandler.readInt(1, options.size());
        return options.get(choice - 1);
    }

    private String askWhere() {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Krok 2: Gdzie popełniono zbrodnię?");
        List<String> options = new ArrayList<>();

        for (Location loc : engine.getLocations()) {
            options.add(loc.getName());
        }

        MenuRenderer.renderMenuOptions(options);
        int choice = inputHandler.readInt(1, options.size());
        return options.get(choice - 1);
    }

    private String askWhat() {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Krok 3: Czym zamordowano ofiarę?");

        List<String> options = new ArrayList<>();
        WeaponData weaponData = engine.getWeaponData();

        // Wyciągamy wszystkie unikalne bronie z podziału na typy
        if (weaponData != null && weaponData.getByType() != null) {
            for (var group : weaponData.getByType()) {
                for (String weapon : group.getWeapons()) {
                    if (!options.contains(weapon)) {
                        options.add(weapon);
                    }
                }
            }
        }

        // Sortujemy alfabetycznie, żeby graczowi było łatwiej szukać
        Collections.sort(options);

        MenuRenderer.renderMenuOptions(options);
        int choice = inputHandler.readInt(1, options.size());
        return options.get(choice - 1);
    }

    private String askWhy() {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Krok 4: Jaki był motyw?");

        List<String> options = List.of(
                "Dziedziczenie",
                "Zemsta",
                "Szantaż",
                "Zazdrość",
                "Ambicja",
                "Ochrona tajemnicy"
        );

        MenuRenderer.renderMenuOptions(options);
        int choice = inputHandler.readInt(1, options.size());
        return options.get(choice - 1);
    }

    private void verifyAccusation(String killer, String location, String weapon, String motive) {
        ScreenCleaner.clear();
        MenuRenderer.renderHeader("Werdykt");

        MurderCase realCase = engine.getMurderCase();

        boolean isKillerCorrect = realCase.getKiller().equalsIgnoreCase(killer);
        boolean isLocationCorrect = realCase.getCrimeScene().getName().equalsIgnoreCase(location);
        boolean isWeaponCorrect = realCase.getWeapon().equalsIgnoreCase(weapon);

        boolean isMotiveCorrect = realCase.getMotive().getLabel().equalsIgnoreCase(motive);

        if (isKillerCorrect && isLocationCorrect && isWeaponCorrect && isMotiveCorrect) {
            handleWin();
        } else {
            handleLoss();
        }

        engine.stop();
    }

    private void handleWin() {
        System.out.println(ColorManager.colorize("\nBINGO! ROZWIĄZAŁEŚ SPRAWĘ!", ColorManager.BOLD_GREEN));
        System.out.println("Z satysfakcją patrzysz, jak sprawca zostaje wyprowadzony w kajdankach.");
        System.out.println("Krakowska elita może znów spać spokojnie (przynajmniej do następnego bankietu).");
        System.out.println("Prezydent miasta wręcza ci honorowy klucz do bram miasta, a komendant...");
        System.out.println("Cóż, komendant wciąż cię nie lubi, ale tym razem musi przyznać ci rację.");
        System.out.println("\nGratulacje, Detektywie!");
    }

    private void handleLoss() {
        System.out.println(ColorManager.colorize("\nKOMPLETNA KOMPROMITACJA...", ColorManager.BOLD_RED));

        String[] failureTexts = {
                "Pudło! Twój zmysł detektywistyczny kompletnie zawiódł. Oskarżyłeś niewinną osobę, a prawdziwy morderca właśnie pije\nspokojnie kawę na Kazimierzu. Oddaj odznakę i spróbuj swoich sił w sprzedawaniu obwarzanków na Rynku.",

                "Niestety, to nie takie proste! Komendant jest wściekły, a lokalne gazety mają używanie. Zamiast awansu czeka cię\nprzeniesienie do straży miejskiej. Podobno jest wakat przy odganianiu gołębi od Sukiennic.",

                "Twoja dedukcja to absolutna katastrofa. Pomyliłeś fakty, a morderca uciekł pierwszym pociągiem Pendolino do Warszawy.\nMoże z takim talentem powinieneś raczej zająć się wróżeniem z fusów w jakiejś piwnicznej kawiarni?"
        };

        Random random = new Random();
        System.out.println(failureTexts[random.nextInt(failureTexts.length)]);

        // Wypisywanie prawidłowego rozwiązania
        MurderCase realCase = engine.getMurderCase();
        System.out.println("\n--------------------------------------------------");
        System.out.println("PRAWDZIWE ROZWIĄZANIE SPRAWY:");
        System.out.println("* Morderca: " + realCase.getKiller());
        System.out.println("* Miejsce zbrodni: " + realCase.getCrimeScene().getName());
        System.out.println("* Narzędzie zbrodni: " + realCase.getWeapon());
        System.out.println("* Motyw: " + realCase.getMotive().getLabel());
        System.out.println("--------------------------------------------------");

        System.out.println("\n[ GAME OVER ]");
    }
}