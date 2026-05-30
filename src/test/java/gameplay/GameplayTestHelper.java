package gameplay;

import engine.GameEngine;
import world.Location;
import world.Motive;
import world.MurderCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Narzędzia testowe wspólne dla całego modułu gameplay.
 */
public class GameplayTestHelper {

    /** Tworzy GameEngine bez blokowania System.in. */
    public static GameEngine makeEngine() {
        InputStream stream = new ByteArrayInputStream("2\n".getBytes(StandardCharsets.UTF_8));
        InputStream original = System.in;
        System.setIn(stream);
        GameEngine engine = new GameEngine();
        System.setIn(original);
        return engine;
    }

    /**
     * Tworzy GameEngine z wypełnionymi: currentLocation ("sypialnia"),
     * locations (sypialnia + gabinet), murderCase (killer = "KAMIL").
     */
    public static GameEngine makeFullEngine() {
        GameEngine engine = makeEngine();
        Location loc   = makeLocation("sypialnia", "Sypialnia",  List.of("gabinet"));
        Location crime = makeLocation("gabinet",   "Gabinet",    List.of("sypialnia"));
        Motive motive  = new Motive("zemsta", "Zemsta", "Ochrona tajemnicy rodzinnej");
        MurderCase mc  = new MurderCase("KAMIL", "Nóż kuchenny", motive, crime);
        setField(engine, "currentLocation", loc);
        setField(engine, "locations", List.of(loc, crime));
        setField(engine, "murderCase", mc);
        return engine;
    }

    /** Tworzy Location za pomocą refleksji (brak publicznego konstruktora z parametrami). */
    public static Location makeLocation(String id, String name, List<String> passages) {
        Location loc = new Location();
        setField(loc, "id", id);
        setField(loc, "name", name);
        setField(loc, "passages", passages);
        return loc;
    }

    /** Ustawia prywatne pole obiektu. Przeszukuje też klasy nadrzędne. */
    public static void setField(Object obj, String fieldName, Object value) {
        try {
            Field f = findField(obj.getClass(), fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /** Odczytuje prywatne pole obiektu. Przeszukuje też klasy nadrzędne. */
    @SuppressWarnings("unchecked")
    public static <T> T getField(Object obj, String fieldName) {
        try {
            Field f = findField(obj.getClass(), fieldName);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), name);
            }
            throw e;
        }
    }
}
