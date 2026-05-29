package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;
import java.util.Random;

public class AlibiCheckGame extends Minigame {

    private final String suspectToCheck;
    private final int correctHour = 22;

    public AlibiCheckGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
        List<String> suspects = List.of("Janusz", "Klaudia", "Artur", "Helena");
        this.suspectToCheck = suspects.get(new Random().nextInt(suspects.size()));
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Weryfikacja Logiki Alibi";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Analiza nagrań z monitoringu miejskiego i paragonów powiodła się!\n"
                    + "Masz niezaprzeczalny dowód, że " + suspectToCheck + " w momencie zbrodni był poza dworkiem."
                    : "Pogubiłeś się w zeznaniach świadków i osi czasu.\n"
                    + "Nadal nie masz pewności, czy podane alibi trzyma się kupy.";
        }

        return "Przeglądasz billingi telefoniczne i bilet z autostrady podejrzanego: " + suspectToCheck + ".\n"
                + "Morderstwo wydarzyło się dokładnie o godzinie 22:00 w dworku.\n"
                + "Logowanie GPS telefonu wskazuje, że o godzinie 21:15 " + suspectToCheck + " płacił za paliwo\n"
                + "na stacji oddalonej o 90 kilometrów stąd, jadąc rodzinnym, miejskim autem.\n\n"
                + "Czy to alibi jest fizycznie możliwe do podważenia w polskich warunkach drogowych?";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Zaktualizuj listę niewinnych w notesie");
        return List.of(
                "Nie, przejechanie 90 km w 45 minut starym autem to średnia 120km/h – mało prawdopodobne, ale możliwe.",
                "Tak, musiałby jechać ze średnią prędkością ponad 200 km/h lokalnymi drogami. Alibi jest ŻELAZNE, był za daleko.",
                "Odejdź od analizy dokumentów"
        );
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            if (won) {
                String alibiClue = "NIEWINNY: " + suspectToCheck + " ma stuprocentowe alibi. Ta osoba NIE MOGŁA być na miejscu zbrodni.";
                engine.getEventLog().addClue(alibiClue);
                returnScene.setMinigameResult(true, "Sukces! " + alibiClue);
            } else {
                returnScene.setMinigameResult(false, "Nie udało Ci się zweryfikować alibi.");
            }

            returnScene.markRoomAsDone(engine.getCurrentLocation().getId());

            return returnScene;
        }

        if (choice == 3) return returnScene;

        if (choice == 2) {
            won = true;
        } else {
            won = false;
        }

        gameOver = true;
        return this;
    }
}