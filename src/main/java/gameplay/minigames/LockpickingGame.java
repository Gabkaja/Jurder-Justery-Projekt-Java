package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.Random;
import java.util.List;

public class LockpickingGame extends Minigame {

    private final int targetPin;
    private int playerGuess = 0;

    public LockpickingGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
        this.targetPin = new Random().nextInt(5) + 1;
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Wytrych (Zręczność i Wyczucie)";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "KLIK! Ostatnia zapadka puszcza. Zamek w starej szufladzie biurka został otwarty!\n"
                    + "W środku nie ma dokumentów o morderstwie, ale znajdujesz zapasowe baterie do latarki i uniwersalny klucz."
                    : "TRZASK! Zbyt mocno docisnąłeś napinacz. Wytrych łamie się wewnątrz mechanizmu,\n"
                    + "blokując zamek na amen. Nic tu po Tobie.";
        }

        return "Próbujesz otworzyć zamkniętą szufladę w lokacji: " + engine.getCurrentLocation().getName() + ".\n"
                + "Wsuwasz wytrych i powoli podnosisz zapadki. Musisz wyczuć odpowiedni moment docisku.\n"
                + "Instynkt podpowiada Ci, że musisz wybrać właściwy stopień nacisku (od 1 do 5).\n"
                + "Twój aktualny wybór: " + (playerGuess == 0 ? "brak" : playerGuess);
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Zabierz łup i odejdź");
        return List.of("Nacisk poziomu 1 (Delikatny)", "Nacisk poziomu 3 (Średni)", "Nacisk poziomu 5 (Mocny)");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            if (won) {
                engine.getEventLog().addClue("ZYSKANO: Uniwersalny klucz do dworku (pozwala pomijać niektóre blokady drzwi).");
                returnScene.setMinigameResult(true, "Otworzyłeś skrytkę z zaopatrzeniem!");
            } else {
                returnScene.setMinigameResult(false, "Zamek zablokowany.");
            }
            returnScene.markRoomAsDone(engine.getCurrentLocation().getId());
            return returnScene;
        }

        int weight = switch (choice) {
            case 1 -> 1;
            case 2 -> 3;
            case 3 -> 5;
            default -> 0;
        };

        if (Math.abs(weight - targetPin) <= 1) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}