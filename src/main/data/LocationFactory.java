package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import world.Location;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationFactory {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Location> loadLocations(String resourcePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Nie znaleziono pliku w resources: " + resourcePath);
                return new ArrayList<>();
            }
            return objectMapper.readValue(inputStream, new TypeReference<List<Location>>() {});
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania lokacji: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}