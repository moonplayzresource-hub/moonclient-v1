package dev.moonplayz.moonclient.gui;

import dev.moonplayz.moonclient.MoonClient;
import dev.moonplayz.moonclient.module.ClientModule;
import dev.moonplayz.moonclient.module.ModuleCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public final class ClickGuiScreen extends Screen {
    private ModuleCategory selected = ModuleCategory.HUD;
    private int panelX;
    private int panelY;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public ClickGuiScreen() {
        super(Text.literal("MoonClient"));
    }

    @Override
    protected void init() {
        clearChildren();
        panelX = Math.max(12, (width - 360) / 2);
        panelY = Math.max(12, (height - 240) / 2);
        int x = panelX + 12;
        int y = panelY + 36;
        for (ModuleCategory category : ModuleCategory.values()) {
            final ModuleCategory target = category;
            addDrawableChild(ButtonWidget.builder(Text.literal(category.label()), button -> {
                selected = target;
                clearAndInit();
            }).dimensions(x, y, 78, 20).build());
            x += 82;
        }
        int moduleY = panelY + 68;
        List<ClientModule> shown = MoonClient.MODULES.all().stream()
                .filter(module -> module.category() == selected).toList();
        for (ClientModule module : shown) {
            addDrawableChild(ButtonWidget.builder(Text.literal((module.enabled() ? "● " : "○ ") + module.name()), button -> {
                module.toggle();
                MoonClient.saveConfig();
                clearAndInit();
            }).dimensions(panelX + 14, moduleY, 190, 20).build());
            moduleY += 24;
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(panelX + 274, panelY + 204, 72, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Intentionally no full-screen dim: the ClickGUI stays lightweight and transparent.
        context.fill(panelX, panelY, panelX + 360, panelY + 240, 0xB8101722);
        context.drawTextWithShadow(textRenderer, "MOONCLIENT", panelX + 14, panelY + 12, 0xB9DFFF);
        context.drawTextWithShadow(textRenderer, "MOONPLAYZ19  |  " + selected.label(), panelX + 132, panelY + 16, 0xFFE7F3FF);
        context.drawTextWithShadow(textRenderer, "Click a module to toggle it", panelX + 14, panelY + 220, 0xFF9BAFC2);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseX >= panelX && mouseX <= panelX + 360 && mouseY >= panelY && mouseY <= panelY + 28) {
            dragging = true;
            dragOffsetX = (int) mouseX - panelX;
            dragOffsetY = (int) mouseY - panelY;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging && button == 0) {
            panelX = Math.max(0, Math.min(width - 360, (int) mouseX - dragOffsetX));
            panelY = Math.max(0, Math.min(height - 240, (int) mouseY - dragOffsetY));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        if (client != null) client.setScreen(null);
    }

    @Override
    public boolean shouldPause() { return false; }
}
