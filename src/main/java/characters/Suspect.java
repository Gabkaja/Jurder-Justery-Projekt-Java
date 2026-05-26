package characters;

import java.util.List;

public class Suspect {
    private String id;
    private String name;
    private String title;
    private String description;
    private List<MotiveOption> motives;

    public Suspect() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<MotiveOption> getMotives() { return motives; }

    @Override
    public String toString() {
        return "Suspect{id='" + id + "', name='" + name + "', title='" + title + "'}";
    }
}