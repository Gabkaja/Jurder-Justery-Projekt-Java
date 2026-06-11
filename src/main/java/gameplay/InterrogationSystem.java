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
        MurderCase caseInfo = engine.getMurderCase();

        if (generator == null || caseInfo == null) {
            this.lastNpcSpeech = suspect.getName() + ": (Brak danych do rozmowy)";
            return;
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("time", "22:00");
        placeholders.put("room", caseInfo.getCrimeScene().getName());
        placeholders.put("npc", suspect.getName());

        // 1. Sprawdzamy, czy ten NPC jest poszukiwanym mordercą
        boolean isKiller = suspect.getName().equalsIgnoreCase(caseInfo.getKiller());

        // Pobieramy listę pozostałych (niewinnych) podejrzanych do manipulacji plotkami
        List<Suspect> innocentSuspects = engine.getSuspects().stream()
                .filter(s -> !s.getName().equalsIgnoreCase(caseInfo.getKiller()))
                .toList();

        // 2. LOGIKA GENEROWANIA POSZLAK I KŁAMSTW
        if (isKiller) {
            // MORDERCA: Próbuje wrobić kogoś niewinnego!
            if (!innocentSuspects.isEmpty()) {
                int randIdx = new java.util.Random().nextInt(innocentSuspects.size());
                placeholders.put("suspect", innocentSuspects.get(randIdx).getName()); // Wskazuje palcem na niewinnego
            }
            placeholders.put("weapon", "czymś innym niż " + caseInfo.getWeapon()); // Myli trop co do broni
            placeholders.put("motive", "jakichś starych porachunkach");

            // Jeśli gracz pyta o poszlaki, wymuszamy kategorię kłamstwa
            if (category.equalsIgnoreCase("poszlaki") || category.equalsIgnoreCase("clues")) {
                category = "klamstwa"; // Silnik dialogowy sięgnie do klamstwa.json!
            }
        } else {
            // NIEWINNY ŚWIADEK: Mówi prawdę, ale tylko jeśli zaufanie (trust) jest wysokie!
            placeholders.put("killer", caseInfo.getKiller());
            placeholders.put("weapon", caseInfo.getWeapon());
            placeholders.put("motive", caseInfo.getMotive().getDescription());

            // Dynamiczny dobór kogo widział świadek (kręcącego się obok miejsca zbrodni)
            placeholders.put("suspect", caseInfo.getKiller()); // Świadek widział mordercę!
            placeholders.put("victim", "ofiary");

            if (category.equalsIgnoreCase("poszlaki") || category.equalsIgnoreCase("clues")) {
                if (suspect.getTrust() >= 60) {
                    category = "obserwacje_wzrokowe"; // Daje kluczową poszlakę!
                } else {
                    category = "alibi"; // Jest nieufny, mówi tylko o sobie
                }
            }
        }

        // 3. Pobieranie wpisu dialogowego z odpowiedniej (przepisanej wyżej) kategorii
        if (suspect.getDialogueConfig() != null && suspect.getDialogueConfig().getUniqueDialogues() != null) {
            List<NpcDialogueConfig.DialogueEntry> entries = suspect.getDialogueConfig().getUniqueDialogues().get(category);
            if (entries != null && !entries.isEmpty()) {
                this.currentEntry = entries.stream()
                        .filter(e -> e.getMinTrust() == null || suspect.getTrust() >= e.getMinTrust())
                        .findFirst().orElse(entries.get(0));
            }
        }

        // Generowanie tekstu (wstrzyknięcie cech mowy i gotowych, logicznych placeholderów)
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