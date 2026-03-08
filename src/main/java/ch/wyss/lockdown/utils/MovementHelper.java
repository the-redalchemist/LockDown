package ch.wyss.lockdown.utils;

import ch.wyss.lockdown.LockDown;
import org.bukkit.entity.Player;

import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class MovementHelper {

    /**
     * Make the player moveable again
     *
     * @param player The player to make moveable
     */
    public static void makePlayerMoveable(Player player) {
        try {
            telePortPlayer(player);
        } catch (Exception e) {
            getLogger().warning("Error while teleporting player: " + e.getMessage());
        }
        moveablePlayer(player);
    }

    public static void moveablePlayer(Player player) {
        player.setCanPickupItems(true);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);

    }

    private static void telePortPlayer(Player player) {
        if (LockDown.getInstance().getConfig().getBoolean("Settings.teleport-to-location.onPasswordCorrect.enabled")) {
            if (LockDown.getInstance().getConfig().getBoolean("Settings.teleport-to-location.onPasswordCorrect.worldSpawn")) {
                player.teleport(player.getWorld().getSpawnLocation());
            } else {
                String[] location = Objects.requireNonNull(LockDown.getInstance().getConfig().getString("Settings.teleport-to-location.onPasswordCorrect.location")).split(",");
                player.teleport(new org.bukkit.Location(LockDown.getInstance().getServer().getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5])));
            }
        }
    }
}
