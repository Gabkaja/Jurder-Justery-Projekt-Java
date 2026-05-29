package gameplay.minigames;

import engine.GameEngine;
import engine.SceneManager;
import gameplay.SearchSystem;
import java.util.List;
import java.util.Random;

public class RhythmGame extends Minigame {

    private int currentStep = 1;
    private int totalStepsRequired = 3; // Gracz musi wykonać 3 dobre kroki z rzędu
    private int targetBeat;             // Cyfra, którą gracz musi kliknąć w tej turze
    private String musicDescription = "Orkiestra zaczyna grać powolnego, dostojnego walca.";

    public RhythmGame(GameEngine engine, SearchSystem returnScene) {
        super(engine, returnScene);
        generateNextBeat();
    }

    private void generateNextBeat() {
        // Losujemy cyfrę od 1 do 3 (krok walca: raz, dwa, trzy...)
        this.targetBeat = new Random().nextInt(3) + 1;
    }

    @Override
    public String getTitle() {
        return "MINIGRA: Taniec w Sali Balowej";
    }

    @Override
    public String getNarration() {
        if (gameOver) {
            return won ? "Płyniesz po parkiecie jak profesjonalista. Twoje ruchy są nienaganne.\n"
                    + "Gdy mijasz parę wirującą obok, ich szept wyraźnie dociera do Twoich uszu..."
                    : "Nagle tracisz równowagę i deptasz swoją partnerkę/partnera po palcach!\n"
                    + "Muzyka na moment cichnie, a na Tobie skupiają się zniesmaczone spojrzenia gości. Skandal...";
        }

        return "Wchodzisz na błyszczący parkiet Sali Balowej. Musisz idealnie wtopić się w tłum,\n"
                + "by podsłuchać sekrety arystokracji. Wokół szumią jedwabne suknie.\n\n"
                + "[Rytm Tańca]: Krok " + currentStep + " z " + totalStepsRequired + "\n"
                + "[Orkiestra]: " + musicDescription + "\n"
                + "Słyszysz rytm: ... RAZ... DWA... TRZY... Szykuj się na krok numer: " + targetBeat + "!";
    }

    @Override
    public List<String> getOptions() {
        if (gameOver) return List.of("Zakończ taniec i przeanalizuj plotki");

        return List.of("Wykonaj krok: RAZ (1)", "Wykonaj krok: DWA (2)", "Wykonaj krok: TRZY (3)", "Ucieknij z parkietu");
    }

    @Override
    public SceneManager onChoice(int choice) {
        if (gameOver) {
            return endMinigame("W rytmie walca wyłowiłeś z szumu cenną informację!", "Z powodu wpadki towarzyskiej niczego się nie dowiedziałeś.", "WEAPON");
        }

        if (choice == 4) {
            return returnScene;
        }

        if (choice == targetBeat) {
            currentStep++;
            musicDescription = "Idealnie! Łapiesz tempo, obrót wychodzi Ci znakomicie.";

            if (currentStep > totalStepsRequired) {
                won = true;
                gameOver = true;
            } else {
                generateNextBeat();
            }
        } else {
            won = false;
            gameOver = true;
        }

        return this;
    }
}