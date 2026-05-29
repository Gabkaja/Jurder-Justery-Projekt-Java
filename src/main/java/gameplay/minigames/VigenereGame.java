package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VigenereGame extends Minigame {

    private final String key = "ABC";
    private String correctAnswer;
    private String ciphertext;
    private List<String> generatedOptions;
    private final Random random = new Random();

    public VigenereGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
        prepareDynamicCipher();
    }

    private void prepareDynamicCipher() {
        if (engine.getMurderCase() == null || engine.getMurderCase().getKiller() == null) {
            this.correctAnswer = "KAMIL";
        } else {
            this.correctAnswer = engine.getMurderCase().getKiller().toUpperCase()
                    .replace("Ł", "L").replace("Ó", "O").replace("Ś", "S")
                    .replace("Ą", "A").replace("Ę", "E").replace("Ć", "C")
                    .replace("Ź", "Z").replace("Ż", "Z").replace("Ń", "N");
        }

        StringBuilder cipherBuilder = new StringBuilder();
        for (int i = 0; i < correctAnswer.length(); i++) {
            char c = correctAnswer.charAt(i);
            if (Character.isLetter(c)) {
                int shift = key.charAt(i % key.length()) - 'A';
                char encryptedChar = (char) (((c - 'A' + shift) % 26) + 'A');
                cipherBuilder.append(encryptedChar);
            } else {
                cipherBuilder.append(c);
            }
        }
        this.ciphertext = cipherBuilder.toString();

        String fake1 = alterLetters(correctAnswer);
        String fake2 = alterLetters(correctAnswer);

        this.generatedOptions = new ArrayList<>();
        int layout = random.nextInt(3);
        if (layout == 0) {
            generatedOptions.add("Hasło to: " + correctAnswer);
            generatedOptions.add("Hasło to: " + fake1);
            generatedOptions.add("Hasło to: " + fake2);
        } else if (layout == 1) {
            generatedOptions.add("Hasło to: " + fake1);
            generatedOptions.add("Hasło to: " + correctAnswer);
            generatedOptions.add("Hasło to: " + fake2);
        } else {
            generatedOptions.add("Hasło to: " + fake1);
            generatedOptions.add("Hasło to: " + fake2);
            generatedOptions.add("Hasło to: " + correctAnswer);
        }

        generatedOptions.add("Zrezygnuj z próby włamania");
    }

    private String alterLetters(String original) {
        char[] chars = original.toCharArray();
        int mutations = 0;
        int safetyCounter = 0;

        while (mutations < Math.max(1, original.length() / 4) && safetyCounter < 50) {
            safetyCounter++;
            int idx = random.nextInt(chars.length);
            if (Character.isLetter(chars[idx])) {
                char oldChar = chars[idx];
                char newChar = (char) ('A' + random.nextInt(26));
                if (oldChar != newChar) {
                    chars[idx] = newChar;
                    mutations++;
                }
            }
        }
        return new String(chars);
    }

    @Override
    public String getTitle() {
        return "MINIGRA: DESZYFRACJA VIGENÈRE'A (EKSPERT)";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "==================================================\n"
                    + "  SYSTEM AKCEPTUJE HASŁO!\n"
                    + "==================================================\n"
                    + "Zabezpieczenia laptopa pękły. Przeszukujesz prywatną korespondencję i pliki ofiary.\n"
                    + "Trafiasz na katalog, który rzuca światło bezpośrednio na sprawcę zbrodni!"
                    : "==================================================\n"
                    + "  BŁĄD LOGOWANIA! BLOKADA SYSTEMU\n"
                    + "==================================================\n"
                    + "Czerwony komunikat ostrzega o zablokowaniu dostępu.\n"
                    + "Zły algorytm deszyfracji uszkodził strukturę plików, bezpowrotnie niszcząc ten trop...";
        }

        return "W gabinecie leży włączony laptop ofiary. System żąda hasła deszyfrującego.\n"
                + "Obok na żółtej kartce post-it ktoś zapisał zakodowaną wiadomość: \"" + ciphertext + "\"\n"
                + "oraz wskazówkę: \"Klucz do siatki to zawsze ABC. Przesuń litery w przód (A=0, B=1, C=2...)\".\n\n"
                + "Musisz złamać ten szyfr polialfabetyczny dla słowa o długości " + correctAnswer.length() + " liter.\n"
                + "Pamiętaj, że litery klucza (ABC) zapętlają się w kółko.\n"
                + "Jak brzmi oryginalne, rozszyfrowane hasło?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) {
            returnScene.markRoomAsDone(engine.getCurrentLocation().getId());
            return List.of("Kontynuuj i wróć do pokoju");
        }
        return generatedOptions;
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            if (won) {
                String dynamicClue = "DOWÓD Z LAPTOPA: Wiadomości e-mail potwierdzają, że " + correctAnswer + " planował spotkanie z ofiarą w celu wyrównania rachunków.";
                engine.getEventLog().addClue(dynamicClue);
                returnScene.setMinigameResult(true, "Sukces! Złamano hasło do laptopa.");
            } else {
                returnScene.setMinigameResult(false, "Nie udało się rozszyfrować zawartości komputera - hasło błędne.");
            }
            return returnScene;
        }

        int exitIndex = generatedOptions.size();

        if (choice < 1 || choice > exitIndex) {
            return this;
        }

        if (choice == exitIndex) {
            return returnScene;
        }

        String selectedOptionText = generatedOptions.get(choice - 1);

        if (selectedOptionText.endsWith(": " + correctAnswer)) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}