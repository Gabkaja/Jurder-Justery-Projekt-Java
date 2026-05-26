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
}