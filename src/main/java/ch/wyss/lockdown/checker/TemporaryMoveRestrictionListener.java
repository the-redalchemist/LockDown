package ch.wyss.lockdown.checker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TemporaryMoveRestrictionListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getPlayer().isWhitelisted()) {
            event.setCancelled(true);
        }
    }
}
