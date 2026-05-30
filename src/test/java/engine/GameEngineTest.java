package engine;

import characters.PlayerCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.EventLog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        InputStream stream = new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8));
        InputStream original = System.in;
        System.setIn(stream);
        engine = new GameEngine();
        System.setIn(original);
    }

    // --- Stan początkowy ---

    @Test
    void initialDifficulty_isMedium() {
        assertEquals(Difficulty.MEDIUM, engine.getDifficulty());
    }

    @Test
    void initialRunning_isFalse() {
        assertFalse(engine.isRunning());
    }

    @Test
    void initialPlayer_isNull() {
        assertNull(engine.getPlayer());
    }

    @Test
    void initialEventLog_isNotNull() {
        assertNotNull(engine.getEventLog());
    }

    @Test
    void initialSuspects_isNull() {
        assertNull(engine.getSuspects());
    }

    @Test
    void initialLocations_isNull() {
        assertNull(engine.getLocations());
    }

    @Test
    void initialCurrentLocation_isNull() {
        assertNull(engine.getCurrentLocation());
    }

    @Test
    void initialMurderCase_isNull() {
        assertNull(engine.getMurderCase());
    }

    // --- stop ---

    @Test
    void stop_setsRunningToFalse() {
        setRunning(engine, true);
        engine.stop();
        assertFalse(engine.isRunning());
    }

    // --- setDifficulty ---

    @Test
    void setDifficulty_easy_changesLevel() {
        engine.setDifficulty(Difficulty.EASY);
        assertEquals(Difficulty.EASY, engine.getDifficulty());
    }

    @Test
    void setDifficulty_veryHard_changesLevel() {
        engine.setDifficulty(Difficulty.VERY_HARD);
        assertEquals(Difficulty.VERY_HARD, engine.getDifficulty());
    }

    // --- setPlayer ---

    @Test
    void setPlayer_withKnownDifficulty_updatesDifficulty() {
        PlayerCharacter pc = playerWithDifficulty("HARD");
        engine.setPlayer(pc);
        assertEquals(Difficulty.HARD, engine.getDifficulty());
    }

    @Test
    void setPlayer_withLowercaseDifficulty_updatesDifficulty() {
        PlayerCharacter pc = playerWithDifficulty("easy");
        engine.setPlayer(pc);
        assertEquals(Difficulty.EASY, engine.getDifficulty());
    }

    @Test
    void setPlayer_withUnknownDifficulty_keepsMedium() {
        PlayerCharacter pc = playerWithDifficulty("LEGENDARY");
        engine.setPlayer(pc);
        assertEquals(Difficulty.MEDIUM, engine.getDifficulty());
    }

    @Test
    void setPlayer_withNullDifficulty_keepsMedium() {
        PlayerCharacter pc = playerWithDifficulty(null);
        engine.setPlayer(pc);
        assertEquals(Difficulty.MEDIUM, engine.getDifficulty());
    }

    @Test
    void setPlayer_null_playerRemainsNull() {
        engine.setPlayer(null);
        assertNull(engine.getPlayer());
    }

    @Test
    void setPlayer_validPlayer_storesPlayer() {
        PlayerCharacter pc = playerWithDifficulty("MEDIUM");
        engine.setPlayer(pc);
        assertSame(pc, engine.getPlayer());
    }

    // --- helpers ---

    private PlayerCharacter playerWithDifficulty(String diff) {
        try {
            PlayerCharacter pc = new PlayerCharacter();
            Field f = PlayerCharacter.class.getDeclaredField("difficulty");
            f.setAccessible(true);
            f.set(pc, diff);
            return pc;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setRunning(GameEngine engine, boolean value) {
        try {
            Field f = GameEngine.class.getDeclaredField("running");
            f.setAccessible(true);
            f.set(engine, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
