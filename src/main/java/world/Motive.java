package world;

public class Motive {
    private String id;
    private String label;
    private String description;

    public Motive(String id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }
    public String getId() { return id; }
    public String getLabel() { return label; }
    public String getDescription() { return description; }
}