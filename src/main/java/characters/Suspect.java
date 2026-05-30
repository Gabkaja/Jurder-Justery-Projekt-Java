package characters;

import dialogue.NpcDialogueConfig;
import java.util.List;

public class Suspect {
    private String id;
    private String name;
    private String title;
    private String description;
    private List<MotiveOption> motives;

    private int trust = 20;
    private String currentEmotion = "NEUTRALNY";
    private int stressLevel = 0;
    private NpcDialogueConfig dialogueConfig;

    private String locationId;


    public Suspect() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<MotiveOption> getMotives() { return motives; }

    public int getTrust() { return trust; }
    public void changeTrust(int amount) { this.trust = Math.max(0, Math.min(100, this.trust + amount)); }

    public String getCurrentEmotion() { return currentEmotion; }
    public void setCurrentEmotion(String currentEmotion) { this.currentEmotion = currentEmotion; }

    public int getStressLevel() { return stressLevel; }
    public void changeStress(int amount) { this.stressLevel = Math.max(0, Math.min(100, this.stressLevel + amount)); }

    public NpcDialogueConfig getDialogueConfig() { return dialogueConfig; }
    public void setDialogueConfig(NpcDialogueConfig dialogueConfig) { this.dialogueConfig = dialogueConfig; }

    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    @Override
    public String toString() {
        return name + " (" + title + ") [Stres: " + stressLevel + "%, Zaufanie: " + trust + "]";
    }
}