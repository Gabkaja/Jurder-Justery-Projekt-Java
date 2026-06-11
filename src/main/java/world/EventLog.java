package world;

import java.util.ArrayList;
import java.util.List;

public class EventLog {

    private final List<String> entries = new ArrayList<>();
    private final List<String> clues = new ArrayList<>();

    public EventLog() {
    }

    public List<String> getEntries() {
        return entries;
    }

    public void addEntry(String entry) {
        this.entries.add(entry);
    }

    public List<String> getClues() {
        return clues;
    }

    public void addClue(String clue) {
        this.clues.add(clue);
    }
}