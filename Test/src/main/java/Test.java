import java.util.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Test {
    final static public Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(\\d+?)\\}");
    public static void main(String[] args) {

    }

    public static String replacePlaceholders(String originalText) {
        return originalText;
    }
    public static String replacePlaceholders(String originalText, HashMap<String, String> translations) {
        Matcher m = PLACEHOLDER_PATTERN.matcher(originalText);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String placeholder = m.group(1);
            if (translations.containsKey(placeholder)) {
                m.appendReplacement(sb, translations.get(m.group(1)));
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }
    public static String replacePlaceholders(String originalText, String replacement) {

        Matcher m = PLACEHOLDER_PATTERN.matcher(originalText);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();

    }
    public static String replacePlaceholders(String originalText, int replacement) {
        return replacePlaceholders(originalText, replacement + "");
    }
    public static String replacePlaceholders(String originalText, String[] replacements) {
        HashMap<String, String> translations = new HashMap<String, String>();
        int count = 0;
        for (String e : replacements) {
            translations.put(count + "", e);
            count++;
        }
        return replacePlaceholders(originalText, translations);
    }
    public static String replacePlaceholders(String originalText, int[] replacements) {
        String[] stringReplacements = Arrays.toString(replacements).split("[\\[\\]]")[1].split(", ");
        return replacePlaceholders(originalText, stringReplacements);
    }
    public static String replacePlaceholders(String originalText, Object[] replacements) {
        HashMap<String, String> translations = new HashMap<String, String>();
        int count = 0;
        for (Object e : replacements) {
            translations.put(count + "", e.toString());
            count++;
        }
        return replacePlaceholders(originalText, translations);
    }
}
