package engine;

import characters.PlayerCharacter;
import data.GameDataLoader;
import gameplay.DifficultySystem;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterSelectScene extends SceneManager {

    private final List<PlayerCharacter> characters;

    public CharacterSelectScene(GameEngine engine) {
        super(engine);
        this.characters = new GameDataLoader().loadPlayerCharacters();
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
        return characters.stream()
                .map(pc -> pc.getName() + " — " + pc.getTitle())
                .collect(Collectors.toList());
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (choice >= 1 && choice <= characters.size()) {
            engine.setPlayer(characters.get(choice - 1));
        }
        return new DifficultySystem(engine);
    }
}
