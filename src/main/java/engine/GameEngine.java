package engine;

import characters.PlayerCharacter;
import characters.Suspect;
import data.GameDataLoader;
import gameplay.DifficultySystem;
import world.Location;
import world.MurderCase;
import world.EventLog;

import java.util.List;

/**
 * Centralny rejestr stanu gry.
 * Przechowuje dane załadowane na starcie i udostępnia je innym systemom.
 */
public class GameEngine {

    // Stan gry
    private PlayerCharacter player;
    private List<Suspect> suspects;
    private List<Location> locations;
    private Location currentLocation;
    private MurderCase murderCase;
    private EventLog eventLog;

    // Podsystemy
    private final SceneManager sceneManager;
    private final GameLoop gameLoop;

    // Poziom trudności
    private DifficultySystem.Difficulty difficulty = DifficultySystem.Difficulty.NORMAL;

    // Flagi
    private boolean running;

    public GameEngine() {
        this.sceneManager = SceneManager.createInitialScene(this);
        this.gameLoop = new GameLoop(this, sceneManager);
        this.eventLog = new EventLog();
    }

    // Inicjalizuje grę: ładuje dane, ustawia stan początkowy, uruchamia pętlę.
    public void start() {
        loadGameData();
        running = true;
        gameLoop.run();
    }

    public void stop() {
        running = false;
    }

    // Ładowanie danych
    private void loadGameData() {
        GameDataLoader loader = new GameDataLoader();
        this.suspects = loader.loadSuspects();
        this.locations = loader.loadLocations();
        this.murderCase = loader.loadMurderCase(suspects, locations);
    }

    // Gettery / settery stanu
    public PlayerCharacter getPlayer() { return player; }
    public void setPlayer(PlayerCharacter player) { this.player = player; }

    public List<Suspect> getSuspects() { return suspects; }

    public List<Location> getLocations() { return locations; }

    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location location) { this.currentLocation = location; }

    public MurderCase getMurderCase() { return murderCase; }

    public EventLog getEventLog() { return eventLog; }

    public boolean isRunning() { return running; }

    public DifficultySystem.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(DifficultySystem.Difficulty difficulty) { this.difficulty = difficulty; }
}