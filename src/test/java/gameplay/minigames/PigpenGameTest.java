package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class PigpenGameTest {

    private GameEngine engine;
    private SearchSystem returnScene;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        returnScene = new SearchSystem(engine);
    }

    // --- Stan początkowy ---

    @Test
    void getTitle_containsPigpen() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        assertTrue(game.getTitle().contains("Pigpen") || game.getTitle().contains("Wolnomularski"));
    }

    @Test
    void initialGameOver_isFalse() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    @Test
    void getOptions_notGameOver_returnsFourOptions() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        assertEquals(4, game.getOptions().size());
    }

    // --- Wybór 4 (odejdź) ---

    @Test
    void onChoice4_returnsReturnScene() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        assertSame(returnScene, game.onChoice(4));
    }

    @Test
    void onChoice4_doesNotSetGameOver() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        game.onChoice(4);
        assertFalse((Boolean) getField(game, "gameOver"));
    }

    // --- Poprawna odpowiedź (sprawdzamy przez refleksję jakie solution jest losowane) ---

    @Test
    void correctAnswerChoice_setsWonTrue() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        String solution = getField(game, "solution");

        int correctChoice = switch (solution) {
            case "KAT"  -> 1;
            case "AKT"  -> 2;
            case "TRUP" -> 3;
            default     -> throw new IllegalStateException("Nieznane solution: " + solution);
        };

        game.onChoice(correctChoice);
        assertTrue((Boolean) getField(game, "won"));
    }

    @Test
    void correctAnswerChoice_setsGameOver() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        String solution = getField(game, "solution");
        int correctChoice = switch (solution) {
            case "KAT"  -> 1;
            case "AKT"  -> 2;
            case "TRUP" -> 3;
            default     -> throw new IllegalStateException("Nieznane solution: " + solution);
        };
        game.onChoice(correctChoice);
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    // --- Błędna odpowiedź ---

    @Test
    void wrongAnswerChoice_setsWonFalse() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        String solution = getField(game, "solution");

        // Wybieramy opcję inną niż poprawna
        int wrongChoice = switch (solution) {
            case "KAT"  -> 2; // AKT ≠ KAT
            case "AKT"  -> 1; // KAT ≠ AKT
            case "TRUP" -> 1; // KAT ≠ TRUP
            default     -> throw new IllegalStateException("Nieznane solution: " + solution);
        };

        game.onChoice(wrongChoice);
        assertFalse((Boolean) getField(game, "won"));
    }

    @Test
    void wrongAnswerChoice_setsGameOver() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        game.onChoice(1); // dowolna odpowiedź kończy grę
        assertTrue((Boolean) getField(game, "gameOver"));
    }

    // --- Po zakończeniu gry ---

    @Test
    void options_afterGameOver_returnsOneOption() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        game.onChoice(1);
        assertEquals(1, game.getOptions().size());
    }

    @Test
    void onChoice_afterGameOver_returnsReturnScene() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        game.onChoice(1);
        SceneManager result = game.onChoice(1);
        assertSame(returnScene, result);
    }

    @Test
    void narration_afterWin_containsSuccessText() {
        PigpenGame game = new PigpenGame(engine, returnScene);
        String solution = getField(game, "solution");
        int correctChoice = switch (solution) {
            case "KAT"  -> 1;
            case "AKT"  -> 2;
            case "TRUP" -> 3;
            default     -> throw new IllegalStateException("Nieznane solution: " + solution);
        };
        game.onChoice(correctChoice);
        assertTrue(game.getNarration().contains("kliknięcie"));
    }

    @Test
    void narration_afterLoss_containsFailText() {
        // Wymuszamy błędną odpowiedź ustawiając solution na "KAT" i wybierając AKT
        PigpenGame game = new PigpenGame(engine, returnScene);
        setField(game, "solution", "KAT");
        game.onChoice(2); // AKT ≠ KAT
        assertTrue(game.getNarration().contains("błąd") || game.getNarration().contains("dezaktywowany"));
    }
}
