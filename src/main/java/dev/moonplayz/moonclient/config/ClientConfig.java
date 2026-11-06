package dev.moonplayz.moonclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("moonclient.json");
    private static JsonObject moduleStates = new JsonObject();

    private ClientConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        try (Reader reader = Files.newBufferedReader(FILE)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root != null && root.has("modules") && root.get("modules").isJsonObject()) {
                moduleStates = root.getAsJsonObject("modules");
            }
        } catch (Exception exception) {
            System.err.println("[MoonClient] Could not read config: " + exception.getMessage());
        }
    }

    public static JsonObject getModuleStates() {
        return moduleStates.deepCopy();
    }

    public static void save(JsonObject modules) {
        JsonObject root = new JsonObject();
        root.add("modules", modules);
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE)) {
                GSON.toJson(root, writer);
            }
        } catch (Exception exception) {
            System.err.println("[MoonClient] Could not save config: " + exception.getMessage());
        }
    }
}
