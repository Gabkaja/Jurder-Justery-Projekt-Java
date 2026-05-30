package gameplay;

import engine.GameEngine;
import engine.SceneManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class InvestigationSystemTest {

    private GameEngine engine;
    private SearchSystem previousScene;
    private InvestigationSystem system;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        previousScene = new SearchSystem(engine);
        system = new InvestigationSystem(engine, previousScene);
    }

    // --- getTitle ---

    @Test
    void getTitle_returnsDziennikSledztwa() {
        assertEquals("Dziennik śledztwa", system.getTitle());
    }

    // --- getNarration ---

    @Test
    void getNarration_returnsPrompt() {
        assertEquals("Co chcesz sprawdzić?", system.getNarration());
    }

    // --- getOptions ---

    @Test
    void getOptions_returnsSixOptions() {
        assertEquals(6, system.getOptions().size());
    }

    @Test
    void getOptions_lastOption_isPowrot() {
        List<String> opts = system.getOptions();
        assertEquals("Powrót", opts.get(opts.size() - 1));
    }

    // --- onChoice(6) – powrót ---

    @Test
    void onChoice6_returnsPreviousScene() {
        SceneManager result = system.onChoice(6);
        assertSame(previousScene, result);
    }

    // --- onChoice(1-5) – widoki listy ---

    @Test
    void onChoice1_returnsDifferentScene() {
        SceneManager result = system.onChoice(1);
        assertNotSame(system, result);
        assertNotSame(previousScene, result);
    }

    @Test
    void onChoice2_returnsDifferentScene() {
        SceneManager result = system.onChoice(2);
        assertNotSame(system, result);
    }

    @Test
    void onChoice3_returnsDifferentScene() {
        SceneManager result = system.onChoice(3);
        assertNotSame(system, result);
    }

    @Test
    void onChoice4_returnsDifferentScene() {
        SceneManager result = system.onChoice(4);
        assertNotSame(system, result);
    }

    @Test
    void onChoice5_returnsDifferentScene() {
        SceneManager result = system.onChoice(5);
        assertNotSame(system, result);
    }

    // --- onChoice(default) ---

    @Test
    void onChoiceDefault_returnsSelf() {
        SceneManager result = system.onChoice(99);
        assertSame(system, result);
    }

    // --- Zawartość list ---

    @Test
    void eventLog_empty_showsEmptyMessage() {
        SceneManager listScene = system.onChoice(1);
        assertTrue(listScene.getNarration().contains("pusty"));
    }

    @Test
    void eventLog_withEntry_showsEntry() {
        engine.getEventLog().addEntry("Testowy wpis");
        InvestigationSystem fresh = new InvestigationSystem(engine, previousScene);
        SceneManager listScene = fresh.onChoice(1);
        assertTrue(listScene.getNarration().contains("Testowy wpis"));
    }

    @Test
    void clues_empty_showsNoBraq() {
        SceneManager listScene = system.onChoice(2);
        assertTrue(listScene.getNarration().contains("Brak"));
    }

    @Test
    void clues_withClue_showsClue() {
        engine.getEventLog().addClue("Ślad krwi");
        InvestigationSystem fresh = new InvestigationSystem(engine, previousScene);
        SceneManager listScene = fresh.onChoice(2);
        assertTrue(listScene.getNarration().contains("Ślad krwi"));
    }

    @Test
    void locationList_withLocations_showsLocationName() {
        SceneManager listScene = system.onChoice(4);
        // engine ma lokacje Sypialnia i Gabinet
        assertTrue(listScene.getNarration().contains("Sypialnia") || listScene.getNarration().contains("Gabinet"));
    }

    // --- Nawigacja powrotu z widoku listy ---

    @Test
    void listScene_lastOption_returnsInvestigationSystem() {
        SceneManager listScene = system.onChoice(1);
        List<String> opts = listScene.getOptions();
        // Ostatnia opcja to "Powrót"
        SceneManager back = listScene.onChoice(opts.size());
        assertSame(system, back);
    }
}
