package world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvidenceTest {

    private Evidence makeEvidence(boolean isTrueClue) {
        return new Evidence("e1", "Slad buta", "Odcisk podeszwy nr 42", Evidence.Type.LOKACJA, isTrueClue);
    }

    // --- Gettery ---

    @Test
    void getId_returnsCorrectId() {
        assertEquals("e1", makeEvidence(true).getId());
    }

    @Test
    void getName_returnsCorrectName() {
        assertEquals("Slad buta", makeEvidence(true).getName());
    }

    @Test
    void getDescription_returnsCorrectDescription() {
        assertEquals("Odcisk podeszwy nr 42", makeEvidence(true).getDescription());
    }

    @Test
    void getType_returnsCorrectType() {
        assertEquals(Evidence.Type.LOKACJA, makeEvidence(true).getType());
    }

    @Test
    void isTrueClue_trueValue_returnsTrue() {
        assertTrue(makeEvidence(true).isTrueClue());
    }

    @Test
    void isTrueClue_falseValue_returnsFalse() {
        assertFalse(makeEvidence(false).isTrueClue());
    }

    // --- Enum Type ---

    @Test
    void typeEnum_hasFourValues() {
        assertEquals(4, Evidence.Type.values().length);
    }

    @Test
    void typeEnum_containsBron() {
        assertDoesNotThrow(() -> Evidence.Type.valueOf("BROŃ"));
    }

    @Test
    void typeEnum_containsPodejrzany() {
        assertDoesNotThrow(() -> Evidence.Type.valueOf("PODEJRZANY"));
    }

    @Test
    void typeEnum_containsMotyw() {
        assertDoesNotThrow(() -> Evidence.Type.valueOf("MOTYW"));
    }

    // --- Rozne typy ---

    @Test
    void evidenceWithTypeBron_getTypeReturnsBron() {
        Evidence e = new Evidence("x", "Noz", "Ostry noz", Evidence.Type.BROŃ, true);
        assertEquals(Evidence.Type.BROŃ, e.getType());
    }

    @Test
    void evidenceWithTypeMotyw_getTypeReturnsMotyw() {
        Evidence e = new Evidence("x", "Zazdrosc", "Motyw zazdrości", Evidence.Type.MOTYW, false);
        assertEquals(Evidence.Type.MOTYW, e.getType());
        assertFalse(e.isTrueClue());
    }
}
