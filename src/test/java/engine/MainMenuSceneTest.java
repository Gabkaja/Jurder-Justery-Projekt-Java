package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuSceneTest {

    private GameEngine engine;
    private MainMenuScene scene;

    @BeforeEach
    void setUp() {
        // Podajemy minimalny strumień wejściowy, żeby GameLoop nie blokował podczas budowy silnika
        InputStream stream = new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8));
        InputStream original = System.in;
        System.setIn(stream);
        engine = new GameEngine();
        System.setIn(original);

        scene = new MainMenuScene(engine);
    }

    // --- getTitle ---

    @Test
    void getTitle_returnsGameName() {
        assertEquals("JURDER JYSTERY", scene.getTitle());
    }

    // --- getNarration ---

    @Test
    void getNarration_returnsEmptyString() {
        assertEquals("", scene.getNarration());
    }

    // --- getOptions ---

    @Test
    void getOptions_returnsTwoOptions() {
        List<String> options = scene.getOptions();
        assertEquals(2, options.size());
    }

    @Test
    void getOptions_firstOption_isNowaGra() {
        assertEquals("Nowa gra", scene.getOptions().get(0));
    }

    @Test
    void getOptions_secondOption_isWyjscie() {
        assertEquals("Wyjście", scene.getOptions().get(1));
    }

    // --- onChoice ---

    @Test
    void onChoice_exit_stopsEngine() {
        scene.onChoice(2);
        assertFalse(engine.isRunning());
    }

    @Test
    void onChoice_exit_returnsSameScene() {
        SceneManager result = scene.onChoice(2);
        assertSame(scene, result);
    }

    @Test
    void onChoice_newGame_returnsCharacterSelectScene() {
        SceneManager result = scene.onChoice(1);
        assertInstanceOf(CharacterSelectScene.class, result);
    }
}
