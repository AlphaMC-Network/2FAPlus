package me.egg82.tfaplus.events;

import java.util.function.Consumer;
import me.egg82.tfaplus.TFAAPI;
import me.egg82.tfaplus.extended.CachedConfigValues;
import me.egg82.tfaplus.services.CollectionProvider;
import me.egg82.tfaplus.utils.LogUtil;
import ninja.egg82.service.ServiceLocator;
import ninja.egg82.service.ServiceNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerCommandPreprocessFrozenHandler implements Consumer<PlayerCommandPreprocessEvent> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TFAAPI api = TFAAPI.getInstance();

    public void accept(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        CachedConfigValues cachedConfig;

        try {
            cachedConfig = ServiceLocator.get(CachedConfigValues.class);
        } catch (IllegalAccessException | InstantiationException | ServiceNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            event.setCancelled(true); // Assume event cancellation
            return;
        }

        if (CollectionProvider.getFrozen().containsKey(event.getPlayer().getUniqueId())) {
            if (cachedConfig.getFreeze().getCommand()) {
                event.getPlayer().sendMessage(LogUtil.getHeading() + ChatColor.DARK_RED + "You must first authenticate with your 2FA code before running commands!");
                event.setCancelled(true);
            }
        } else {
            int colon = event.getMessage().indexOf(':');
            String split = event.getMessage().substring(colon + 1);

            if (colon > -1) {
                for (String command : cachedConfig.getCommands()) {
                    if (split.startsWith(command)) {
                        if (!api.isVerified(event.getPlayer().getUniqueId(), true)) {
                            CollectionProvider.getCommandFrozen().put(event.getPlayer().getUniqueId(), event.getMessage());
                            event.getPlayer().sendMessage(LogUtil.getHeading() + ChatColor.YELLOW + "Please enter your 2FA code into the chat.");
                            event.setCancelled(true);
                        }
                        break;
                    }
                }
            } else {
                for (String command : cachedConfig.getCommands()) {
                    if (event.getMessage().startsWith(command)) {
                        if (!api.isVerified(event.getPlayer().getUniqueId(), true)) {
                            CollectionProvider.getCommandFrozen().put(event.getPlayer().getUniqueId(), event.getMessage());
                            event.getPlayer().sendMessage(LogUtil.getHeading() + ChatColor.YELLOW + "Please enter your 2FA code into the chat.");
                            event.setCancelled(true);
                        }
                        break;
                    }
                }
            }
        }
    }
}