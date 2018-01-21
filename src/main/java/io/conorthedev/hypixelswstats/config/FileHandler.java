package io.conorthedev.hypixelswstats.config;

import net.minecraft.client.*;
import com.google.gson.*;

import io.conorthedev.hypixelswstats.MainClass;

import java.io.*;
import java.nio.file.*;
import org.apache.logging.log4j.*;

public class FileHandler
{
    private static final File configFile;
    private static JsonObject config;
    
    static {
        configFile = new File(new File(String.valueOf(Minecraft.getMinecraft().mcDataDir.getPath()) + "/config/HypixelSkywarsStats"), "HypixelSWStats.settings");
        FileHandler.config = new JsonObject();
    }
    
    public static boolean configExists() {
        return exists(FileHandler.configFile.getPath());
    }
    
    public static void loadSettings() {
        if (configExists()) {
            log("Config file exists! Reading...", new String[0]);
            System.out.println("Config file exists! Reading..");
            try {
                final FileReader ex = new FileReader(FileHandler.configFile);
                final BufferedReader bufferedReader = new BufferedReader(ex);
                final StringBuilder builder = new StringBuilder();
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    builder.append(currentLine);
                }
                bufferedReader.close();
                final String complete = builder.toString();
                FileHandler.config = new JsonParser().parse(complete).getAsJsonObject();
            }
            catch (Exception var5) {
                log("Could not write config! Saving...", new String[0]);
                saveSettings();
            }
            System.out.println("reading...");
            MainClass.chromaEnabled = (FileHandler.config.has("chromaEnabled") && FileHandler.config.get("chromaEnabled").getAsBoolean());
            MainClass.debugEnabled = (FileHandler.config.has("debugEnabled") && FileHandler.config.get("debugEnabled").getAsBoolean());
        }
        else {
            log("Config does not exist! Saving...", new String[0]);
            saveSettings();
        }
    }
    
    public static void saveSettings() {
        FileHandler.config = new JsonObject();
        try {
            if (!FileHandler.configFile.getParentFile().exists()) {
                FileHandler.configFile.getParentFile().mkdir();
            }
            FileHandler.configFile.createNewFile();
            final FileWriter ex = new FileWriter(FileHandler.configFile);
            final BufferedWriter bufferedWriter = new BufferedWriter(ex);
            FileHandler.config.addProperty("chromaEnabled", MainClass.chromaEnabled);
            FileHandler.config.addProperty("debugEnabled", MainClass.debugEnabled);
            bufferedWriter.write(FileHandler.config.toString());
            bufferedWriter.close();
            ex.close();
        }
        catch (Exception var2) {
            log("Could not save config!", new String[0]);
            var2.printStackTrace();
        }
    }
    
    private static boolean exists(final String path) {
        return Files.exists(Paths.get(path, new String[0]), new LinkOption[0]);
    }
    
    private static void log(final Object message, final String... replacements) {
        LogManager.getLogger("FileUtils").info(String.format(String.valueOf(message), replacements));
    }
}
