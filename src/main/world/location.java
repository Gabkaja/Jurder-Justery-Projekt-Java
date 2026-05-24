package world;

import java.util.List;

public class Location {
    private String id;
    private String name;
    private String description;
    private List<String> passages;
    private List<String> weapons;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getPassages() { return passages; }
    public List<String> getWeapons() { return weapons; }
}