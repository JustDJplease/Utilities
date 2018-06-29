package me.theblockbender.util;

import me.theblockbender.util.armorstand.UtilArmorStandAnimatorTask;
import me.theblockbender.util.gui.UtilMultiPageGuiListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * @author TheBlockBender / JustDJplease
 * https://www.spigotmc.org/members/justdjplease.120217/
 */
public class Main extends JavaPlugin {
    public void onEnable() {
        registerEvents();
        registerSchedulers();
    }

    private void registerSchedulers() {
        BukkitScheduler bs = Bukkit.getScheduler();
        bs.runTaskTimer(this, new UtilArmorStandAnimatorTask(), 3L, 3L);
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new UtilMultiPageGuiListener(), this);
    }
}
