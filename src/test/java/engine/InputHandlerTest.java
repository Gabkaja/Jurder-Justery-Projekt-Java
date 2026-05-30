package engine;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    private InputHandler handlerFrom(String input) {
        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        InputStream original = System.in;
        System.setIn(stream);
        InputHandler handler = new InputHandler();
        System.setIn(original);
        return handler;
    }

    // --- readInt ---

    @Test
    void readInt_validInputInRange_returnsValue() {
        InputHandler handler = handlerFrom("3\n");
        assertEquals(3, handler.readInt(1, 5));
        handler.close();
    }

    @Test
    void readInt_exactMinimum_returnsMin() {
        InputHandler handler = handlerFrom("1\n");
        assertEquals(1, handler.readInt(1, 3));
        handler.close();
    }

    @Test
    void readInt_exactMaximum_returnsMax() {
        InputHandler handler = handlerFrom("3\n");
        assertEquals(3, handler.readInt(1, 3));
        handler.close();
    }

    @Test
    void readInt_outOfRangeThenValid_returnsValidValue() {
        // First line out of range, second is valid
        InputHandler handler = handlerFrom("9\n2\n");
        assertEquals(2, handler.readInt(1, 3));
        handler.close();
    }

    @Test
    void readInt_nonNumericThenValid_returnsValidValue() {
        InputHandler handler = handlerFrom("abc\n2\n");
        assertEquals(2, handler.readInt(1, 3));
        handler.close();
    }

    @Test
    void readInt_emptyLineThenValid_returnsValidValue() {
        InputHandler handler = handlerFrom("\n2\n");
        assertEquals(2, handler.readInt(1, 3));
        handler.close();
    }

    @Test
    void readInt_negativeOutOfRangeThenValid_returnsValid() {
        InputHandler handler = handlerFrom("-5\n1\n");
        assertEquals(1, handler.readInt(1, 3));
        handler.close();
    }

    // --- readLine ---

    @Test
    void readLine_returnsTrimmmedLine() {
        InputHandler handler = handlerFrom("  hello world  \n");
        assertEquals("hello world", handler.readLine());
        handler.close();
    }

    @Test
    void readLine_emptyLine_returnsEmptyString() {
        InputHandler handler = handlerFrom("\n");
        assertEquals("", handler.readLine());
        handler.close();
    }

    // --- waitForEnter ---

    @Test
    void waitForEnter_consumesLine_allowsSubsequentReadLine() {
        InputHandler handler = handlerFrom("\nnext\n");
        handler.waitForEnter();
        assertEquals("next", handler.readLine());
        handler.close();
    }
}
