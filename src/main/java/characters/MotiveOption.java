package characters;

public class MotiveOption {
    private String type;
    private String description;

    public MotiveOption() {}

    public String getType() { return type; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return "MotiveOption{type='" + type + "', description='" + description + "'}";
    }
}