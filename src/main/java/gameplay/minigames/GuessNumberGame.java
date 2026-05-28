package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;
import java.util.Random;

public class GuessNumberGame extends Minigame {

    private final int targetNumber;
    private int attemptsLeft;
    private String hint = "Mechanizm stawia opór. Wprowadź pierwszą kombinację (1-20).";

    public GuessNumberGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
        this.targetNumber = new Random().nextInt(20) + 1;

        this.attemptsLeft = switch (engine.getDifficulty()) {
            case EASY -> 6;
            case MEDIUM -> 4;
            case HARD -> 3;
            case VERY_HARD -> 2;
        };
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Złamanie Szyfru Kasetki";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Ciche, satysfakcjonujące KLIKNIĘCIE rozchodzi się po cichym pomieszczeniu.\n"
                    + "Ciężkie wieko odskakuje, odsłaniając dokumenty oblepione kurzem. Udało się!"
                    : "Z wnętrza obudowy dobiega przeciągły, metaliczny zgrzyt, a cyfrowy wyświetlacz gaśnie.\n"
                    + "System bezpieczeństwa trwale zablokował rygiel. Szansa przepadła bezpowrotnie...";
        }

        return "Stoisz w głębokim cieniu pomieszczenia: " + engine.getCurrentLocation().getName() + ".\n"
                + "Twoje palce spoczywają na chłodnej, stalowej kasetce ukrytej w bocznej szufladzie.\n"
                + "Gdzieś z korytarza dobiega tykanie starego zegara, pot spływa Ci po skroni.\n"
                + "Wiesz, że błędny ruch odetnie Cię od prawdy. Zamek elektroniczny cicho buczy.\n\n"
                + "Pozostałe próby: " + attemptsLeft + "\n"
                + "Status zapadek: " + hint;
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Zakończ minigrę i zbierz ślady");
        return List.of("Wbij kod: 5", "Wbij kod: 10", "Wbij kod: 15", "Wbij kod: " + targetNumber, "Odejdź od kasetki");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            return endMinigame("Udało Ci się sforsować zamek! W środku znajdujesz:", "Niestety, zamek został zablokowany na amen.");
        }

        if (choice == 5) {
            return returnScene;
        }

        int[] guessedValues = {5, 10, 15, targetNumber};
        int guess = guessedValues[Math.min(choice - 1, 3)];
        attemptsLeft--;

        if (guess == targetNumber) {
            won = true;
            gameOver = true;
        } else if (attemptsLeft <= 0) {
            gameOver = true;
        } else {
            hint = (guess < targetNumber) ? "Ciche piknięcie... Wprowadzona liczba jest ZA MAŁA." : "Ciche piknięcie... Wprowadzona liczba jest ZA DUŻA.";
        }

        return this;
    }
}