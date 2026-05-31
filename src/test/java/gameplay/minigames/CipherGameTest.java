package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class CipherGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;
    private CipherGame game;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine(); // killer = "KAMIL"
        returnScene = new SearchSystem(engine);
        game = new CipherGame(engine, returnScene);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        assertEquals("MINIGRA: Ślad na Lustrze", game.getTitle());
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
    void getNarration_containsCipherText() {
        assertTrue(game.getNarration().contains("IMAK"));
    }

    @Test
    void getOptions_notGameOver_returnsFourOptions() {
        assertEquals(4, game.getOptions().size());
    }

    // --- Wybór 4 (ignoruj napis) ---

    @Test
    void onChoice4_skipReturnsReturnScene() {
        SceneManager result = game.onChoice(4);
        assertSame(returnScene, result);
    }

    @Test
    void onChoice4_doesNotSetGameOver() {
        game.onChoice(4);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    // --- Wybór 2 (KAMIL – poprawny anagram) ---

    @Test
    void onChoice2_setsWonTrue() {
        game.onChoice(2);
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice2_setsGameOver() {
        game.onChoice(2);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    @Test
    void onChoice2_returnsSelf() {
        SceneManager result = game.onChoice(2);
        assertSame(game, result);
    }

    // --- Wybór 1 (LIMA – błędny) ---

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

    // --- Wybór 3 (imię mordercy) gdy killer = KAMIL ---

    @Test
    void onChoice3_killerIsKamil_setsWonTrue() {
        // killer z makeFullEngine to "KAMIL"
        game.onChoice(3);
        assertTrue((Boolean) getField(game, "won"));
    }

    // --- Po zakończeniu gry ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        game.onChoice(2);
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void onChoice_afterGameOver_returnsReturnScene() {
        game.onChoice(2); // wygrana
        SceneManager result = game.onChoice(1); // wybór "powrót"
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsSuccessText() {
        game.onChoice(2);
        assertTrue(game.getNarration().contains("logiczną całość"));
    }

    @Test
    void narration_afterLoss_containsFailText() {
        game.onChoice(1);
        assertTrue(game.getNarration().contains("zniknął"));
    }
}
