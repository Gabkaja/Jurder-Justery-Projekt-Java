package engine;

import java.util.List;

public class MainMenuScene extends SceneManager {

    public MainMenuScene(GameEngine engine) {
        super(engine);
    }

    @Override
    public String getTitle() {
        return "JURDER JYSTERY";
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
