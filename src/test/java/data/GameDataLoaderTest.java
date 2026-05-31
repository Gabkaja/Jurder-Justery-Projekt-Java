package data;

import characters.Suspect;
import characters.PlayerCharacter;
import com.google.gson.Gson;
import world.Location;
import world.Motive;
import world.MurderCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDataLoaderTest {

    private GameDataLoader loader;
    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        loader = new GameDataLoader();
    }

    // -------------------------------------------------------------------------
    // loadLocations
    // -------------------------------------------------------------------------

    @Test
    void loadLocations_returnsNonEmptyList() {
        List<Location> locations = loader.loadLocations();
        assertNotNull(locations);
        assertFalse(locations.isEmpty());
    }

    @Test
    void loadLocations_eachLocationHasIdAndName() {
        for (Location loc : loader.loadLocations()) {
            assertNotNull(loc.getId(),   "id lokacji nie może być null");
            assertNotNull(loc.getName(), "name lokacji nie może być null");
        }
    }

    @Test
    void loadLocations_eachLocationHasWeaponsList() {
        for (Location loc : loader.loadLocations()) {
            assertNotNull(loc.getWeapons(),
                    "Lokacja '" + loc.getId() + "' nie ma listy broni");
            assertFalse(loc.getWeapons().isEmpty(),
                    "Lokacja '" + loc.getId() + "' ma pustą listę broni");
        }
    }

    // -------------------------------------------------------------------------
    // loadSuspects
    // -------------------------------------------------------------------------

    @Test
    void loadSuspects_returnsNonEmptyList() {
        List<Suspect> suspects = loader.loadSuspects();
        assertNotNull(suspects);
        assertFalse(suspects.isEmpty());
    }

    @Test
    void loadSuspects_eachSuspectHasIdAndName() {
        for (Suspect s : loader.loadSuspects()) {
            assertNotNull(s.getId(),   "id podejrzanego nie może być null");
            assertNotNull(s.getName(), "name podejrzanego nie może być null");
        }
    }

    @Test
    void loadSuspects_eachSuspectHasMotives() {
        for (Suspect s : loader.loadSuspects()) {
            assertNotNull(s.getMotives(),
                    "Podejrzany '" + s.getId() + "' nie ma listy motywów");
            assertFalse(s.getMotives().isEmpty(),
                    "Podejrzany '" + s.getId() + "' ma pustą listę motywów");
        }
    }

    @Test
    void loadSuspects_defaultTrustAndStressArePreserved() {
        // Gson nie nadpisuje pól z wartościami domyślnymi gdy ich brak w JSON
        for (Suspect s : loader.loadSuspects()) {
            assertEquals(20, s.getTrust(),
                    "Domyślne zaufanie podejrzanego '" + s.getId() + "' powinno wynosić 20");
            assertEquals(0, s.getStressLevel(),
                    "Domyślny stres podejrzanego '" + s.getId() + "' powinien wynosić 0");
        }
    }

    // -------------------------------------------------------------------------
    // loadPlayerCharacters
    // -------------------------------------------------------------------------

    @Test
    void loadPlayerCharacters_returnsNonEmptyList() {
        List<PlayerCharacter> pcs = loader.loadPlayerCharacters();
        assertNotNull(pcs);
        assertFalse(pcs.isEmpty());
    }

    @Test
    void loadPlayerCharacters_eachCharacterHasIdNameAndDifficulty() {
        for (PlayerCharacter pc : loader.loadPlayerCharacters()) {
            assertNotNull(pc.getId(),         "id postaci gracza nie może być null");
            assertNotNull(pc.getName(),       "name postaci gracza nie może być null");
            assertNotNull(pc.getDifficulty(), "difficulty postaci gracza nie może być null");
        }
    }

    @Test
    void loadPlayerCharacters_eachCharacterHasSpecialSkills() {
        for (PlayerCharacter pc : loader.loadPlayerCharacters()) {
            assertNotNull(pc.getSpecialSkills(),
                    "Postać '" + pc.getId() + "' nie ma listy umiejętności");
            assertFalse(pc.getSpecialSkills().isEmpty(),
                    "Postać '" + pc.getId() + "' ma pustą listę umiejętności");
        }
    }

    // -------------------------------------------------------------------------
    // loadMurderCase — guardy na złe dane
    // -------------------------------------------------------------------------

    @Test
    void loadMurderCase_nullSuspects_returnsNull() {
        List<Location> locations = loader.loadLocations();
        assertNull(loader.loadMurderCase(null, locations));
    }

    @Test
    void loadMurderCase_nullLocations_returnsNull() {
        List<Suspect> suspects = loader.loadSuspects();
        assertNull(loader.loadMurderCase(suspects, null));
    }

    @Test
    void loadMurderCase_emptySuspects_returnsNull() {
        List<Location> locations = loader.loadLocations();
        assertNull(loader.loadMurderCase(List.of(), locations));
    }

    @Test
    void loadMurderCase_emptyLocations_returnsNull() {
        List<Suspect> suspects = loader.loadSuspects();
        assertNull(loader.loadMurderCase(suspects, List.of()));
    }

    // -------------------------------------------------------------------------
    // loadMurderCase — poprawne dane
    // -------------------------------------------------------------------------

    @Test
    void loadMurderCase_validData_returnsNonNull() {
        MurderCase mc = loader.loadMurderCase(loader.loadSuspects(), loader.loadLocations());
        assertNotNull(mc);
    }

    @Test
    void loadMurderCase_killerComesFromSuspectList() {
        List<Suspect> suspects = loader.loadSuspects();
        List<Location> locations = loader.loadLocations();
        MurderCase mc = loader.loadMurderCase(suspects, locations);

        List<String> suspectNames = suspects.stream().map(Suspect::getName).toList();
        assertTrue(suspectNames.contains(mc.getKiller()),
                "Morderca '" + mc.getKiller() + "' musi należeć do listy podejrzanych");
    }

    @Test
    void loadMurderCase_crimeSceneComesFromLocationList() {
        List<Suspect> suspects = loader.loadSuspects();
        List<Location> locations = loader.loadLocations();
        MurderCase mc = loader.loadMurderCase(suspects, locations);

        List<String> locationIds = locations.stream().map(Location::getId).toList();
        assertNotNull(mc.getCrimeScene());
        assertTrue(locationIds.contains(mc.getCrimeScene().getId()),
                "Miejsce zbrodni musi należeć do listy lokacji");
    }

    @Test
    void loadMurderCase_weaponComesFromCrimeSceneWeapons() {
        List<Suspect> suspects = loader.loadSuspects();
        List<Location> locations = loader.loadLocations();
        MurderCase mc = loader.loadMurderCase(suspects, locations);

        assertNotNull(mc.getWeapon());
        assertTrue(mc.getCrimeScene().getWeapons().contains(mc.getWeapon()),
                "Broń '" + mc.getWeapon() + "' musi znajdować się w broniach miejsca zbrodni");
    }

    @Test
    void loadMurderCase_motiveMatchesKiller() {
        List<Suspect> suspects = loader.loadSuspects();
        List<Location> locations = loader.loadLocations();
        MurderCase mc = loader.loadMurderCase(suspects, locations);

        Motive motive = mc.getMotive();
        assertNotNull(motive, "Motyw nie może być null gdy morderca ma motywy");
        assertNotNull(motive.getId());

        // Motyw pochodzi z puli motywów mordercy
        Suspect killer = suspects.stream()
                .filter(s -> s.getName().equals(mc.getKiller()))
                .findFirst()
                .orElseThrow();
        List<String> killerMotiveTypes = killer.getMotives().stream()
                .map(m -> m.getType())
                .toList();
        assertTrue(killerMotiveTypes.contains(motive.getId()),
                "Typ motywu '" + motive.getId() + "' musi należeć do motywów mordercy");
    }

    @Test
    void loadMurderCase_singleSuspect_alwaysPicksThatSuspect() {
        String json = "[{\"id\":\"npc_test\",\"name\":\"Testowy Podejrzany\","
                + "\"motives\":[{\"type\":\"ZEMSTA\",\"description\":\"Opis\"}]}]";
        List<Suspect> suspects = new Gson().fromJson(json,
                new com.google.gson.reflect.TypeToken<List<Suspect>>(){}.getType());

        List<Location> locations = loader.loadLocations();
        MurderCase mc = loader.loadMurderCase(suspects, locations);

        assertEquals("Testowy Podejrzany", mc.getKiller());
    }

    @Test
    void loadMurderCase_singleLocation_alwaysPicksThatLocation() {
        List<Suspect> suspects = loader.loadSuspects();
        String locJson = "[{\"id\":\"loc_test\",\"name\":\"Testowa Lokacja\","
                + "\"weapons\":[\"Nóż\"]}]";
        List<Location> locations = new Gson().fromJson(locJson,
                new com.google.gson.reflect.TypeToken<List<Location>>(){}.getType());

        MurderCase mc = loader.loadMurderCase(suspects, locations);

        assertEquals("loc_test", mc.getCrimeScene().getId());
        assertEquals("Nóż", mc.getWeapon());
    }

    // -------------------------------------------------------------------------
    // loadWeaponData
    // -------------------------------------------------------------------------

    @Test
    void loadWeaponData_returnsNonNull() {
        assertNotNull(loader.loadWeaponData());
    }

    // -------------------------------------------------------------------------
    // loadNpcDialogue
    // -------------------------------------------------------------------------

    @Test
    void loadNpcDialogue_knownNpcId_returnsNonNull() {
        // kamilKrakowski → plik dialogi_Kamil.json
        assertNotNull(loader.loadNpcDialogue("kamilKrakowski"));
    }

    @Test
    void loadNpcDialogue_allNpcsFromFile_returnConfigs() {
        List<String> npcIds = loader.loadSuspects().stream()
                .map(Suspect::getId)
                .toList();
        for (String id : npcIds) {
            assertNotNull(loader.loadNpcDialogue(id),
                    "Brak pliku dialogów dla NPC: " + id);
        }
    }

    @Test
    void loadNpcDialogue_unknownNpcId_returnsNull() {
        assertNull(loader.loadNpcDialogue("nieIstniejacyNpc"));
    }
}
