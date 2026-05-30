package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class LockpickingGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
    }

    private LockpickingGame gameWithTarget(int pin) {
        LockpickingGame game = new LockpickingGame(engine, returnScene);
        setField(game, "targetPin", pin);
        return game;
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        LockpickingGame game = new LockpickingGame(engine, returnScene);
        assertEquals("MINIGRA: Wytrych (Zręczność i Wyczucie)", game.getTitle());
    }

    @Test
    void initialGameOver_isFalse() {
        LockpickingGame game = new LockpickingGame(engine, returnScene);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    @Test
    void getOptions_notGameOver_returnsThreeOptions() {
        LockpickingGame game = new LockpickingGame(engine, returnScene);
        assertEquals(3, game.getOptions().size());
    }

    @Test
    void getNarration_initialPlayerGuess_isBrak() {
        LockpickingGame game = new LockpickingGame(engine, returnScene);
        assertTrue(game.getNarration().contains("brak"));
    }

    // --- Wybór 1 (nacisk 1) ---

    @Test
    void onChoice1_target1_wins() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(1); // weight=1, |1-1|=0 ≤ 1
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice1_target5_loses() {
        LockpickingGame game = gameWithTarget(5);
        game.onChoice(1); // |1-5|=4 > 1
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Wybór 2 (nacisk 3) ---

    @Test
    void onChoice2_target3_wins() {
        LockpickingGame game = gameWithTarget(3);
        game.onChoice(2); // weight=3, |3-3|=0 ≤ 1
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice2_target1_loses() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(2); // weight=3, |3-1|=2 > 1
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Wybór 3 (nacisk 5) ---

    @Test
    void onChoice3_target5_wins() {
        LockpickingGame game = gameWithTarget(5);
        game.onChoice(3); // weight=5, |5-5|=0 ≤ 1
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void onChoice3_target2_loses() {
        LockpickingGame game = gameWithTarget(2);
        game.onChoice(3); // |5-2|=3 > 1
        assertFalse((Boolean) getField(game, "won"));
    }

    // --- Każdy wybór ustawia gameOver ---

    @Test
    void anyChoice_setsGameOver() {
        LockpickingGame game = gameWithTarget(3);
        game.onChoice(1);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    @Test
    void anyChoice_returnsSelf() {
        LockpickingGame game = gameWithTarget(3);
        assertSame(game, game.onChoice(1));
    }

    // --- Po zakończeniu gry ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(1); // wygrana
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void afterWin_confirmationChoice_addsClueToLog() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(1); // wygrana
        game.onChoice(1); // potwierdzenie
        boolean hasClue = engine.getEventLog().getClues().stream()
                .anyMatch(c -> c.contains("Uniwersalny klucz"));
        assertTrue(hasClue);
    }

    @Test
    void afterWin_confirmationChoice_returnsReturnScene() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(1);
        SceneManager result = game.onChoice(1);
        assertSame(returnScene, result);
    }

    @Test
    void afterLoss_confirmationChoice_returnsReturnScene() {
        LockpickingGame game = gameWithTarget(5);
        game.onChoice(1); // przegrana
        SceneManager result = game.onChoice(1); // potwierdzenie
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsKlik() {
        LockpickingGame game = gameWithTarget(1);
        game.onChoice(1);
        assertTrue(game.getNarration().contains("KLIK"));
    }

    @Test
    void narration_afterLoss_containsTrzask() {
        LockpickingGame game = gameWithTarget(5);
        game.onChoice(1); // nacisk 1 vs pin 5 – przegrana
        assertTrue(game.getNarration().contains("TRZASK"));
    }
}
