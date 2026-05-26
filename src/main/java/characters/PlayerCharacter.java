package characters;

import java.util.List;

public class PlayerCharacter {
    private String id;
    private String name;
    private String title;
    private String difficulty;
    private String description;
    private List<String> specialSkills;

    public PlayerCharacter() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public List<String> getSpecialSkills() { return specialSkills; }

    @Override
    public String toString() {
        return "PlayerCharacter{id='" + id + "', name='" + name + "', difficulty='" + difficulty + "'}";
    }
}