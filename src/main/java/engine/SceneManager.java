package engine;

import java.util.List;
import ui.TerminalUI;

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

    // Tytuł aktualnej sceny (nagłówek ekranu).
    public abstract String getTitle();

    // Tekst kontekstowy aktualnej sceny.
    public abstract String getNarration();

    // Lista opcji menu (w kolejności od 1).
    public abstract List<String> getOptions();

    // Obsługuje wybór opcji i zwraca następną scenę.
    public abstract SceneManager onChoice(int choice);

    // Wspólne renderowanie: czyści ekran, wypisuje nagłówek, tekst i opcje, zwraca liczbę opcji.
    public final int render() {
        List<String> options = getOptions();
        TerminalUI.showLocationScreen(getTitle(), getNarration(), options);
        return options.size();
    }

    // TODO: usunąć, to wszystko tymczasowe, dopóki nie mamy modułu world
    public static class MainMenuScene extends SceneManager {

        public MainMenuScene(GameEngine engine) {
            super(engine);
        }

        @Override
        public String getTitle() {
            return "JURDER MYSTERY";
        }

        @Override
        public String getNarration() {
            return "";
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
        public String getTitle() {
            return "Wybór postaci";
        }

        @Override
        public String getNarration() {
            return "";
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
        public String getTitle() {
            return "Poziom trudności";
        }

        @Override
        public String getNarration() {
            return "";
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
        public String getTitle() {
            return title;
        }

        @Override
        public String getNarration() {
            return message;
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