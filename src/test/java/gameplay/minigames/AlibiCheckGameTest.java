package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class AlibiCheckGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;
    private AlibiCheckGame game;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
        game = new AlibiCheckGame(engine, returnScene);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        assertEquals("MINIGRA: Weryfikacja Logiki Alibi", game.getTitle());
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
    void getNarration_containsMurderTime() {
        assertTrue(game.getNarration().contains("22:00"));
    }

    @Test
    void getOptions_notGameOver_returnsThreeOptions() {
        assertEquals(3, game.getOptions().size());
    }

    // --- Wybór 3 (odejście) ---

    @Test
    void onChoice3_returnsReturnScene() {
        SceneManager result = game.onChoice(3);
        assertSame(returnScene, result);
    }

    @Test
    void onChoice3_doesNotSetGameOver() {
        game.onChoice(3);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    // --- Wybór 2 (żelazne alibi – poprawna odpowiedź) ---

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

    // --- Wybór 1 (możliwe alibi – błędna odpowiedź) ---

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

    // --- Stan po zakończeniu gry (gameOver) ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        game.onChoice(2);
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void afterWin_confirmationChoice_addsClueToLog() {
        game.onChoice(2); // wygrana → gameOver
        game.onChoice(1); // potwierdzenie po gameOver
        boolean hasAlibiClue = engine.getEventLog().getClues().stream()
                .anyMatch(c -> c.contains("NIEWINNY"));
        assertTrue(hasAlibiClue);
    }

    @Test
    void afterWin_confirmationChoice_returnsReturnScene() {
        game.onChoice(2);
        SceneManager result = game.onChoice(1);
        assertSame(returnScene, result);
    }

    @Test
    void afterLoss_confirmationChoice_returnsReturnScene() {
        game.onChoice(1); // przegrana → gameOver
        SceneManager result = game.onChoice(1); // potwierdzenie
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsSuccessText() {
        game.onChoice(2);
        assertTrue(game.getNarration().contains("powiodła się"));
    }

    @Test
    void narration_afterLoss_containsFailText() {
        game.onChoice(1);
        assertTrue(game.getNarration().contains("Pogubiłeś się"));
    }
}
