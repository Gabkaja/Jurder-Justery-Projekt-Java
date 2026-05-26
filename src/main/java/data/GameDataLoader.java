package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import characters.Suspect;
import characters.PlayerCharacter;
import world.Location;
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
    private static final String LOCATIONS_PATH = "lokacje.json";
    private static final String NPC_PATH = "npc.json";
    private static final String PC_PATH = "pc.json";

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

    // Ładowanie sprawy morderstwa
    public MurderCase loadMurderCase(List<Suspect> suspects, List<Location> locations) {
        try {
            // Wyciągamy pierwszą lepszą lokację z listy jako miejsce zbrodni (jeśli lista nie jest pusta)
            Location defaultScene = locations.isEmpty() ? null : locations.get(0);

            // Tworzymy testową sprawę morderstwa
            return new MurderCase("Nieznany", "Noż", null, defaultScene);
        } catch (Exception e) {
            System.err.println("[GameDataLoader] Blad podczas tworzenia MurderCase: " + e.getMessage());
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