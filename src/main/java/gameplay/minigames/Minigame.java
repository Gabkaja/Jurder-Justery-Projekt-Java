package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;

public abstract class Minigame extends SceneManager {

    protected final SearchSystem returnScene;
    protected boolean gameOver = false;
    protected boolean won = false;

    public Minigame(GameEngine engine, SearchSystem returnScene) {
        super(engine);
        this.returnScene = returnScene;
    }

    protected SceneManager endMinigame(String successPrefix, String failureMessage) {
        return endMinigame(successPrefix, failureMessage, "RANDOM");
    }

    protected SceneManager endMinigame(String successPrefix, String failureMessage, String clueCategory) {
        if (won) {
            String clue = returnScene.generateSpecificClue(engine.getCurrentLocation(), clueCategory);
            engine.getEventLog().addClue(clue);
            returnScene.setMinigameResult(true, successPrefix + " " + clue);
        } else {
            returnScene.setMinigameResult(false, failureMessage);
        }
        return returnScene;
    }
}