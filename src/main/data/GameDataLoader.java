package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import characters.Suspect;
import characters.PlayerCharacter;
import world.Location;
import dialogue.Dialogue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GameDataLoader {
    private final Gson gson = new Gson();

    // Ścieżki do plików JSON schowane w jednym miejscu
    private static final String LOCATIONS_PATH = "locations.json";
    private static final String NPC_PATH = "npc.json";
    private static final String PC_PATH = "pc.json";
    private static final String DIALOGUES_PATH = "dialogues.json";

    // Ładowanie lokacji
    public List<Location> loadLocations() {
        return loadList(LOCATIONS_PATH, new TypeToken<List<Location>>(){});
    }

    // Ładowanie podejrzanych (NPC)
    public List<Suspect> loadNPCs() {
        return loadList(NPC_PATH, new TypeToken<List<Suspect>>(){});
    }

    // Ładowanie postaci gracza (Konfiguracja startowa)
    public List<PlayerCharacter> loadPlayerCharacters() {
        return loadList(PC_PATH, new TypeToken<List<PlayerCharacter>>(){});
    }

    // Ładowanie dialogów
    public List<Dialogue> loadDialogues() {
        return loadList(DIALOGUES_PATH, new TypeToken<List<Dialogue>>(){});
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