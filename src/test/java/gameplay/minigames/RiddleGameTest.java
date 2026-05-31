package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class RiddleGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;
    private RiddleGame game;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
        game = new RiddleGame(engine, returnScene);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        assertEquals("MINIGRA: Zagadka Manuskryptu (Trudna)", game.getTitle());
    }

    @Test
    void initialGameOver_isFalse() {
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    @Test
    void initialWon_isFalse() {
        assertFalse((Boolean) getField(game, "won"));
    }

    @Test
    void getNarration_containsRiddleText() {
        assertTrue(game.getNarration().contains("Kamil"));
    }

    @Test
    void getOptions_notGameOver_returnsFourOptions() {
        assertEquals(4, game.getOptions().size());
    }

    // --- Wybór 4 (rezygnacja) ---

    @Test
    void onChoice4_returnsReturnScene() {
        SceneManager result = game.onChoice(4);
        assertSame(returnScene, result);
    }

    @Test
    void onChoice4_doesNotSetGameOver() {
        game.onChoice(4);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    // --- Wybór 3 (MATEUSZ – poprawna odpowiedź) ---

    @Test
    void onChoice3_setsWonTrue() {
        game.onChoice(3);
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice3_setsGameOver() {
        game.onChoice(3);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    @Test
    void onChoice3_returnsSelf() {
        SceneManager result = game.onChoice(3);
        assertSame(game, result);
    }

    // --- Wybór 1 (KAMIL – błędna odpowiedź) ---

    @Test
    void onChoice1_setsWonFalse() {
        game.onChoice(1);
        assertFalse((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice1_setsGameOver() {
        game.onChoice(1);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    // --- Wybór 2 (DIANA – błędna odpowiedź) ---

    @Test
    void onChoice2_setsWonFalse() {
        game.onChoice(2);
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Po zakończeniu gry ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        game.onChoice(3);
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void onChoice_afterGameOver_returnsReturnScene() {
        game.onChoice(3);
        SceneManager result = game.onChoice(1);
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsSuccessText() {
        game.onChoice(3);
        assertTrue(game.getNarration().contains("idealnie"));
    }

    @Test
    void narration_afterLoss_containsFailText() {
        game.onChoice(1);
        assertTrue(game.getNarration().contains("złe"));
    }
}
