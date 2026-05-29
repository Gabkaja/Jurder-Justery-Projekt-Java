package engine;

import characters.PlayerCharacter;
import data.GameDataLoader;
import gameplay.SearchSystem;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterSelectScene extends SceneManager {

    private final List<PlayerCharacter> characters;

    public CharacterSelectScene(GameEngine engine) {
        super(engine);
        this.characters = new GameDataLoader().loadPlayerCharacters();
    }

    @Override
    public String getTitle() {
        return "Wybór postaci";
    }

    @Override
    public String getNarration() {
        return "Wybierz detektywa, którym chcesz poprowadzić śledztwo.\n"
                + "Każda postać posiada unikalne umiejętności i definiuje poziom trudności gry.";
    }

    @Override
    public List<String> getOptions() {
        return characters.stream()
                .map(pc -> pc.getName() + " — " + pc.getTitle() + " (" + translateDifficulty(pc.getDifficulty()) + ")")
                .collect(Collectors.toList());
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (choice >= 1 && choice <= characters.size()) {
            PlayerCharacter selectedChar = characters.get(choice - 1);

            // Zapisujemy wybraną postać w silniku (silnik sam zajmie się poziomem trudności)
            engine.setPlayer(selectedChar);

            if (engine.getEventLog() != null) {
                engine.getEventLog().addEntry("Rozpoczęto śledztwo jako: " + selectedChar.getName());
            }

            return new SearchSystem(engine);
        }

        return this;
    }

    // Pomocnicza metoda, żeby w menu wyświetlać ładne polskie nazwy trudności zamiast EASY/HARD
    private String translateDifficulty(String diff) {
        if (diff == null) return "Nieznany";
        return switch (diff.toUpperCase()) {
            case "EASY" -> "Łatwy";
            case "MEDIUM" -> "Normalny";
            case "HARD" -> "Trudny";
            case "VERY_HARD" -> "Bardzo Trudny";
            default -> diff;
        };
    }
}