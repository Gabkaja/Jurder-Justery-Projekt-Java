package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;

public class CipherGame extends Minigame {

    private final String encryptedText = "IMAK";

    public CipherGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Ślad na Lustrze";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Przecierasz delikatnie krawędź szkła. Układ liter układa się w logiczną całość.\n"
                    + "Ktoś bardzo chciał, żeby ten ślad został odkryty – albo wręcz przeciwnie."
                    : "Rękaw Twojego płaszcza zbyt mocno rozmazał parę wodną. \n"
                    + "Napis zamienia się w bezkształtną plamę wilgoci. Ślad zniknął na zawsze...";
        }

        return "Wchodzisz do łazienki. W powietrzu wciąż unosi się gęsta, gorąca para z kranu.\n"
                + "Ktokolwiek tu był przed Tobą, brał gorącą kąpiel... albo próbował zmyć z siebie coś potwornego.\n"
                + "Nagle dostrzegasz, że na zaparowanej tafli wielkiego lustra ktoś drżącym palcem\n"
                + "nabazgrał pośpiesznie cztery litery: \"" + encryptedText + "\".\n"
                + "Krople wody powoli spływają w dół, zacierając krawędzie pisma. Musisz działać szybko.\n"
                + "To wygląda na klasyczny anagram. Co autor miał na myśli?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Powrót do oględzin łazienki");
        return List.of("To anagram imienia: LIMA", "To anagram imienia: KAMIL", "To anagram imienia: " + engine.getMurderCase().getKiller(), "Zignoruj napis, nim spłynie");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            return endMinigame("Rozszyfrowałeś intencje uciekiniera! Zabezpieczony dowód to:", "Wiadomość z lustra spłynęła wraz z parą wodną.", "KILLER");
        }

        if (choice == 4) {
            return returnScene;
        }

        if (choice == 2 || (choice == 3 && "KAMIL".equalsIgnoreCase(engine.getMurderCase().getKiller()))) {
            won = true;
        }
        gameOver = true;
        return this;
    }
}