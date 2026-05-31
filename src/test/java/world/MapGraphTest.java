package world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapGraphTest {

    private Location sypialnia;
    private Location gabinet;
    private Location kuchnia;
    private MapGraph graph;

    @BeforeEach
    void setUp() {
        sypialnia = makeLocation("sypialnia", "Sypialnia", List.of("gabinet", "kuchnia"));
        gabinet   = makeLocation("gabinet",   "Gabinet",   List.of("sypialnia"));
        kuchnia   = makeLocation("kuchnia",   "Kuchnia",   List.of("sypialnia"));

        graph = new MapGraph(Map.of(
                "sypialnia", sypialnia,
                "gabinet",   gabinet,
                "kuchnia",   kuchnia
        ));
    }

    // --- getLocationById ---

    @Test
    void getLocationById_existingId_returnsLocation() {
        assertSame(sypialnia, graph.getLocationById("sypialnia"));
    }

    @Test
    void getLocationById_anotherExistingId_returnsCorrectLocation() {
        assertSame(gabinet, graph.getLocationById("gabinet"));
    }

    @Test
    void getLocationById_unknownId_returnsNull() {
        assertNull(graph.getLocationById("nieistnieje"));
    }

    @Test
    void getLocationById_null_throwsOrReturnsNull() {
        // Map.of() nie dopuszcza kluczy null – oczekujemy NPE lub null
        try {
            assertNull(graph.getLocationById(null));
        } catch (NullPointerException e) {
            // akceptowalne – implementacja nie obsługuje null
        }
    }

    // --- getPassages ---

    @Test
    void getPassages_locationWithPassages_returnsPassageList() {
        List<String> passages = graph.getPassages("sypialnia");
        assertEquals(2, passages.size());
        assertTrue(passages.contains("gabinet"));
        assertTrue(passages.contains("kuchnia"));
    }

    @Test
    void getPassages_locationWithOnePassage_returnsOneElement() {
        List<String> passages = graph.getPassages("gabinet");
        assertEquals(1, passages.size());
        assertTrue(passages.contains("sypialnia"));
    }

    @Test
    void getPassages_unknownId_returnsEmptyList() {
        assertTrue(graph.getPassages("nieistnieje").isEmpty());
    }

    @Test
    void getPassages_locationWithNullPassages_returnsEmptyList() {
        Location izolowana = makeLocation("izolowana", "Izolowana", null);
        MapGraph g = new MapGraph(Map.of("izolowana", izolowana));
        assertTrue(g.getPassages("izolowana").isEmpty());
    }

    // --- areConnected ---

    @Test
    void areConnected_directPassage_returnsTrue() {
        assertTrue(graph.areConnected("sypialnia", "gabinet"));
    }

    @Test
    void areConnected_secondDirectPassage_returnsTrue() {
        assertTrue(graph.areConnected("sypialnia", "kuchnia"));
    }

    @Test
    void areConnected_reverseDirection_returnsTrue() {
        assertTrue(graph.areConnected("gabinet", "sypialnia"));
    }

    @Test
    void areConnected_noDirectPassage_returnsFalse() {
        // gabinet nie ma przejscia bezposrednio do kuchni
        assertFalse(graph.areConnected("gabinet", "kuchnia"));
    }

    @Test
    void areConnected_unknownSource_returnsFalse() {
        assertFalse(graph.areConnected("nieistnieje", "gabinet"));
    }

    @Test
    void areConnected_unknownTarget_returnsFalse() {
        assertFalse(graph.areConnected("sypialnia", "nieistnieje"));
    }

    @Test
    void areConnected_sameRoom_returnsFalse() {
        // sypialnia nie ma siebie na liscie przejsc
        assertFalse(graph.areConnected("sypialnia", "sypialnia"));
    }

    // --- Pusty graf ---

    @Test
    void emptyGraph_getLocationById_returnsNull() {
        MapGraph empty = new MapGraph(Map.of());
        assertNull(empty.getLocationById("x"));
    }

    @Test
    void emptyGraph_getPassages_returnsEmptyList() {
        MapGraph empty = new MapGraph(Map.of());
        assertTrue(empty.getPassages("x").isEmpty());
    }

    @Test
    void emptyGraph_areConnected_returnsFalse() {
        MapGraph empty = new MapGraph(Map.of());
        assertFalse(empty.areConnected("a", "b"));
    }

    // --- pomocnicza ---

    private Location makeLocation(String id, String name, List<String> passages) {
        try {
            Location loc = new Location();
            setField(loc, "id", id);
            setField(loc, "name", name);
            setField(loc, "passages", passages);
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
