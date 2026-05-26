package gameplay;

import characters.Suspect;
import engine.GameEngine;
import engine.SceneManager;
import world.EventLog;
import world.MurderCase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ekran dziennika śledztwa.
 * Pozwala graczowi przeglądać:
 *   1. Dziennik zdarzeń   — akcje podjęte podczas gry (stan dynamiczny)
 *   2. Lista poszlak      — znalezione dowody (stan dynamiczny)
 *   3. Lista postaci      — NPC ładowani z npc.json przez GameEngine
 *   4. Lista lokacji      — lokacje ładowane z lokacje.json przez GameEngine
 *   5. Powrót             — wraca do sceny, z której wywołano InvestigationSystem
 */
public class InvestigationSystem extends SceneManager {

    private final SceneManager previousScene;

    public InvestigationSystem(GameEngine engine, SceneManager previousScene) {
        super(engine);
        this.previousScene = previousScene;
    }

    @Override
    public String getNarration() {
        return "=== Dziennik śledztwa ===\nCo chcesz sprawdzić?";
    }

    @Override
    public List<String> getOptions() {
        return List.of(
            "Dziennik zdarzeń",
            "Lista poszlak",
            "Lista postaci",
            "Lista lokacji",
            "Powrót"
        );
    }

    @Override
    public SceneManager onChoice(int choice) {
        return switch (choice) {
            case 1 -> new ListScene(engine, this, "Dziennik zdarzeń", getEventLogEntries());
            case 2 -> new ListScene(engine, this, "Lista poszlak",   getClues());
            case 3 -> new ListScene(engine, this, "Lista postaci",   getNpcNames());
            case 4 -> new ListScene(engine, this, "Lista lokacji",   getLocationNames());
            case 5 -> previousScene;
            default -> this;
        };
    }

    // TODO: nie ma jeszcze modułu data, więc nic tutaj nie działa
    private List<String> getEventLogEntries() {
        EventLog log = engine.getEventLog();
        if (log == null) return List.of("(Dziennik jest pusty)");
        List<String> entries = log.getEntries();
        if (entries.isEmpty()) return List.of("(Dziennik jest pusty)");
        return entries;
    }

    private List<String> getClues() {
        MurderCase murderCase = engine.getMurderCase();
        if (murderCase == null) return List.of("(Brak znalezionych poszlak)");
        List<String> evidence = murderCase.getFoundEvidence();
        if (evidence.isEmpty()) return List.of("(Brak znalezionych poszlak)");
        return evidence;
    }

    private List<String> getNpcNames() {
        List<Suspect> suspects = engine.getSuspects();
        if (suspects == null || suspects.isEmpty()) return List.of("(Brak danych o postaciach)");
        return suspects.stream()
            .map(Suspect::toString)   // "Imię Nazwisko — Tytuł"
            .collect(Collectors.toList());
    }

    private List<String> getLocationNames() {
        var locations = engine.getLocations();
        if (locations == null || locations.isEmpty()) return List.of("(Brak danych o lokacjach)");
        return locations.stream()
            .map(Object::toString)   // Location.toString() zwraca nazwę
            .collect(Collectors.toList());
    }

    private static class ListScene extends SceneManager {

        private final SceneManager returnScene;
        private final String title;
        private final List<String> items;

        ListScene(GameEngine engine, SceneManager returnScene, String title, List<String> items) {
            super(engine);
            this.returnScene = returnScene;
            this.title = title;
            this.items = items;
        }

        @Override
        public String getNarration() {
            StringBuilder sb = new StringBuilder("=== ").append(title).append(" ===\n");
            for (int i = 0; i < items.size(); i++) {
                sb.append(i + 1).append(". ").append(items.get(i)).append('\n');
            }
            return sb.toString().stripTrailing();
        }

        @Override
        public List<String> getOptions() {
            return List.of("Powrót");
        }

        @Override
        public SceneManager onChoice(int choice) {
            return returnScene;
        }
    }
}
