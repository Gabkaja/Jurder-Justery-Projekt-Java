package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;

public class RiddleGame extends Minigame {

    private String selectedAnswer = null;

    public RiddleGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Zagadka Manuskryptu (Trudna)";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Twoje oczy rozszerzają się ze zdumienia. Układ faktów pasuje idealnie!\n"
                    + "Podwójne dno w biurku odblokowuje się z głośnym trzaskiem."
                    : "Wpisałeś złe nazwisko. Prastary zamek mechaniczny zazębia się na stałe.\n"
                    + "Słyszałeś opowieści o tych mechanizmach – bez ślusarza już tego nie otworzysz...";
        }

        return "Przeszukując zakurzone regały Biblioteki, trafiasz na stary, skórzany notes.\n"
                + "Ktoś opisał w nim spotkanie trzech osób przy okrągłym stole, ale brakuje nazwisk.\n"
                + "Musisz wydedukować, kto siedział na **Miejscu Numer 2**, by otworzyć skrytkę:\n\n"
                + "[Zapiski z notesu]:\n"
                + "1. Kamil siedział bezpośrednio po lewej stronie osoby pijącej czerwone wino.\n"
                + "2. Osoba na Miejscu 1 paliła drogie cygara.\n"
                + "3. Diana siedziała na Miejscu 3, tuż obok osoby, która piła wyłącznie whisky.\n"
                + "4. Lokaj podał koniak osobie, która nie paliła cygar ani nie miała na imię Diana.\n\n"
                + "Pytanie: Kto siedział na Miejscu Numer 2?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Odbierz nagrodę i wróć do pokoju");

        return List.of("Na Miejscu 2 siedział: KAMIL", "Na Miejscu 2 siedziała: DIANA", "Na Miejscu 2 siedział: MATEUSZ", "Zrezygnuj z zagadki");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            return endMinigame("Rozwiązałeś zagadkę notesu! Ukryty dokument ujawnia:", "Zamek pozostał zamknięty.", "MOTIVE");
        }

        if (choice == 4) return returnScene;

        if (choice == 3) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}