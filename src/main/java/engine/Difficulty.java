package engine;

public enum Difficulty {
    EASY("Łatwy"),
    MEDIUM("Normalny"),
    HARD("Trudny"),
    VERY_HARD("Bardzo Trudny");

    private final String label;

    Difficulty(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}