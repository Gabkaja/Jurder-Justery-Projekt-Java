package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import world.Location;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocationFactory {
    private final Gson gson = new Gson();

    public List<Location> loadLocations(String resourcePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Nie znaleziono pliku w resources: " + resourcePath);
                return new ArrayList<>();
            }

            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            // Jedna linijka od Google Gson i plik JSON zamienia się w listę obiektów Java
            return gson.fromJson(reader, new TypeToken<List<Location>>(){}.getType());

        } catch (Exception e) {
            System.err.println("Blad podczas wczytywania lokacji przy uzyciu Gson: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}