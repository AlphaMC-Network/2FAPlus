package me.egg82.tfaplus.events;

import me.egg82.tfaplus.extended.CachedConfigValues;
import me.egg82.tfaplus.services.CollectionProvider;
import me.egg82.tfaplus.utils.ConfigUtil;
import me.egg82.tfaplus.utils.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockPlaceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

public class BlockPlaceFrozenHandler implements Consumer<BlockPlaceEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void accept(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!CollectionProvider.getFrozen().containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        Optional<CachedConfigValues> cachedConfig = ConfigUtil.getCachedConfig();
        if (!cachedConfig.isPresent()) {
            event.setCancelled(true); // Assume event cancellation
            return;
        }

        if (cachedConfig.get().getFreeze().getBlocks()) {
            event.getPlayer().sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You must first authenticate with your 2FA code before placing that!");
            event.setCancelled(true);
        }
    }
}
