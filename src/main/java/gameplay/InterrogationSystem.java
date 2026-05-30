package gameplay;

import characters.Suspect;
import dialogue.DialogueGenerator;
import engine.GameEngine;
import engine.SceneManager;
import world.MurderCase;
import dialogue.NpcDialogueConfig.DialogueEntry;
import dialogue.NpcDialogueConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterrogationSystem extends SceneManager {

    private final SceneManager previousScene;
    private final Suspect suspect;
    private String lastNpcSpeech = "";

    private DialogueEntry currentEntry = null;

    public InterrogationSystem(GameEngine engine, SceneManager previousScene, Suspect suspect) {
        super(engine);
        this.previousScene = previousScene;
        this.suspect = suspect;

        triggerDialogue("POWITANIA");
    }

    @Override
    public String getTitle() {
        return "Przesłuchanie: " + suspect.getName();
    }

    @Override
    public String getNarration() {
        return "Status podejrzanego: [" + suspect.getTitle() + "]\n"
                + "Zaufanie: " + suspect.getTrust() + "/100 | Stres: " + suspect.getStressLevel() + "%\n"
                + "Nastrój: " + suspect.getCurrentEmotion() + "\n"
                + "======================================================\n\n"
                + lastNpcSpeech;
    }

    @Override
    public List<String> getOptions() {
        if (currentEntry != null && currentEntry.getContradictedBy() != null && !currentEntry.getContradictedBy().isEmpty()) {
            return List.of(
                    "Zapytaj o alibi",
                    "Zapytaj o obserwacje wzrokowe",
                    "Zapytaj o obserwacje słuchowe",
                    "Zapytaj o opinię o innych",
                    "Wywrzyj nacisk psychologiczny",
                    "!!! PRZEDSTAW DOWÓD I ZARZUĆ KŁAMSTWO !!!",
                    "Zakończ rozmowę"
            );
        }

        return List.of(
                "Zapytaj o alibi",
                "Zapytaj o obserwacje wzrokowe",
                "Zapytaj o obserwacje słuchowe",
                "Zapytaj o opinię o innych",
                "Wywrzyj nacisk psychologiczny",
                "Zakończ rozmowę"
        );
    }

    @Override
    public SceneManager onChoice(int choice) {
        boolean dynamicOptionPresent = currentEntry != null && currentEntry.getContradictedBy() != null && !currentEntry.getContradictedBy().isEmpty();

        if (dynamicOptionPresent && choice == 6) {
            handleAccuseOfLying();
            return this;
        }

        int effectiveChoice = (dynamicOptionPresent && choice > 6) ? choice - 1 : choice;

        switch (effectiveChoice) {
            case 1 -> triggerDialogue("ALIBI");
            case 2 -> triggerDialogue("OBSERWACJE_WZROKOWE");
            case 3 -> triggerDialogue("OBSERWACJE_SLUCHOWE");
            case 4 -> triggerDialogue("OPINIE");
            case 5 -> {
                suspect.changeStress(20);
                suspect.changeTrust(-5);
                suspect.setCurrentEmotion("ZANIEPOKOJONY");
                triggerDialogue("REAKCJA_NA_PRESJE");
            }
            case 6 -> { return previousScene; }
        }
        return this;
    }

    private void triggerDialogue(String category) {
        DialogueGenerator generator = engine.getDialogueGenerator();
        if (generator == null) {
            this.lastNpcSpeech = suspect.getName() + ": (System dialogowy nieaktywny)";
            return;
        }

        // Przygotowanie danych do wstrzyknięcia w tekst (placeholdery z pliku JSON)
        Map<String, String> placeholders = new HashMap<>();
        MurderCase caseInfo = engine.getMurderCase();

        if (caseInfo != null) {
            placeholders.put("room", caseInfo.getCrimeScene().getName());
            placeholders.put("weapon", caseInfo.getWeapon());
            placeholders.put("motive", caseInfo.getMotive().getDescription());
            placeholders.put("killer", caseInfo.getKiller());
        }
        placeholders.put("time", "22:00");
        placeholders.put("npc", suspect.getName());

        String dynamicSuspectName = "ktoś podejrzany";
        if (engine.getSuspects() != null && engine.getSuspects().size() > 1) {
            List<Suspect> otherSuspects = engine.getSuspects().stream()
                    .filter(s -> !s.getId().equals(suspect.getId()))
                    .toList();

            if (!otherSuspects.isEmpty()) {
                int randomIndex = new java.util.Random().nextInt(otherSuspects.size());
                dynamicSuspectName = otherSuspects.get(randomIndex).getName();
            }
        }
        placeholders.put("suspect", dynamicSuspectName);

        if (suspect.getDialogueConfig() != null && suspect.getDialogueConfig().getUniqueDialogues() != null) {
            List<NpcDialogueConfig.DialogueEntry> entries = suspect.getDialogueConfig().getUniqueDialogues().get(category);
            if (entries != null && !entries.isEmpty()) {
                this.currentEntry = entries.stream()
                        .filter(e -> e.getMinTrust() == null || suspect.getTrust() >= e.getMinTrust())
                        .findFirst().orElse(entries.get(0));
            }
        }

        this.lastNpcSpeech = generator.generateResponse(suspect, category, placeholders);
    }

    private void handleAccuseOfLying() {
        if (currentEntry == null || currentEntry.getContradictedBy() == null) return;

        List<String> requiredClueTriggers = currentEntry.getContradictedBy();
        List<String> playerClues = engine.getEventLog().getClues();

        boolean foundContradiction = false;
        String matchingClue = "";

        for (String clue : playerClues) {
            for (String trigger : requiredClueTriggers) {
                if (clue.toUpperCase().contains(trigger.toUpperCase())) {
                    foundContradiction = true;
                    matchingClue = clue;
                    break;
                }
            }
            if (foundContradiction) break;
        }

        if (foundContradiction) {
            suspect.changeStress(40);
            suspect.changeTrust(-25);
            suspect.setCurrentEmotion("PRZERAŻONY");

            this.lastNpcSpeech = "DETEKTYW: \"To kłamstwo! Moje śledztwo wykazuje coś zupełnie innego: " + matchingClue + "\"\n\n"
                    + suspect.getName() + ": \"G-Głupoty opowiadacie... Skąd to macie?! No dobrze, może nie mówiłem całej prawdy...\"";

            engine.getEventLog().addEntry("Złamano kłamstwo postaci " + suspect.getName() + " podczas przesłuchania!");
        } else {
            suspect.changeTrust(-15);
            suspect.changeStress(-10);
            suspect.setCurrentEmotion("PEWNY_SIEBIE");

            this.lastNpcSpeech = "DETEKTYW: \"Mijasz się z prawdą! Wiem to!\"\n\n"
                    + suspect.getName() + ": \"Rzuca pan bezpodstawne oskarżenia. Proszę przedstawić dowody, albo przestać marnować mój czas!\"";
        }

        currentEntry = null;
    }
}