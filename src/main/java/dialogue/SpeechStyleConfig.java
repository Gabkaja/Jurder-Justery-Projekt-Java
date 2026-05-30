package dialogue;

import java.util.List;
import java.util.Map;

public class SpeechStyleConfig {
    private List<StyleConfig> styles;
    private Map<String, List<String>> styleCompatibility;

    public List<StyleConfig> getStyles() { return styles; }
    public Map<String, List<String>> getStyleCompatibility() { return styleCompatibility; }


    public static class StyleConfig {
        private String id;
        private String label;
        private SentenceModifiers sentenceModifiers;

        public String getId() { return id; }
        public String getLabel() { return label; }
        public SentenceModifiers getSentenceModifiers() { return sentenceModifiers; }
    }

    public static class SentenceModifiers {
        private List<String> prefixes;
        private List<String> suffixes;
        private List<String> fillers;

        public List<String> getPrefixes() { return prefixes; }
        public List<String> getSuffixes() { return suffixes; }
        public List<String> getFillers() { return fillers; }
    }
}