package engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyTest {

    @Test
    void enumHasFourValues() {
        assertEquals(4, Difficulty.values().length);
    }

    @Test
    void easyLabel_isLaatwy() {
        assertEquals("Łatwy", Difficulty.EASY.getLabel());
    }

    @Test
    void mediumLabel_isNormalny() {
        assertEquals("Normalny", Difficulty.MEDIUM.getLabel());
    }

    @Test
    void hardLabel_isTrudny() {
        assertEquals("Trudny", Difficulty.HARD.getLabel());
    }

    @Test
    void veryHardLabel_isBardzoTrudny() {
        assertEquals("Bardzo Trudny", Difficulty.VERY_HARD.getLabel());
    }

    @Test
    void valueOf_easy_returnsEasy() {
        assertEquals(Difficulty.EASY, Difficulty.valueOf("EASY"));
    }

    @Test
    void valueOf_unknownName_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Difficulty.valueOf("UNKNOWN"));
    }

    @Test
    void ordinalOrder_isCorrect() {
        assertTrue(Difficulty.EASY.ordinal() < Difficulty.MEDIUM.ordinal());
        assertTrue(Difficulty.MEDIUM.ordinal() < Difficulty.HARD.ordinal());
        assertTrue(Difficulty.HARD.ordinal() < Difficulty.VERY_HARD.ordinal());
    }
}
