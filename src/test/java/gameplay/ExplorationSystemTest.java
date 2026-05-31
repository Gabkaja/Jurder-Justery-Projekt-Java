package gameplay;

import engine.GameEngine;
import engine.SceneManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.Location;

import java.util.List;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class ExplorationSystemTest {

    private GameEngine engine;
    private SearchSystem previousScene;
    private ExplorationSystem system;

    /** Lokacje: sypialnia <-> gabinet */
    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        // currentLocation = sypialnia z przejściem do gabinet (ustawione w makeFullEngine)
        previousScene = new SearchSystem(engine);
        system = new ExplorationSystem(engine, previousScene);
    }

    // --- getTitle ---

    @Test
    void getTitle_returnsNavigationLabel() {
        assertEquals("Nawigacja — Wybór kolejnego pomieszczenia", system.getTitle());
    }

    // --- getNarration ---

    @Test
    void getNarration_showsCurrentLocationName() {
        assertTrue(system.getNarration().contains("Sypialnia"));
    }

    // --- getOptions ---

    @Test
    void getOptions_containsAdjacentRoom() {
        List<String> opts = system.getOptions();
        assertTrue(opts.stream().anyMatch(o -> o.contains("Gabinet")));
    }

    @Test
    void getOptions_lastOption_isRozmyslSie() {
        List<String> opts = system.getOptions();
        assertEquals("Rozmyśl się (zostań w tym pokoju)", opts.get(opts.size() - 1));
    }

    @Test
    void getOptions_countEqualsPassagesPlusOne() {
        int passages = engine.getCurrentLocation().getPassages().size();
        assertEquals(passages + 1, system.getOptions().size());
    }

    // --- onChoice – przejście do sąsiedniego pokoju ---

    @Test
    void onChoice1_changesCurrentLocation() {
        Location before = engine.getCurrentLocation();
        system.onChoice(1);
        assertNotSame(before, engine.getCurrentLocation());
    }

    @Test
    void onChoice1_newLocationIsGabinet() {
        system.onChoice(1);
        assertEquals("gabinet", engine.getCurrentLocation().getId());
    }

    @Test
    void onChoice1_addsEntryToEventLog() {
        system.onChoice(1);
        assertFalse(engine.getEventLog().getEntries().isEmpty());
    }

    @Test
    void onChoice1_returnsNewSearchSystem() {
        SceneManager result = system.onChoice(1);
        assertInstanceOf(SearchSystem.class, result);
        assertNotSame(previousScene, result);
    }

    // --- onChoice – ostatnia opcja (rozmyślenie się) ---

    @Test
    void lastChoice_returnsPreviousScene() {
        int lastChoice = system.getOptions().size();
        SceneManager result = system.onChoice(lastChoice);
        assertSame(previousScene, result);
    }

    @Test
    void lastChoice_doesNotChangeLocation() {
        Location before = engine.getCurrentLocation();
        int lastChoice = system.getOptions().size();
        system.onChoice(lastChoice);
        assertSame(before, engine.getCurrentLocation());
    }

    // --- Brak lokacji na liście ---

    @Test
    void onChoice1_locationNotFound_returnsSelf() {
        // Podmień passages na ID nie istniejące w locations
        Location loc = makeLocation("kuchnia", "Kuchnia", List.of("nieznane-id"));
        setField(engine, "currentLocation", loc);
        ExplorationSystem noTarget = new ExplorationSystem(engine, previousScene);
        SceneManager result = noTarget.onChoice(1);
        assertSame(noTarget, result);
    }
}
