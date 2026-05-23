package main.engine;

/**
 * Główna pętla gry.
 * W każdej iteracji: renderuje scenę, pobiera input, przekazuje do SceneManager.
 */
public class GameLoop {

    private final GameEngine engine;
    private SceneManager currentScene;
    private final InputHandler inputHandler;

    public GameLoop(GameEngine engine, SceneManager sceneManager) {
        this.engine = engine;
        this.currentScene = sceneManager;
        this.inputHandler = new InputHandler();
    }

    // Uruchamia pętlę. Blokuje wątek do końca gry.
    public void run() {
        while (engine.isRunning()) {
            int optionCount = currentScene.render();
            int choice = inputHandler.readInt(1, optionCount);
            currentScene = currentScene.onChoice(choice);
        }

        inputHandler.close();
    }
}