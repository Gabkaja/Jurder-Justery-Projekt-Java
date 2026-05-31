package characters;

import com.google.gson.Gson;
import dialogue.NpcDialogueConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuspectTest {

    private Suspect suspect;

    @BeforeEach
    void setUp() {
        suspect = new Suspect();
    }

    // --- Wartości domyślne ---

    @Test
    void defaultTrust_is20() {
        assertEquals(20, suspect.getTrust());
    }

    @Test
    void defaultStressLevel_is0() {
        assertEquals(0, suspect.getStressLevel());
    }

    @Test
    void defaultEmotion_isNeutralny() {
        assertEquals("NEUTRALNY", suspect.getCurrentEmotion());
    }

    @Test
    void defaultDialogueConfig_isNull() {
        assertNull(suspect.getDialogueConfig());
    }

    @Test
    void defaultLocationId_isNull() {
        assertNull(suspect.getLocationId());
    }

    // --- changeTrust ---

    @Test
    void changeTrust_positiveAmount_increasesTrust() {
        suspect.changeTrust(30);
        assertEquals(50, suspect.getTrust());
    }

    @Test
    void changeTrust_negativeAmount_decreasesTrust() {
        suspect.changeTrust(-10);
        assertEquals(10, suspect.getTrust());
    }

    @Test
    void changeTrust_clampedAtMaximum100() {
        suspect.changeTrust(200);
        assertEquals(100, suspect.getTrust());
    }

    @Test
    void changeTrust_clampedAtMinimum0() {
        suspect.changeTrust(-200);
        assertEquals(0, suspect.getTrust());
    }

    @Test
    void changeTrust_exactlyAt100_staysAt100() {
        suspect.changeTrust(80);
        assertEquals(100, suspect.getTrust());
    }

    @Test
    void changeTrust_exactlyAt0_staysAt0() {
        suspect.changeTrust(-20);
        assertEquals(0, suspect.getTrust());
    }

    @Test
    void changeTrust_multipleChanges_accumulatCorrectly() {
        suspect.changeTrust(10);
        suspect.changeTrust(15);
        assertEquals(45, suspect.getTrust());
    }

    // --- changeStress ---

    @Test
    void changeStress_positiveAmount_increasesStress() {
        suspect.changeStress(40);
        assertEquals(40, suspect.getStressLevel());
    }

    @Test
    void changeStress_negativeAmount_decreasesStress() {
        suspect.changeStress(50);
        suspect.changeStress(-20);
        assertEquals(30, suspect.getStressLevel());
    }

    @Test
    void changeStress_clampedAtMaximum100() {
        suspect.changeStress(999);
        assertEquals(100, suspect.getStressLevel());
    }

    @Test
    void changeStress_clampedAtMinimum0() {
        suspect.changeStress(-999);
        assertEquals(0, suspect.getStressLevel());
    }

    @Test
    void changeStress_multipleChanges_accumulateCorrectly() {
        suspect.changeStress(30);
        suspect.changeStress(30);
        assertEquals(60, suspect.getStressLevel());
    }

    // --- setCurrentEmotion ---

    @Test
    void setCurrentEmotion_updatesEmotion() {
        suspect.setCurrentEmotion("ZDENERWOWANY");
        assertEquals("ZDENERWOWANY", suspect.getCurrentEmotion());
    }

    @Test
    void setCurrentEmotion_toNull_returnsNull() {
        suspect.setCurrentEmotion(null);
        assertNull(suspect.getCurrentEmotion());
    }

    // --- setLocationId ---

    @Test
    void setLocationId_updatesLocationId() {
        suspect.setLocationId("salon");
        assertEquals("salon", suspect.getLocationId());
    }

    // --- setDialogueConfig ---

    @Test
    void setDialogueConfig_updatesConfig() {
        NpcDialogueConfig config = new NpcDialogueConfig();
        suspect.setDialogueConfig(config);
        assertSame(config, suspect.getDialogueConfig());
    }

    // --- Gettery ustawiane przez Gson ---

    @Test
    void gsonDeserialization_setsNameAndTitle() {
        String json = "{\"id\":\"npc_001\",\"name\":\"Cyprian Czerwiński\",\"title\":\"Notariusz\"}";
        Suspect s = new Gson().fromJson(json, Suspect.class);
        assertEquals("npc_001", s.getId());
        assertEquals("Cyprian Czerwiński", s.getName());
        assertEquals("Notariusz", s.getTitle());
    }

    @Test
    void gsonDeserialization_trustIsStillDefault() {
        String json = "{\"id\":\"npc_001\",\"name\":\"Test\"}";
        Suspect s = new Gson().fromJson(json, Suspect.class);
        assertEquals(20, s.getTrust());
        assertEquals(0, s.getStressLevel());
        assertEquals("NEUTRALNY", s.getCurrentEmotion());
    }

    // --- toString ---

    @Test
    void toString_containsNameTitleStressAndTrust() {
        String json = "{\"name\":\"Jan Nowak\",\"title\":\"Kucharz\"}";
        Suspect s = new Gson().fromJson(json, Suspect.class);
        String result = s.toString();
        assertTrue(result.contains("Jan Nowak"));
        assertTrue(result.contains("Kucharz"));
        assertTrue(result.contains("Stres:"));
        assertTrue(result.contains("Zaufanie:"));
    }

    @Test
    void toString_withNullNameAndTitle_doesNotThrow() {
        assertDoesNotThrow(() -> suspect.toString());
    }
}
