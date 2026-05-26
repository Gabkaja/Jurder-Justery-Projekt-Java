package gameplay;

import java.util.List;

import engine.GameEngine;
import engine.SceneManager;

// odpowiada za przemieszczanie się pomiędzy lokacajmi
public class ExplorationSystem extends SceneManager {

    public ExplorationSystem(GameEngine engine) {
        super(engine);
    }

    @Override
    public String getNarration() {
        // zwróć tekst, dla eksploracji to proste "wybierz dokąd chcesz iść"
        // TODO: obiekt Location powinien przechowywać i zwracać swój tekst
        return "Dokąd chcesz pójść?";
    }

    @Override
    public List<String> getOptions() {
        // zwróć opcje, dla eksploracji to lista lokacji, do których możesz dojść + wróć
        // TODO: obiekt Location powinien przechowywać i zwracać swój tekst
        return engine.getCurrentLocation().getOptions();
    }

    @Override
    public SceneManager onChoice(int choice) {
        // jeśli wybrana opcja odpowiada lokacji, zmień currentLocation
        // jeśli cofnij, zwróć siebie
        List<String> options = getOptions();

        if (choice < 1 || choice > options.size()) {
            return this;
        }

        String selectedOption = options.get(choice - 1);
        if ("wróć".equalsIgnoreCase(selectedOption) || "cofnij".equalsIgnoreCase(selectedOption)) {
            return this;
        }

        for (var location : engine.getLocations()) {
            if (selectedOption.equalsIgnoreCase(location.toString())) {
                engine.setCurrentLocation(location);
                break;
            }
        }

        return this;
    }
}
