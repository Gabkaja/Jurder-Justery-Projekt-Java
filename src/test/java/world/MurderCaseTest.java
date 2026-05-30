package world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MurderCaseTest {

    private Location crimeScene;
    private Motive motive;
    private MurderCase murderCase;

    @BeforeEach
    void setUp() {
        crimeScene = makeLocation("gabinet", "Gabinet");
        motive = new Motive("szantaz", "Szantaz", "Ofiara szantazowala sprawce");
        murderCase = new MurderCase("Jan Kowalski", "Swieczknik", motive, crimeScene);
    }

    // --- Gettery ---

    @Test
    void getKiller_returnsCorrectName() {
        assertEquals("Jan Kowalski", murderCase.getKiller());
    }

    @Test
    void getWeapon_returnsCorrectWeapon() {
        assertEquals("Swieczknik", murderCase.getWeapon());
    }

    @Test
    void getMotive_returnsCorrectMotive() {
        assertSame(motive, murderCase.getMotive());
    }

    @Test
    void getCrimeScene_returnsCorrectLocation() {
        assertSame(crimeScene, murderCase.getCrimeScene());
    }

    // --- Null safety ---

    @Test
    void nullKiller_isStored() {
        MurderCase mc = new MurderCase(null, "Bron", motive, crimeScene);
        assertNull(mc.getKiller());
    }

    @Test
    void nullWeapon_isStored() {
        MurderCase mc = new MurderCase("Ktos", null, motive, crimeScene);
        assertNull(mc.getWeapon());
    }

    @Test
    void nullMotive_isStored() {
        MurderCase mc = new MurderCase("Ktos", "Bron", null, crimeScene);
        assertNull(mc.getMotive());
    }

    @Test
    void nullCrimeScene_isStored() {
        MurderCase mc = new MurderCase("Ktos", "Bron", motive, null);
        assertNull(mc.getCrimeScene());
    }

    // --- Relacja z Motive ---

    @Test
    void getMotive_description_isAccessible() {
        assertEquals("Ofiara szantazowala sprawce", murderCase.getMotive().getDescription());
    }

    @Test
    void getMotive_label_isAccessible() {
        assertEquals("Szantaz", murderCase.getMotive().getLabel());
    }

    // --- Relacja z Location ---

    @Test
    void getCrimeScene_id_isAccessible() {
        assertEquals("gabinet", murderCase.getCrimeScene().getId());
    }

    @Test
    void getCrimeScene_name_isAccessible() {
        assertEquals("Gabinet", murderCase.getCrimeScene().getName());
    }

    // --- Niezaleznosc instancji ---

    @Test
    void twoDifferentCases_haveIndependentKillers() {
        MurderCase mc2 = new MurderCase("Anna Nowak", "Tasak", motive, crimeScene);
        assertNotEquals(murderCase.getKiller(), mc2.getKiller());
    }

    // --- pomocnicza ---

    private Location makeLocation(String id, String name) {
        try {
            Location loc = new Location();
            setField(loc, "id", id);
            setField(loc, "name", name);
            setField(loc, "passages", List.of());
            return loc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(obj, value);
    }
}
