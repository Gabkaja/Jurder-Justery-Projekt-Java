package dialogue;

import characters.Suspect;
import java.util.List;
import java.util.Map;
import java.util.Random;
import dialogue.NpcDialogueConfig.DialogueEntry;
import dialogue.SpeechStyleConfig.StyleConfig;
import dialogue.SpeechStyleConfig.SentenceModifiers;
public class DialogueGenerator {
    private final Random random = new Random();
    private final SpeechStyleConfig styleConfig;

    public DialogueGenerator(SpeechStyleConfig styleConfig) {
        this.styleConfig = styleConfig;
    }

    public String generateResponse(Suspect suspect, String category, Map<String, String> placeholders) {
        NpcDialogueConfig config = suspect.getDialogueConfig();
        if (config == null || config.getUniqueDialogues() == null) {
            return suspect.getName() + ": Nie mam nic do powiedzenia.";
        }

        List<DialogueEntry> lines = config.getUniqueDialogues().get(category);
        if (lines == null || lines.isEmpty()) {
            return suspect.getName() + ": (Milczy niepewnie)";
        }

        // Filtrowanie pod kątem zaufania (minTrust)
        List<DialogueEntry> availableLines = lines.stream()
                .filter(l -> l.getMinTrust() == null || suspect.getTrust() >= l.getMinTrust())
                .toList();

        if (availableLines.isEmpty()) {
            return suspect.getName() + ": Nie ufam panu na tyle, by o tym rozmawiać.";
        }

        // Losowanie bazy wypowiedzi
        DialogueEntry entry = availableLines.get(random.nextInt(availableLines.size()));
        String text = entry.getText();

        // 1. Podmiana placeholderów
        for (Map.Entry<String, String> p : placeholders.entrySet()) {
            text = text.replace("{" + p.getKey() + "}", p.getValue());
        }

        // 2. Pobranie stylu mowy postaci
        String styleId = config.getSpeechProfile().getPrimaryStyle();
        StyleConfig style = styleConfig.getStyles().stream()
                .filter(s -> s.getId().equalsIgnoreCase(styleId))
                .findFirst().orElse(null);

        if (style != null && style.getSentenceModifiers() != null) {
            SentenceModifiers mods = style.getSentenceModifiers();
            String prefix = getRandomOrEmpty(mods.getPrefixes());
            String suffix = getRandomOrEmpty(mods.getSuffixes());
            String filler = getRandomOrEmpty(mods.getFillers());

            if (!filler.isEmpty() && text.contains(",")) {
                text = text.replaceFirst(",", ", " + filler + ",");
            }
            text = prefix + " " + text + " " + suffix;
        }

        // 3. Dodanie unikalnych fragmentów osobistych dla NPC (np. Powitanie/Pożegnanie)
        if (category.equals("POWITANIA") && config.getPersonalFragments() != null) {
            String personalOpener = getRandomOrEmpty(config.getPersonalFragments().getOpeners());
            text = personalOpener + " " + text;
        }

        return suspect.getName() + ": \"" + text.replaceAll("\\s+", " ").trim() + "\"";
    }

    private String getRandomOrEmpty(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return list.get(random.nextInt(list.size()));
    }
}