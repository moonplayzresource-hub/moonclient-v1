package dev.moonplayz.moonclient;

import dev.moonplayz.moonclient.module.ClientModule;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class MoonHud {
    private static final int TEXT = 0xFFF4F8FC;
    private static final int ACCENT = 0xFFB9DFFF;
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm");

    private MoonHud() {}

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> render(context));
    }

    private static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;
        int x = 8;
        int y = 8;
        List<String> lines = new ArrayList<>();
        if (enabled("fps")) lines.add("FPS " + client.getCurrentFps());
        if (enabled("coordinates") && client.player != null) {
            ClientPlayerEntity player = client.player;
            lines.add(String.format("XYZ  %d  %d  %d", (int) player.getX(), (int) player.getY(), (int) player.getZ()));
        }
        if (enabled("ping") && client.player != null && client.getNetworkHandler() != null) {
            var entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (entry != null) lines.add("PING " + entry.getLatency() + " ms");
        }
        if (enabled("clock")) lines.add(CLOCK.format(LocalTime.now()));
        if (enabled("keystrokes")) {
            String keys = (client.options.forwardKey.isPressed() ? "W" : "w") + " "
                    + (client.options.leftKey.isPressed() ? "A" : "a") + " "
                    + (client.options.backKey.isPressed() ? "S" : "s") + " "
                    + (client.options.rightKey.isPressed() ? "D" : "d");
            lines.add(keys);
        }
        for (int i = 0; i < lines.size(); i++) {
            context.drawTextWithShadow(client.textRenderer, lines.get(i), x, y + i * 11, i == 0 ? ACCENT : TEXT);
        }
    }

    private static boolean enabled(String id) {
        ClientModule module = MoonClient.MODULES == null ? null : MoonClient.MODULES.find(id);
        return module != null && module.enabled();
    }
}
