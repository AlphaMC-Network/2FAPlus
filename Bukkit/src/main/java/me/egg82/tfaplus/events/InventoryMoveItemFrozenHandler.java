package me.egg82.tfaplus.events;

import me.egg82.tfaplus.extended.CachedConfigValues;
import me.egg82.tfaplus.services.CollectionProvider;
import me.egg82.tfaplus.utils.ConfigUtil;
import me.egg82.tfaplus.utils.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

public class InventoryMoveItemFrozenHandler implements Consumer<InventoryMoveItemEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void accept(InventoryMoveItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!CollectionProvider.getFrozen().containsKey(((Player) event.getSource().getHolder()).getUniqueId())) {
            return;
        }

        if (!(event.getSource().getHolder() instanceof Player)) {
            return;
        }

        Optional<CachedConfigValues> cachedConfig = ConfigUtil.getCachedConfig();
        if (!cachedConfig.isPresent()) {
            event.setCancelled(true); // Assume event cancellation
            return;
        }

        if (cachedConfig.get().getFreeze().getInventory()) {
            ((Player) event.getSource().getHolder()).sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You must first authenticate with your 2FA code before doing that!");
            event.setCancelled(true);
        }
    }
}
