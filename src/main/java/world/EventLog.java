package world;

import java.util.ArrayList;
import java.util.List;

/**
 * Tymczasowa atrapa klasy EventLog, żeby gra się uruchomiła.
 */
public class EventLog {

    // Lista przechowująca wpisy (na razie będzie pusta)
    private final List<String> entries = new ArrayList<>();

    public EventLog() {
        // Konstruktor domyślny, nic nie musi robić
    }

    // Metoda, której szuka interfejs InvestigationSystem
    public List<String> getEntries() {
        return entries;
    }

    // Metoda do dodawania nowych zdarzeń (przyda się na przyszłość)
    public void addEntry(String entry) {
        this.entries.add(entry);
    }

}