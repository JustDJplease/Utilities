package me.theblockbender.util.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Hex_27
 * https://www.spigotmc.org/members/hex_27.23764/
 */
public class UtilMultiPageGuiListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void InventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!UtilMultiPageGui.users.containsKey(player.getUniqueId())) return;
        UtilMultiPageGui inv = UtilMultiPageGui.users.get(player.getUniqueId());
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(UtilMultiPageGui.nextPageName)) {
            event.setCancelled(true);
            if (inv.currpage >= inv.pages.size() - 1) {
                player.sendMessage(ChatColor.RED + "This is the last page.");
            } else {
                inv.currpage += 1;
                player.openInventory(inv.pages.get(inv.currpage));
            }
        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(UtilMultiPageGui.previousPageName)) {
            event.setCancelled(true);
            if (inv.currpage > 0) {
                inv.currpage -= 1;
                player.openInventory(inv.pages.get(inv.currpage));
            } else {
                player.sendMessage(ChatColor.RED + "This is the first page.");
            }
        }
    }
}
