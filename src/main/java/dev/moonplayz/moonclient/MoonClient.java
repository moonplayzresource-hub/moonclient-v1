package dev.moonplayz.moonclient;

import dev.moonplayz.moonclient.gui.ClickGuiScreen;
import dev.moonplayz.moonclient.module.ModuleManager;
import dev.moonplayz.moonclient.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class MoonClient implements ClientModInitializer {
    public static final String MOD_ID = "moonclient";
    public static final String BRAND = "MOONPLAYZ19";
    public static ModuleManager MODULES;
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        ClientConfig.load();
        MODULES = new ModuleManager();
        MODULES.load(ClientConfig.getModuleStates());
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.moonclient.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.create(net.minecraft.util.Identifier.of(MOD_ID, "main"))));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
                else if (client.currentScreen instanceof ClickGuiScreen) client.setScreen(null);
            }
        });
        MoonHud.register();
    }

    public static void saveConfig() {
        if (MODULES != null) ClientConfig.save(MODULES.save());
    }

    public static Text title() {
        return Text.literal("MOONCLIENT").styled(style -> style.withColor(0xB9DFFF));
    }
}
