package com.cradle.starscape.managers;

import com.cradle.starscape.Main;
import com.cradle.starscape.utils.ColorCode;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocaleManager {
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{(\\d+?)\\}");

    private Main main;
    private ResourceBundle locale;

    public LocaleManager(Main main) {
        this.main = main;
        File dataFolder = main.getDataFolder();
        File propertiesFile = new File(dataFolder, "locale.properties");
        if (!propertiesFile.exists()) {
            main.saveResource("locale.properties", false);
        }
        try {
            URL[] urls = new URL[]{dataFolder.toURI().toURL()};
            locale = ResourceBundle.getBundle("locale", Locale.getDefault(), new URLClassLoader(urls));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    public String msg(String msgKey) {
        return ColorCode.translate(locale.getString(msgKey));
    }
    public String msg(String msgKey, HashMap<String, String> translations) {
        Matcher m = PLACEHOLDER_PATTERN.matcher(locale.getString(msgKey));
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            String placeholder = m.group(1);
            if (translations.containsKey(placeholder)) {
                m.appendReplacement(sb, translations.get(m.group(1)));
            }
        }
        m.appendTail(sb);
        return ColorCode.translate(sb.toString());
    }
    public String msg(String msgKey, String replacement) {
        Matcher m = PLACEHOLDER_PATTERN.matcher(locale.getString(msgKey));
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return ColorCode.translate(sb.toString());

    }
    public String msg(String msgKey, int replacement) {
        return msg(msgKey, replacement + "");
    }
    public String msg(String msgKey, String[] replacements) {
        HashMap<String, String> translations = new HashMap<String, String>();
        int count = 0;
        for (String e : replacements) {
            translations.put(count + "", e);
            count++;
        }
        return msg(msgKey, translations);
    }
    public String msg(String msgKey, int[] replacements) {
        String[] stringReplacements = Arrays.toString(replacements).split("[\\[\\]]")[1].split(", ");
        return msg(msgKey, stringReplacements);
    }
    public String msg(String msgKey, Object replacement) {
        if (replacement instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) replacement;
            String name = main.getPlayerManager().getPlayer(player.getUniqueId()).getDisplayName();
            return msg(msgKey, name);
        }
        return msg(msgKey, replacement.toString());
    }
    public String msg(String msgKey, Object[] replacements) {
        HashMap<String, String> translations = new HashMap<String, String>();
        int count = 0;
        for (Object e : replacements) {
            if (e instanceof OfflinePlayer) {
                OfflinePlayer player = (OfflinePlayer) e;
                String name = main.getPlayerManager().getPlayer(player.getUniqueId()).getDisplayName();
                translations.put(count + "", name);
            } else {
                translations.put(count + "", e.toString());
            }
            count++;
        }
        return msg(msgKey, translations);
    }
}