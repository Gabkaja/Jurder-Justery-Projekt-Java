package world;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotiveTest {

    private Motive motive() {
        return new Motive("zemsta", "Zemsta", "Chcial sie odplacic za zdrade");
    }

    // --- Gettery ---

    @Test
    void getId_returnsCorrectId() {
        assertEquals("zemsta", motive().getId());
    }

    @Test
    void getLabel_returnsCorrectLabel() {
        assertEquals("Zemsta", motive().getLabel());
    }

    @Test
    void getDescription_returnsCorrectDescription() {
        assertEquals("Chcial sie odplacic za zdrade", motive().getDescription());
    }

    // --- Rozne wartosci ---

    @Test
    void differentMotives_haveIndependentFields() {
        Motive m1 = new Motive("a", "A", "opis A");
        Motive m2 = new Motive("b", "B", "opis B");
        assertNotEquals(m1.getId(), m2.getId());
        assertNotEquals(m1.getLabel(), m2.getLabel());
        assertNotEquals(m1.getDescription(), m2.getDescription());
    }

    @Test
    void nullValues_areStored() {
        Motive m = new Motive(null, null, null);
        assertNull(m.getId());
        assertNull(m.getLabel());
        assertNull(m.getDescription());
    }

    @Test
    void emptyStrings_areStored() {
        Motive m = new Motive("", "", "");
        assertEquals("", m.getId());
        assertEquals("", m.getLabel());
        assertEquals("", m.getDescription());
    }

    @Test
    void idAndLabel_canBeSameValue() {
        Motive m = new Motive("ambit", "ambit", "Opis ambicji");
        assertEquals(m.getId(), m.getLabel());
    }
}
