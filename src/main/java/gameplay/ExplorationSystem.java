package gameplay;

import java.util.ArrayList;
import java.util.List;
import engine.GameEngine;
import engine.SceneManager;
import world.Location;

public class ExplorationSystem extends SceneManager {

    // Zapamiętujemy poprzednią scenę pokoju, żeby gracz mógł się łatwo wycofać, jeśli się rozmyśli
    private final SceneManager previousScene;

    public ExplorationSystem(GameEngine engine, SceneManager previousScene) {
        super(engine);
        this.previousScene = previousScene;
    }

    @Override
    public String getTitle() {
        return "Nawigacja — Wybór kolejnego pomieszczenia";
    }

    @Override
    public String getNarration() {
        return "Stoisz w: " + engine.getCurrentLocation().getName() + ".\n"
                + "Rozejrzałeś się po przejściach. Widzisz stąd kilka dróg.\n"
                + "Gdzie decydujesz się teraz pójść?";
    }

    @Override
    public List<String> getOptions() {
        List<String> options = new ArrayList<>();

        // 1. Pobieramy listę technicznych ID pokoi połączonych z tym pomieszczeniem
        List<String> adjacentRoomIds = engine.getCurrentLocation().getPassages();

        // 2. Mapujemy techniczne ID na ładne nazwy do menu dla użytkownika
        for (String passageId : adjacentRoomIds) {
            Location loc = findLocationById(passageId);
            if (loc != null) {
                options.add("Przejdź do: " + loc.getName());
            } else {
                options.add("Przejdź do: " + passageId); // Zabezpieczenie awaryjne
            }
        }

        // 3. Na samym końcu dodajemy opcję rezygnacji
        options.add("Rozmyśl się (zostań w tym pokoju)");
        return options;
    }

    @Override
    public SceneManager onChoice(int choice) {
        List<String> adjacentRoomIds = engine.getCurrentLocation().getPassages();

        // Sprawdzamy, czy gracz wybrał któryś z sąsiadujących pokoi
        if (choice >= 1 && choice <= adjacentRoomIds.size()) {
            String targetRoomId = adjacentRoomIds.get(choice - 1);
            Location targetLocation = findLocationById(targetRoomId);

            if (targetLocation != null) {
                // Zmieniamy pozycję gracza w silniku gry
                engine.setCurrentLocation(targetLocation);

                // Odnotowujemy ruch w dzienniku zdarzeń
                if (engine.getEventLog() != null) {
                    engine.getEventLog().addEntry("Przemieszczono się do lokacji: " + targetLocation.getName());
                }

                // Gracz wchodzi do nowego pokoju, więc tworzymy dla niego nową instancję interakcji
                return new SearchSystem(engine);
            }
        }

        // Jeśli gracz wybrał ostatnią opcję (rozmyślenie się), wraca do menu tego samego pokoju
        if (choice == adjacentRoomIds.size() + 1) {
            return previousScene;
        }

        return this;
    }

    // Pomocnicza metoda szukająca pełnego obiektu lokacji na podstawie ID z listy przejść
    private Location findLocationById(String id) {
        if (engine.getLocations() == null) return null;
        return engine.getLocations().stream()
                .filter(l -> l.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}