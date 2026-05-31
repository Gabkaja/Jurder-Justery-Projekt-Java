package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class BoobyTrapGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;
    private BoobyTrapGame game;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
        game = new BoobyTrapGame(engine, returnScene);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        assertTrue(game.getTitle().contains("Pułapka"));
    }

    @Test
    void initialGameOver_isFalse() {
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    @Test
    void getOptions_notGameOver_returnsThreeOptions() {
        assertEquals(3, game.getOptions().size());
    }

    // --- Wybór 1 (padnij – wygrana) ---

    @Test
    void onChoice1_setsWonTrue() {
        game.onChoice(1);
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice1_setsGameOver() {
        game.onChoice(1);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    @Test
    void onChoice1_returnsSelf() {
        assertSame(game, game.onChoice(1));
    }

    // --- Wybór 2 (odskok – przegrana) ---

    @Test
    void onChoice2_setsWonFalse() {
        game.onChoice(2);
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Wybór 3 (złap linkę – przegrana) ---

    @Test
    void onChoice3_setsWonFalse() {
        game.onChoice(3);
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Po zakończeniu gry ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        game.onChoice(1);
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void afterLoss_confirmationChoice_addsClueAboutInjury() {
        game.onChoice(2); // przegrana
        game.onChoice(1); // potwierdzenie
        boolean hasClue = engine.getEventLog().getClues().stream()
                .anyMatch(c -> c.contains("ranny"));
        assertTrue(hasClue);
    }

    @Test
    void afterWin_confirmationChoice_doesNotAddInjuryClue() {
        game.onChoice(1); // wygrana
        game.onChoice(1); // potwierdzenie
        boolean hasInjury = engine.getEventLog().getClues().stream()
                .anyMatch(c -> c.contains("ranny"));
        assertFalse(hasInjury);
    }

    @Test
    void afterWin_confirmationChoice_returnsReturnScene() {
        game.onChoice(1);
        SceneManager result = game.onChoice(1);
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsSuccessText() {
        game.onChoice(1);
        assertTrue(game.getNarration().contains("Uff"));
    }

    @Test
    void narration_afterLoss_containsFailText() {
        game.onChoice(2);
        assertTrue(game.getNarration().contains("TRZASK"));
    }
}
