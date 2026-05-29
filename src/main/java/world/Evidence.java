package world;

public class Evidence {
    public enum Type {
        BROŃ, LOKACJA, PODEJRZANY, MOTYW
    }

    private final String id;
    private final String name;
    private final String description;
    private final Type type;
    private final boolean isTrueClue; // true = prowadzi do mordercy, false = ślepy zaułek / alibi

    public Evidence(String id, String name, String description, Type type, boolean isTrueClue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.isTrueClue = isTrueClue;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Type getType() { return type; }
    public boolean isTrueClue() { return isTrueClue; }
}