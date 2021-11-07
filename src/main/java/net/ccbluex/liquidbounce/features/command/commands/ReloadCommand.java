package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.script.Script;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", "configreload");
    }

    @Override
    public void execute(final String[] args) {
        this.chat("Reloading...");
        this.chat("§c§lReloading scripts...");
        LiquidBounce.scriptManager.reloadScripts();
        this.chat("§c§lReloading fonts...");
        Fonts.loadFonts();
        this.chat("§c§lReloading modules...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.modulesConfig);
        this.chat("§c§lReloading values...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.valuesConfig);
        this.chat("§c§lReloading accounts...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.accountsConfig);
        this.chat("§c§lReloading friends...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.friendsConfig);
        this.chat("§c§lReloading xray...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.xrayConfig);
        this.chat("§c§lReloading hud...");
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.hudConfig);
        this.chat("§c§lReloading ClickGUI...");
        LiquidBounce.clickGui = new ClickGui();
        LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.clickGuiConfig);
        this.chat("Reloaded.");
    }
}