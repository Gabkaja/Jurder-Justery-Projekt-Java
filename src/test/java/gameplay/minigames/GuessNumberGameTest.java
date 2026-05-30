package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.GameplayTestHelper;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class GuessNumberGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;
    private GuessNumberGame game;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
        game = new GuessNumberGame(engine, returnScene);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_returnsTitle() {
        assertEquals("MINIGRA: Złamanie Szyfru Kasetki", game.getTitle());
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
    void getOptions_notGameOver_returnsFiveOptions() {
        assertEquals(5, game.getOptions().size());
    }

    // --- Wybór opcji 5 (uciekamy) ---

    @Test
    void onChoice5_returnsReturnScene() {
        SceneManager result = game.onChoice(5);
        assertSame(returnScene, result);
    }

    @Test
    void onChoice5_doesNotSetGameOver() {
        game.onChoice(5);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    // --- Wybór opcji 4 (zawsze trafia w target) ---

    @Test
    void onChoice4_alwaysWins() {
        // options[3] = targetNumber, więc guess == targetNumber
        game.onChoice(4);
        assertTrue((Boolean) getField(game, "won"));
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    @Test
    void onChoice4_gameOver_returnsSelf() {
        game.onChoice(4); // ustawia gameOver
        SceneManager result = game.onChoice(4);
        // Po zakończeniu minigry wybór 1 uruchamia endMinigame i wraca do returnScene
        assertSame(returnScene, result);
    }

    // --- Wybór opcji 1 (wartość 5, nie zawsze trafia) ---

    @Test
    void onChoice1_reducesAttemptsLeft() {
        int before = (int) getField(game, "attemptsLeft");
        // Tylko jeśli 5 != targetNumber
        int target = (int) getField(game, "targetNumber");
        if (target != 5) {
            game.onChoice(1);
            int after = (int) getField(game, "attemptsLeft");
            assertEquals(before - 1, after);
        }
    }

    // --- Poziom trudności a liczba prób ---

    @Test
    void easyDifficulty_gives6Attempts() {
        engine.setDifficulty(engine.getDifficulty().EASY);
        GuessNumberGame easyGame = new GuessNumberGame(engine, returnScene);
        assertEquals(6, (int) getField(easyGame, "attemptsLeft"));
    }

    @Test
    void hardDifficulty_gives3Attempts() {
        engine.setDifficulty(engine.getDifficulty().HARD);
        GuessNumberGame hardGame = new GuessNumberGame(engine, returnScene);
        assertEquals(3, (int) getField(hardGame, "attemptsLeft"));
    }

    @Test
    void veryHardDifficulty_gives2Attempts() {
        engine.setDifficulty(engine.getDifficulty().VERY_HARD);
        GuessNumberGame vhGame = new GuessNumberGame(engine, returnScene);
        assertEquals(2, (int) getField(vhGame, "attemptsLeft"));
    }

    // --- Stan gameOver w narrracji ---

    @Test
    void narration_afterWin_containsSuccessText() {
        game.onChoice(4);
        assertTrue(game.getNarration().contains("satysfakcjonujące KLIKNIĘCIE") || game.getNarration().contains("Udało"));
    }

    @Test
    void options_afterGameOver_returnsOneOption() {
        game.onChoice(4);
        assertEquals(1, game.getOptions().size());
    }
}
