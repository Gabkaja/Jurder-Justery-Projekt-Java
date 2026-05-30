package world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventLogTest {

    private EventLog log;

    @BeforeEach
    void setUp() {
        log = new EventLog();
    }

    // --- Stan poczatkowy ---

    @Test
    void initialEntries_isEmpty() {
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    void initialClues_isEmpty() {
        assertTrue(log.getClues().isEmpty());
    }

    // --- addEntry ---

    @Test
    void addEntry_singleEntry_isRetrievable() {
        log.addEntry("Wejscie do biblioteki");
        assertEquals(List.of("Wejscie do biblioteki"), log.getEntries());
    }

    @Test
    void addEntry_multipleEntries_preservesOrder() {
        log.addEntry("A");
        log.addEntry("B");
        log.addEntry("C");
        assertEquals(List.of("A", "B", "C"), log.getEntries());
    }

    @Test
    void addEntry_doesNotAffectClues() {
        log.addEntry("zdarzenie");
        assertTrue(log.getClues().isEmpty());
    }

    @Test
    void addEntry_emptyString_isStored() {
        log.addEntry("");
        assertEquals(1, log.getEntries().size());
    }

    // --- addClue ---

    @Test
    void addClue_singleClue_isRetrievable() {
        log.addClue("Slad krwi");
        assertEquals(List.of("Slad krwi"), log.getClues());
    }

    @Test
    void addClue_multipleClues_preservesOrder() {
        log.addClue("Poszlaka 1");
        log.addClue("Poszlaka 2");
        assertEquals(List.of("Poszlaka 1", "Poszlaka 2"), log.getClues());
    }

    @Test
    void addClue_doesNotAffectEntries() {
        log.addClue("dowod");
        assertTrue(log.getEntries().isEmpty());
    }

    @Test
    void addClue_emptyString_isStored() {
        log.addClue("");
        assertEquals(1, log.getClues().size());
    }

    // --- Niezaleznosc list ---

    @Test
    void entriesAndClues_areIndependent() {
        log.addEntry("E1");
        log.addClue("C1");
        assertEquals(1, log.getEntries().size());
        assertEquals(1, log.getClues().size());
    }

    @Test
    void addingManyEntries_doesNotChangeCluesSize() {
        for (int i = 0; i < 10; i++) {
            log.addEntry("e" + i);
        }
        assertEquals(0, log.getClues().size());
    }

    @Test
    void addingManyClues_doesNotChangeEntriesSize() {
        for (int i = 0; i < 10; i++) {
            log.addClue("c" + i);
        }
        assertEquals(0, log.getEntries().size());
    }
}
