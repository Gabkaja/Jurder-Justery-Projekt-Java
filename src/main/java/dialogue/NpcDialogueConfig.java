package dialogue;

import java.util.List;
import java.util.Map;

public class NpcDialogueConfig {
    private String npcId;
    private String name;
    private String title;
    private SpeechProfile speechProfile;
    private Map<String, List<DialogueEntry>> uniqueDialogues;
    private PersonalFragments personalFragments;

    public String getNpcId() { return npcId; }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public SpeechProfile getSpeechProfile() { return speechProfile; }
    public Map<String, List<DialogueEntry>> getUniqueDialogues() { return uniqueDialogues; }
    public PersonalFragments getPersonalFragments() { return personalFragments; }

    public static class SpeechProfile {
        private String primaryStyle;
        private String secondaryStyle;
        private String defaultEmotion;
        private List<String> speakingTraits;
        private String vocabulary;
        private String verbosity;
        private List<String> quirks;

        public String getPrimaryStyle() { return primaryStyle; }
        public String getSecondaryStyle() { return secondaryStyle; }
        public String getDefaultEmotion() { return defaultEmotion; }
        public List<String> getSpeakingTraits() { return speakingTraits; }
        public String getVocabulary() { return vocabulary; }
        public String getVerbosity() { return verbosity; }
        public List<String> getQuirks() { return quirks; }
    }

    public static class DialogueEntry {
        private String id;
        private String text;
        private String emotion;
        private String context;
        private Integer certainty;
        private String lieType;
        private List<String> contradictedBy;
        private String stressLevel;
        private String responseType;
        private Integer minTrust;

        public String getId() { return id; }
        public String getText() { return text; }
        public String getEmotion() { return emotion; }
        public String getContext() { return context; }
        public Integer getCertainty() { return certainty; }
        public String getLieType() { return lieType; }
        public List<String> getContradictedBy() { return contradictedBy; }
        public String getStressLevel() { return stressLevel; }
        public String getResponseType() { return responseType; }
        public Integer getMinTrust() { return minTrust; }
    }

    public static class PersonalFragments {
        private List<String> openers;
        private List<String> fillers;
        private List<String> closers;

        public List<String> getOpeners() { return openers; }
        public List<String> getFillers() { return fillers; }
        public List<String> getClosers() { return closers; }
    }
}