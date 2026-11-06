package dev.moonplayz.moonclient.module;

public enum ModuleCategory {
    HUD("HUD"), VISUALS("Visuals"), UTILITY("Utility"), CLIENT("Client");

    private final String label;
    ModuleCategory(String label) { this.label = label; }
    public String label() { return label; }
}
