package main.gameplay;

import main.engine.GameEngine;
import main.engine.SceneManager;

import java.util.List;

public class DifficultySystem extends SceneManager {

    public enum Difficulty {
        EASY("Łatwy"),
        NORMAL("Normalny"),
        HARD("Trudny");

        private final String label;

        Difficulty(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public DifficultySystem(GameEngine engine) {
        super(engine);
    }

    @Override
    public String getNarration() {
        return "=== Poziom trudności ===";
    }

    @Override
    public List<String> getOptions() {
        return List.of(
            Difficulty.EASY.getLabel(),
            Difficulty.NORMAL.getLabel(),
            Difficulty.HARD.getLabel()
        );
    }

    @Override
    public SceneManager onChoice(int choice) {
        Difficulty selected = switch (choice) {
            case 1 -> Difficulty.EASY;
            case 2 -> Difficulty.NORMAL;
            case 3 -> Difficulty.HARD;
            default -> Difficulty.NORMAL;
        };
        engine.setDifficulty(selected);
        return new ExplorationSystem(engine);
    }
}
