package characters;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotiveOptionTest {

    private final Gson gson = new Gson();

    @Test
    void defaultConstructor_createsObjectWithNullFields() {
        MotiveOption motiveOption = new MotiveOption();
        assertNull(motiveOption.getType());
        assertNull(motiveOption.getDescription());
    }

    @Test
    void getType_returnsCorrectValue() {
        String json = "{\"type\":\"ZAZDROSC\",\"description\":\"Zazdrosny o majątek\"}";
        MotiveOption motiveOption = gson.fromJson(json, MotiveOption.class);
        assertEquals("ZAZDROSC", motiveOption.getType());
    }

    @Test
    void getDescription_returnsCorrectValue() {
        String json = "{\"type\":\"ZEMSTA\",\"description\":\"Chciał zemsty za krzywdy\"}";
        MotiveOption motiveOption = gson.fromJson(json, MotiveOption.class);
        assertEquals("Chciał zemsty za krzywdy", motiveOption.getDescription());
    }

    @Test
    void toString_containsTypeAndDescription() {
        String json = "{\"type\":\"CHCIWOSC\",\"description\":\"Chciwy na spadek\"}";
        MotiveOption motiveOption = gson.fromJson(json, MotiveOption.class);
        String result = motiveOption.toString();
        assertTrue(result.contains("CHCIWOSC"));
        assertTrue(result.contains("Chciwy na spadek"));
    }

    @Test
    void toString_withNullFields_doesNotThrow() {
        MotiveOption motiveOption = new MotiveOption();
        assertDoesNotThrow(motiveOption::toString);
    }
}
