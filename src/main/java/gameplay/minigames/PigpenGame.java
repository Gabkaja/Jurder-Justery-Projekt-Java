package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;
import java.util.Random;

public class PigpenGame extends Minigame {

    private final String symbols;
    private final String solution;

    public PigpenGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);

        int pool = new Random().nextInt(3);
        if (pool == 0) {
            this.symbols = "[_|] [.] [| ]";
            this.solution = "KAT";
        } else if (pool == 1) {
            this.symbols = "[_] [| ] [] [^]";
            this.solution = "TRUP";
        } else {
            this.symbols = "[.] [-_] [| ]";
            this.solution = "AKT";
        }
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Szyfr Wolnomularski (Ekspert)";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Słyszysz suche kliknięcie zapadki. Kamienna płyta w ścianie przesuwa się,\n"
                    + "odsłaniając tajną skrytkę z dokumentami rodowymi!"
                    : "Wprowadziłeś złe hasło runiczne. Z sufitu sypie się kurz, a mechanizm\n"
                    + "blokuje się stalowymi sztabami. Kod został dezaktywowany.";
        }

        return "Schodzisz do mrocznej strefy lokacji: " + engine.getCurrentLocation().getName() + ".\n"
                + "Na starej, kamiennej płaskorzeźbie dostrzegasz wyryte symbole starego szyfru Pigpen.\n"
                + "Obok widzisz legendę wykutą w skale:\n"
                + "  [_|] = K,   [.] = A,   [| ] = T\n"
                + "  [_] = R,    [] = U,    [^] = P\n"
                + "  [-_] = Krok awaryjny (zamienia się w K lub A zależnie od kontekstu)\n\n"
                + "Wiadomość na ścianie to: " + symbols + "\n"
                + "Musisz ułożyć litery w odpowiednie słowo. Jaki jest wynik?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Przeszukaj otwartą skrytkę");
        return List.of("Słowo to: KAT", "Słowo to: AKT", "Słowo to: TRUP", "Odejdź od płaskorzeźby");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            return endMinigame("Skrytka wolnomularska skrywała mroczną tajemnicę:", "Mechanizm zatrzasnął się na amen.", "MOTIVE");
        }

        if (choice == 4) return returnScene;

        String playerAnswer = switch (choice) {
            case 1 -> "KAT";
            case 2 -> "AKT";
            case 3 -> "TRUP";
            default -> "";
        };

        if (playerAnswer.equals(this.solution)) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}