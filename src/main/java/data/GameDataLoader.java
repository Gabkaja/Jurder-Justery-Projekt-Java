package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import characters.Suspect;
import characters.PlayerCharacter;
import world.Location;
import world.Motive;
import world.MurderCase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameDataLoader {
    private final Gson gson = new Gson();

    // Ścieżki do plików JSON schowane w jednym miejscu
    // Ścieżki dopasowane dokładnie do Twojej struktury w resources
    private static final String LOCATIONS_PATH = "static/lokacje.json";
    private static final String NPC_PATH = "static/npc.json";
    private static final String PC_PATH = "static/pc.json";
    private static final String WEAPONS_PATH = "static/narzedzia_zbrodni.json";
    private static final String STYLES_PATH = "static/dialogi/system/speech_styles.json";

    // Ładowanie lokacji
    public List<Location> loadLocations() {
        return loadList(LOCATIONS_PATH, new TypeToken<List<Location>>(){});
    }

    // Ładowanie podejrzanych
    public List<Suspect> loadSuspects() {
        return loadList(NPC_PATH, new TypeToken<List<Suspect>>(){});
    }

    // Ładowanie postaci gracza (Konfiguracja startowa)
    public List<PlayerCharacter> loadPlayerCharacters() {
        return loadList(PC_PATH, new TypeToken<List<PlayerCharacter>>(){});
    }

    // Ładowanie i losowanie sprawy morderstwa
    public MurderCase loadMurderCase(List<Suspect> suspects, List<Location> locations) {
        if (suspects == null || suspects.isEmpty() || locations == null || locations.isEmpty()) {
            System.err.println("[GameDataLoader] Brakuje danych do wygenerowania sprawy morderstwa!");
            return null;
        }

        java.util.Random random = new java.util.Random();

        // 1. GDZIE: Najpierw losujemy miejsce zbrodni
        Location crimeScene = locations.get(random.nextInt(locations.size()));

        // 2. CZYM: Losujemy broń wyłącznie z listy przypisanej do wylosowanej lokacji
        List<String> availableWeapons = crimeScene.getWeapons();
        String weapon = "Nieznane narzędzie";

        if (availableWeapons != null && !availableWeapons.isEmpty()) {
            weapon = availableWeapons.get(random.nextInt(availableWeapons.size()));
        } else {
            System.err.println("[GameDataLoader] Blad: Lokacja " + crimeScene.getName() + " nie ma przypisanych broni!");
        }

        // 3. KTO: Losujemy mordercę
        Suspect killer = suspects.get(random.nextInt(suspects.size()));

        // 4. DLACZEGO: Losujemy motyw należący do tego konkretnego mordercy
        Motive selectedMotive = null;
        if (killer.getMotives() != null && !killer.getMotives().isEmpty()) {
            // Wyciągamy losowy motyw z puli podejrzanego
            var chosenOption = killer.getMotives().get(random.nextInt(killer.getMotives().size()));

            // Zamieniamy na obiekt Motive (ID z typu, etykieta z typu, oryginalny opis z NPC)
            selectedMotive = new Motive(chosenOption.getType(), chosenOption.getType(), chosenOption.getDescription());
        }

        return new MurderCase(killer.getName(), weapon, selectedMotive, crimeScene);
    }

    public world.WeaponData loadWeaponData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(WEAPONS_PATH)) {
            if (inputStream == null) {
                System.err.println("[GameDataLoader] Nie znaleziono pliku: " + WEAPONS_PATH);
                return null;
            }
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(reader, world.WeaponData.class);
        } catch (Exception e) {
            System.err.println("[GameDataLoader] Blad podczas parsowania " + WEAPONS_PATH + ": " + e.getMessage());
            return null;
        }
    }

    public dialogue.SpeechStyleConfig loadSpeechStyles() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(STYLES_PATH)) {
            if (inputStream == null) {
                System.err.println("[GameDataLoader] Nie znaleziono pliku stylów: " + STYLES_PATH);
                return null;
            }
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(reader, dialogue.SpeechStyleConfig.class);
        } catch (Exception e) {
            System.err.println("[GameDataLoader] Blad podczas parsowania stylów mowy: " + e.getMessage());
            return null;
        }
    }

    public dialogue.NpcDialogueConfig loadNpcDialogue(String npcId) {
        String firstName = npcId.split("(?=[A-Z])")[0];

        String capitalizedName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

        String path = "static/dialogi/npc/dialogi_" + capitalizedName + ".json";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                System.err.println("[GameDataLoader] Nie znaleziono pliku dialogów dla: " + npcId + " pod ścieżką " + path);
                return null;
            }
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(reader, dialogue.NpcDialogueConfig.class);
        } catch (Exception e) {
            System.err.println("[GameDataLoader] Blad podczas parsowania dialogu NPC (" + npcId + "): " + e.getMessage());
            return null;
        }
    }
    private <T> List<T> loadList(String resourcePath, TypeToken<List<T>> typeToken) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("[GameDataLoader] Nie znaleziono pliku: " + resourcePath);
                return new ArrayList<>();
            }
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(reader, typeToken.getType());
        } catch (Exception e) {
            System.err.println("[GameDataLoader] Blad podczas parsowania " + resourcePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}