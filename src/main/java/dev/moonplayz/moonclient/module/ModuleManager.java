package dev.moonplayz.moonclient.module;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private final List<ClientModule> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new ClientModule("fps", "FPS", "Displays the current frame rate.", ModuleCategory.HUD, true));
        modules.add(new ClientModule("coordinates", "Coordinates", "Shows your current position.", ModuleCategory.HUD, true));
        modules.add(new ClientModule("ping", "Ping", "Displays latency to the connected server.", ModuleCategory.HUD, false));
        modules.add(new ClientModule("keystrokes", "Keystrokes", "Shows movement key states.", ModuleCategory.HUD, false));
        modules.add(new ClientModule("clock", "Clock", "Displays the local time.", ModuleCategory.HUD, false));
        modules.add(new ClientModule("crosshair", "Crosshair", "Uses the vanilla crosshair; customization is planned.", ModuleCategory.VISUALS, false));
        modules.add(new ClientModule("zoom", "Zoom", "Client-side view zoom while the assigned key is held.", ModuleCategory.VISUALS, false));
        modules.add(new ClientModule("low_memory", "Low Memory Profile", "Reduces optional client HUD work; does not change game graphics.", ModuleCategory.UTILITY, false));
    }

    public List<ClientModule> all() { return List.copyOf(modules); }

    public ClientModule find(String id) {
        return modules.stream().filter(module -> module.id().equals(id)).findFirst().orElse(null);
    }

    public void load(JsonObject state) {
        for (ClientModule module : modules) {
            if (state.has(module.id()) && state.get(module.id()).isJsonPrimitive()) {
                try { module.setEnabled(state.get(module.id()).getAsBoolean()); } catch (RuntimeException ignored) { }
            }
        }
    }

    public JsonObject save() {
        JsonObject state = new JsonObject();
        for (ClientModule module : modules) state.addProperty(module.id(), module.enabled());
        return state;
    }
}
