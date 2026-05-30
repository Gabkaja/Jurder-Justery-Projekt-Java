package engine;

import characters.PlayerCharacter;
import characters.Suspect;
import data.GameDataLoader;
import world.Location;
import world.MurderCase;
import world.EventLog;
import world.WeaponData;
import dialogue.DialogueGenerator;
import dialogue.SpeechStyleConfig;

import java.util.List;

public class GameEngine {

    // Stan gry
    private PlayerCharacter player;
    private List<Suspect> suspects;
    private List<Location> locations;
    private WeaponData weaponData;
    private Location currentLocation;
    private MurderCase murderCase;
    private EventLog eventLog;

    private DialogueGenerator dialogueGenerator;

    private final SceneManager sceneManager;
    private final GameLoop gameLoop;

    private Difficulty difficulty = Difficulty.MEDIUM;

    private boolean running;

    public GameEngine() {
        this.sceneManager = SceneManager.createInitialScene(this);
        this.gameLoop = new GameLoop(this, sceneManager);
        this.eventLog = new EventLog();
    }

    public void start() {
        loadGameData();
        running = true;
        gameLoop.run();
    }

    public void stop() {
        running = false;
    }

    private void loadGameData() {
        GameDataLoader loader = new GameDataLoader();
        this.suspects = loader.loadSuspects();
        this.locations = loader.loadLocations();
        this.weaponData = loader.loadWeaponData();
        this.murderCase = loader.loadMurderCase(suspects, locations);

        dialogue.SpeechStyleConfig styles = loader.loadSpeechStyles();
        if (styles != null) {
            this.dialogueGenerator = new dialogue.DialogueGenerator(styles);
        } else {
            System.err.println("[GameEngine] Krytyczny błąd: Nie udało się stworzyć DialogueGenerator (brak stylów mowy)!");
        }

        if (this.suspects != null && !this.suspects.isEmpty()) {
            for (Suspect suspect : this.suspects) {
                var dialogueConfig = loader.loadNpcDialogue(suspect.getId());
                if (dialogueConfig != null) {
                    suspect.setDialogueConfig(dialogueConfig);
                } else {
                    System.err.println("[GameEngine] Ostrzeżenie: Brak pliku dialogowego dla NPC o ID: " + suspect.getId());
                }
            }
        }

        if (this.suspects != null && this.locations != null && !this.locations.isEmpty()) {
            java.util.Random rand = new java.util.Random();
            for (Suspect suspect : this.suspects) {
                Location randomLoc = this.locations.get(rand.nextInt(this.locations.size()));
                suspect.setLocationId(randomLoc.getId());
            }
        }

        if (this.locations != null && !this.locations.isEmpty()) {
            this.currentLocation = this.locations.get(0);
        }
    }
    public DialogueGenerator getDialogueGenerator() {
        return dialogueGenerator;
    }

    public PlayerCharacter getPlayer() { return player; }
    public void setPlayer(PlayerCharacter player) {
        this.player = player;
        if (player != null && player.getDifficulty() != null) {
            try {
                this.difficulty = Difficulty.valueOf(player.getDifficulty().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("[GameEngine] Nieznany poziom trudności: " + player.getDifficulty() + ". Ustawiono domyślny (MEDIUM).");
                this.difficulty = Difficulty.MEDIUM;
            }
        }
    }
    public List<Suspect> getSuspects() { return suspects; }
    public List<Location> getLocations() { return locations; }
    public WeaponData getWeaponData() { return weaponData; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location location) { this.currentLocation = location; }
    public MurderCase getMurderCase() { return murderCase; }
    public EventLog getEventLog() { return eventLog; }
    public boolean isRunning() { return running; }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
}