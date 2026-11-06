# MoonClient V1

An original client-side Fabric mod project branded **MOONCLIENT / MOONPLAYZ19**. This repository starts the V1 implementation with a transparent-by-default HUD, a lightweight ClickGUI, and persisted module toggles.

## Build

- Minecraft: 1.21.11
- Java: 21
- Fabric Loader: 0.18.1
- Fabric API: 0.140.2+1.21.11

Use a Java 21 JDK and run `./gradlew build` (or `gradlew.bat build` on Windows). The distributable mod JAR is written to `build/libs/`.

## Current implemented functionality

- Client entrypoint and Fabric metadata.
- Right Shift opens/closes the ClickGUI.
- HUD widgets: FPS, coordinates, server ping, local clock, and movement-key state.
- HUD text renders without a panel/background by default.
- Category navigation and functional enable/disable toggles for implemented HUD widgets.
- Module enabled states load from and save to `config/moonclient.json`.
- ClickGUI can be dragged by its header and does not pause the game.

## Status and limitations

This is the initial source scaffold, not a finished premium client. The ClickGUI has a restrained translucent panel; HUD components are background-free. The module entries marked Crosshair, Zoom, and Low Memory Profile are currently catalog-only and are not implemented toggles; they are intentionally not exposed in the GUI until their behavior is implemented. No custom combat automation, anti-cheat bypass, or server-rule evasion is included.

The repository has not been built or run in a Minecraft client in this environment. Verify the selected Minecraft/Fabric dependency versions and API compatibility with `./gradlew build`, then test in a disposable client instance before distributing.
