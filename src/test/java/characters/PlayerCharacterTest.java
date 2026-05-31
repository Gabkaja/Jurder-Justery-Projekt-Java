package characters;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCharacterTest {

    private final Gson gson = new Gson();

    @Test
    void defaultConstructor_createsObjectWithNullFields() {
        PlayerCharacter pc = new PlayerCharacter();
        assertNull(pc.getId());
        assertNull(pc.getName());
        assertNull(pc.getTitle());
        assertNull(pc.getDifficulty());
        assertNull(pc.getDescription());
        assertNull(pc.getSpecialSkills());
    }

    @Test
    void getId_returnsCorrectValue() {
        String json = "{\"id\":\"det_kowalski\",\"name\":\"Jan Kowalski\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        assertEquals("det_kowalski", pc.getId());
    }

    @Test
    void getName_returnsCorrectValue() {
        String json = "{\"id\":\"det_kowalski\",\"name\":\"Jan Kowalski\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        assertEquals("Jan Kowalski", pc.getName());
    }

    @Test
    void getTitle_returnsCorrectValue() {
        String json = "{\"title\":\"Detektyw\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        assertEquals("Detektyw", pc.getTitle());
    }

    @Test
    void getDifficulty_returnsCorrectValue() {
        String json = "{\"difficulty\":\"TRUDNY\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        assertEquals("TRUDNY", pc.getDifficulty());
    }

    @Test
    void getDescription_returnsCorrectValue() {
        String json = "{\"description\":\"Doświadczony śledczy\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        assertEquals("Doświadczony śledczy", pc.getDescription());
    }

    @Test
    void getSpecialSkills_returnsCorrectList() {
        String json = "{\"specialSkills\":[\"Analiza poszlak\",\"Czytanie mowy ciała\"]}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        List<String> skills = pc.getSpecialSkills();
        assertNotNull(skills);
        assertEquals(2, skills.size());
        assertEquals("Analiza poszlak", skills.get(0));
        assertEquals("Czytanie mowy ciała", skills.get(1));
    }

    @Test
    void toString_containsIdNameAndDifficulty() {
        String json = "{\"id\":\"det_kowalski\",\"name\":\"Jan Kowalski\",\"difficulty\":\"LATWY\"}";
        PlayerCharacter pc = gson.fromJson(json, PlayerCharacter.class);
        String result = pc.toString();
        assertTrue(result.contains("det_kowalski"));
        assertTrue(result.contains("Jan Kowalski"));
        assertTrue(result.contains("LATWY"));
    }

    @Test
    void toString_withNullFields_doesNotThrow() {
        PlayerCharacter pc = new PlayerCharacter();
        assertDoesNotThrow(pc::toString);
    }
}
