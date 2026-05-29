package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;

public class BoobyTrapGame extends Minigame {

    public BoobyTrapGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
    }

    @Override
    public String getTitle() {
        return "MINIGRA: NIEBEZPIECZEŃSTWO! Pułapka naciągowa";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Uff! Przecinasz właściwą linkę z idealnym wyczuciem.\n"
                    + "Ciężki mechanizm blokuje się, a zagrożenie zostaje zneutralizowane. Możesz bezpiecznie rozejrzeć się po pokoju."
                    : "TRZASK! Za mocno pociągnąłeś za drut! Z sufitu z impaktem spada metalowa konstrukcja,\n"
                    + "raniąc Cię w ramię. Tracisz cenny czas na opatrzenie rany, a hałas na pewno kogoś zaalarmował!";
        }

        return "Wchodzisz głębiej do lokacji: " + engine.getCurrentLocation().getName() + ".\n"
                + "Nagle Twoja noga natrafia na ledwo widoczną, stalową linkę rozciągniętą tuż nad podłogą.\n"
                + "Naciąg już drgnął. Słyszysz ciche tykanie zapadki ukrytej w ścianie.\n"
                + "Musisz zareagować w ułamku sekundy! Co robisz?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Odetchnij i wróć do eksploracji");
        return List.of(
                "Padnij natychmiast na podłogę (Zminimalizuj profil ciała)",
                "Gwałtownie odskocz do tyłu (Próba ucieczki ze strefy rażenia)",
                "Złap linkę dłonią i spróbuj utrzymać napięcie mechanizmu"
        );
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            if (!won) {
                engine.getEventLog().addClue("ZDARZENIE: Zostałeś ranny w pułapce. Eksploracja staje się trudniejsza.");
            }
            returnScene.markRoomAsDone(engine.getCurrentLocation().getId());
            return returnScene;
        }

        if (choice == 1) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}