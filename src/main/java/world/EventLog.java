package world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventLog {

    private final List<String> entries = new ArrayList<>();
    private final Set<String> uniqueClues = new HashSet<>();

    public EventLog() {
    }

    public List<String> getEntries() {
        return entries;
    }

    public void addEntry(String entry) {
        this.entries.add(entry);
    }

    public List<String> getClues() {
        return new ArrayList<>(uniqueClues);
    }

    public void addClue(String clue) {
        if (uniqueClues.add(clue)) {
            this.entries.add("Znaleziono nowy dowód: " + clue);
        }
    }
}