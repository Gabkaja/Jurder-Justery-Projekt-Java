package gameplay;

import engine.GameEngine;
import engine.SceneManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import world.Location;

import java.util.List;

import static gameplay.GameplayTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class SearchSystemTest {

    private GameEngine engine;
    private SearchSystem system;

    @BeforeEach
    void setUp() {
        engine = makeFullEngine();
        system = new SearchSystem(engine);
    }

    // --- getTitle ---

    @Test
    void getTitle_returnsCorrectLabel() {
        assertEquals("Interakcja z otoczeniem", system.getTitle());
    }

    // --- getNarration ---

    @Test
    void getNarration_withLocation_showsLocationName() {
        String narration = system.getNarration();
        assertTrue(narration.contains("Sypialnia"));
    }

    @Test
    void getNarration_withoutLocation_showsNieznane() {
        setField(engine, "currentLocation", null);
        assertTrue(system.getNarration().contains("Nieznane pomieszczenie"));
    }

    @Test
    void getNarration_initialSearchCount_isZero() {
        assertTrue(system.getNarration().contains("0"));
    }

    // --- getOptions ---

    @Test
    void getOptions_returnsSixOptions() {
        assertEquals(6, system.getOptions().size());
    }

    // --- onChoice(1) – przeszukanie pokoju ---

    @Test
    void onChoice1_firstSearch_addsCluToEventLog() {
        system.onChoice(1);
        assertFalse(engine.getEventLog().getClues().isEmpty());
    }

    @Test
    void onChoice1_firstSearch_returnsSelf() {
        SceneManager result = system.onChoice(1);
        assertSame(system, result);
    }

    @Test
    void onChoice1_secondSearch_noNewClueAdded() {
        system.onChoice(1);
        int cluesAfterFirst = engine.getEventLog().getClues().size();
        system.onChoice(1);
        assertEquals(cluesAfterFirst, engine.getEventLog().getClues().size());
    }

    @Test
    void onChoice1_secondSearch_narrationContainsAlreadySearched() {
        system.onChoice(1);
        system.onChoice(1);
        assertTrue(system.getNarration().contains("Przetrząsnąłeś"));
    }

    @Test
    void onChoice1_incrementsSearchCount() {
        system.onChoice(1);
        assertTrue(system.getNarration().contains("1"));
    }

    // --- onChoice(2) – minigra ---

    @Test
    void onChoice2_sypialniaRoom_returnsAlibiCheckGame() {
        // currentLocation id = "sypialnia" (ustawione w makeFullEngine)
        SceneManager result = system.onChoice(2);
        assertInstanceOf(gameplay.minigames.AlibiCheckGame.class, result);
    }

    @Test
    void onChoice2_afterRoomDone_returnsSelf() {
        system.markRoomAsDone("sypialnia");
        SceneManager result = system.onChoice(2);
        assertSame(system, result);
    }

    // --- onChoice(3) – wybór podejrzanego ---

    @Test
    void onChoice3_returnsSceneManagerNotSelf() {
        SceneManager result = system.onChoice(3);
        assertNotSame(system, result);
    }

    // --- onChoice(4) – eksploracja ---

    @Test
    void onChoice4_returnsExplorationSystem() {
        SceneManager result = system.onChoice(4);
        assertInstanceOf(ExplorationSystem.class, result);
    }

    // --- onChoice(5) – dziennik ---

    @Test
    void onChoice5_returnsInvestigationSystem() {
        SceneManager result = system.onChoice(5);
        assertInstanceOf(InvestigationSystem.class, result);
    }

    // --- onChoice(default) ---

    @Test
    void onChoiceDefault_returnsSelf() {
        assertSame(system, system.onChoice(99));
    }

    // --- markRoomAsDone ---

    @Test
    void markRoomAsDone_null_doesNotThrow() {
        assertDoesNotThrow(() -> system.markRoomAsDone(null));
    }

    @Test
    void markRoomAsDone_validId_preventsMinigame() {
        system.markRoomAsDone("sypialnia");
        SceneManager result = system.onChoice(2);
        assertSame(system, result);
    }

    // --- setMinigameResult ---

    @Test
    void setMinigameResult_success_updatesNarration() {
        system.setMinigameResult(true, "Świetna robota!");
        assertTrue(system.getNarration().contains("Świetna robota!"));
    }

    @Test
    void setMinigameResult_failure_updatesNarration() {
        system.setMinigameResult(false, "Nie udało się.");
        assertTrue(system.getNarration().contains("Nie udało się."));
    }

    @Test
    void setMinigameResult_marksCurrentRoomAsDone() {
        system.setMinigameResult(true, "OK");
        // po oznaczeniu pokoju – kolejna minigra w tym samym pokoju powinna zwrócić self
        SceneManager result = system.onChoice(2);
        assertSame(system, result);
    }

    // --- generateSpecificClue ---

    @Test
    void generateSpecificClue_crimeScene_returnsSpecialMessage() {
        Location crimeScene = engine.getMurderCase().getCrimeScene();
        String clue = system.generateSpecificClue(crimeScene, "RANDOM");
        assertTrue(clue.contains("miejscem zbrodni"));
    }

    @Test
    void generateSpecificClue_killerCategory_containsKillerName() {
        Location other = makeLocation("kuchnia", "Kuchnia", List.of());
        String clue = system.generateSpecificClue(other, "KILLER");
        assertTrue(clue.contains("KAMIL"));
    }

    @Test
    void generateSpecificClue_motiveCategory_containsDescription() {
        Location other = makeLocation("kuchnia", "Kuchnia", List.of());
        String clue = system.generateSpecificClue(other, "MOTIVE");
        assertTrue(clue.contains("Ochrona tajemnicy rodzinnej"));
    }

    @Test
    void generateSpecificClue_weaponCategory_containsWeaponCategory() {
        Location other = makeLocation("kuchnia", "Kuchnia", List.of());
        String clue = system.generateSpecificClue(other, "WEAPON");
        assertTrue(clue.contains("OSTRE")); // "Nóż kuchenny" → OSTRE
    }

    @Test
    void generateSpecificClue_noMurderCase_returnsFallback() {
        setField(engine, "murderCase", null);
        SearchSystem noCase = new SearchSystem(engine);
        Location loc = makeLocation("kuchnia", "Kuchnia", List.of());
        String clue = noCase.generateSpecificClue(loc, "RANDOM");
        assertNotNull(clue);
        assertFalse(clue.isBlank());
    }
}
