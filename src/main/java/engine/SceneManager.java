package engine;

import java.util.List;

/**
 * Bazowa klasa sceny.
 * Każda akcja w grze ma ten sam schemat: tekst -> opcje -> wybór -> przejście.
 * Konkretne systemy (np. dialog, eksploracja) powinny dziedziczyć po tej klasie.
 */
public abstract class SceneManager {

    protected final GameEngine engine;

    protected SceneManager(GameEngine engine) {
        this.engine = engine;
    }

    // Tworzy scenę startową.
    public static SceneManager createInitialScene(GameEngine engine) {
        return new MainMenuScene(engine);
    }

    // Tekst kontekstowy aktualnej sceny.
    public abstract String getNarration();

    // Lista opcji menu (w kolejności od 1).
    public abstract List<String> getOptions();

    // Obsługuje wybór opcji i zwraca następną scenę.
    public abstract SceneManager onChoice(int choice);

    // TODO: podpiąć moduł ui, kiedy ten będzie gotowy
    // Wspólne renderowanie: wypisz tekst i opcje, zwróć liczbę opcji.
    public final int render() {
        System.out.println(getNarration());

        List<String> options = getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        return options.size();
    }

    // TODO: usunąć, to wszystko tymczasowe, dopóki nie mamy modułu world
    public static class MainMenuScene extends SceneManager {

        public MainMenuScene(GameEngine engine) {
            super(engine);
        }

        @Override
        public String getNarration() {
            return "=== JURDER JYSTERY ===";
        }

        @Override
        public List<String> getOptions() {
            return List.of("Nowa gra", "Wyjście");
        }

        @Override
        public SceneManager onChoice(int choice) {
            if (choice == 1) {
                return new CharacterSelectScene(engine);
            }

            engine.stop();
            return this;
        }
    }

    // TODO: usunąć, to wszystko tymczasowe, dopóki nie mamy modułu world
    // docelowo powinno pobierać informacje o PC z modułu characters
    public static class CharacterSelectScene extends SceneManager {

        public CharacterSelectScene(GameEngine engine) {
            super(engine);
        }

        @Override
        public String getNarration() {
            return "=== Wybór postaci ===";
        }

        @Override
        public List<String> getOptions() {
            return List.of(
                "Doświadczony detektyw",
                "Dziennikarka śledcza",
                "Młody prywatny detektyw",
                "Technik śledczy"
            );
        }

        @Override
        public SceneManager onChoice(int choice) {
            // TODO: podmiana na fabrykę postaci po podłączeniu danych z JSON.
            return new DifficultySelectScene(engine);
        }
    }

    // TODO: usunąć, to wszystko tymczasowe, dopóki nie mamy modułu world
    public static class DifficultySelectScene extends SceneManager {

        public DifficultySelectScene(GameEngine engine) {
            super(engine);
        }

        @Override
        public String getNarration() {
            return "=== Poziom trudności ===";
        }

        @Override
        public List<String> getOptions() {
            return List.of("Łatwy", "Normalny", "Trudny");
        }

        @Override
        public SceneManager onChoice(int choice) {
            return new gameplay.DifficultySystem(engine);
        }
    }

    // TODO: usunąć, to wszystko tymczasowe, dopóki nie mamy modułu world
    public static class PlaceholderScene extends SceneManager {

        private final String title;
        private final String message;

        public PlaceholderScene(GameEngine engine, String title, String message) {
            super(engine);
            this.title = title;
            this.message = message;
        }

        @Override
        public String getNarration() {
            return "=== " + title + " ===\n" + message;
        }

        @Override
        public List<String> getOptions() {
            return List.of("Wróć do menu głównego", "Wyjście z gry");
        }

        @Override
        public SceneManager onChoice(int choice) {
            if (choice == 1) {
                return new MainMenuScene(engine);
            }

            engine.stop();
            return this;
        }
    }
}