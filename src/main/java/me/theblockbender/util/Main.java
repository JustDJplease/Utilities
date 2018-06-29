package me.theblockbender.util;

import me.theblockbender.util.gui.UtilMultiPageGuiListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TheBlockBender / JustDJplease
 * https://www.spigotmc.org/members/justdjplease.120217/
 */
public class Main extends JavaPlugin {
    public void onEnable() {
        registerEvents();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new UtilMultiPageGuiListener(), this);
    }
}
