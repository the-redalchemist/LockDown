package ch.wyss.lockdown.checker;

import ch.wyss.lockdown.LockDown;
import ch.wyss.lockdown.utils.Lang;
import ch.wyss.lockdown.utils.MovementHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public class PasswordChecker implements Listener {
    private final Object lock = new Object(); // Create a lock object for synchronization

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!LockDown.getInstance().getConfig().getBoolean("Settings.passwordProtected")) return;

        Player player = event.getPlayer();
        if (player.isWhitelisted()) return;

        preventPlayerMovement(player);
        writePlayerToLoginTries(player);

        event.setJoinMessage(null);

        String title = LockDown.getInstance().getConfig().getString("Settings.Welcome-Message.title");
        String subTitle = LockDown.getInstance().getConfig().getString("Settings.Welcome-Message.sub-title");
        List<Map<?, ?>> messages = LockDown.getInstance().getConfig().getMapList("Settings.Welcome-Message.chat-message");

        int timeUntilKick = LockDown.getInstance().getConfig().getInt("Settings.time-until-kick");
        int timeUntilKickInTicks = timeUntilKick * 20;

        player.sendTitle(title, subTitle, 20, 300, 20);

        for (Map<?, ?> message : messages) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) message.get("message")));
        }

        if (LockDown.getInstance().getConfig().getBoolean("Settings.Countdown-Settings.countdown")) {
            loadCountdown(timeUntilKick, player);
        }

        Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(LockDown.class), () -> {
            if (player.isOnline() && !player.isWhitelisted()) {
                MovementHelper.makePlayerMoveable(player);
                player.kickPlayer(Lang.PASSWORD_ON_TIMEOUT.getTranslation(player));
            }

        }, timeUntilKickInTicks);
    }

    /**
     * Prevent the player from moving, set the player to spectator mode, teleport the player to the spawn location,
     * set the player's walk speed to 0, set the player's fly speed to 0, set the player's gravity to false, set the player
     * to invulnerable, set the player to not collidable, set the player to not be able to pick up items
     *
     * @param player The player that is trying to login
     */
    private void preventPlayerMovement(Player player) {
        try {
            teleportPlayer(player);
        } catch (Exception e) {
            getLogger().warning("Error while teleporting player: " + e.getMessage());
        }
        restrictPlayerMovementTemporarily();
        player.setGameMode(GameMode.ADVENTURE);
        player.setCanPickupItems(false);
    }

    private void restrictPlayerMovementTemporarily() {
        TemporaryMoveRestrictionListener moveListener = new TemporaryMoveRestrictionListener();
        Bukkit.getPluginManager().registerEvents(moveListener, LockDown.getInstance());
        Bukkit.getScheduler().runTaskLater(LockDown.getInstance(), () -> PlayerMoveEvent.getHandlerList().unregister(moveListener), 61 * 20L);
    }


    private void teleportPlayer(Player player) {
        if (LockDown.getInstance().getConfig().getBoolean("Settings.teleport-to-location.onJoin.enabled")) {
            if (LockDown.getInstance().getConfig().getBoolean("Settings.teleport-to-location.onJoin.worldSpawn")) {
                player.teleport(player.getWorld().getSpawnLocation());
            } else {
                String[] location = LockDown.getInstance().getConfig().getString("Settings.teleport-to-location.onJoin.location").split(",");
                player.teleport(new org.bukkit.Location(LockDown.getInstance().getServer().getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5])));
            }
        }
    }

    /**
     * Write the player to the loginTries.yml list if the player is already in the list increment the number of tries
     * if the player has tried to log in more than 3 times, kick the player
     *
     * @param player The player that is trying to log in
     */
    private void writePlayerToLoginTries(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(LockDown.getInstance(), () -> {
            synchronized (lock) { // Combine async task with a synchronized block
                File file = loadLoginTries();
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection players = yaml.getConfigurationSection("players");
                assert players != null;
                HashMap<String, Integer> loginTriesList = getLoginTriesList(players);

                String playerIdentifier = player.getUniqueId().toString();
                int tries = loginTriesList.getOrDefault(playerIdentifier, 0);
                int maxTries = LockDown.getInstance().getConfig().getInt("Settings.amounts-of-tries");
                if (tries >= maxTries) {
                    int banTime = LockDown.getInstance().getConfig().getInt("Settings.banned-time");
                    Duration duration = Duration.ofDays(banTime);
                    Bukkit.getScheduler().runTask(LockDown.getInstance(), () -> {
                        player.ban(Lang.BAN_MESSAGE.getTranslation(player), duration, "You have tried to login more than 3 times");
                    });
                } else {
                    loginTriesList.put(playerIdentifier, tries + 1);
                }

                List<String> loginTriesListString = loginTriesList.entrySet().stream()
                        .map(entry -> entry.getKey() + ":" + entry.getValue())
                        .toList();

                players.set("loginTries", loginTriesListString);
                try {
                    yaml.save(file);
                } catch (Exception e) {
                    getLogger().warning("Failed to save loginTries.yml file: " + e.getMessage());
                }
            }
        });
    }


    /**
     * Load the loginTries.yml file
     *
     * @return The loginTries.yml file
     */
    private File loadLoginTries() {
        String fileName = "loginTries.yml";
        File file = new File(JavaPlugin.getPlugin(LockDown.class).getDataFolder(), fileName);
        if (!file.exists()) {
            JavaPlugin.getPlugin(LockDown.class).saveResource(fileName, false);
        }
        return file;
    }

    /**
     * Get the loginTries list from the players section of the loginTries.yml file
     *
     * @param players The players section of the loginTries.yml file
     * @return The loginTries list
     */
    private HashMap<String, Integer> getLoginTriesList(ConfigurationSection players) {
        List<String> list = players.getStringList("loginTries");
        HashMap<String, Integer> loginTriesList = new HashMap<>();
        for (String player : list) {
            String[] playerData = player.split(":");
            loginTriesList.put(playerData[0], Integer.parseInt(playerData[1]));
        }
        //print the loginTriesList to the console for debugging
        getLogger().info("Current login tries: " + loginTriesList.toString());

        return loginTriesList;
    }

    /**
     * Schedules countdown messages to be sent to the player at specific intervals.
     *
     * @param maxTime The maximum time in seconds before the player is kicked.
     * @param player  The player to whom the countdown messages will be sent.
     */
    private void loadCountdown(int maxTime, Player player) {
        List<Integer> countdownList = new ArrayList<>();
        try {
            String countdownString = LockDown.getInstance().getConfig().getString("Settings.Countdown-Settings.countdown-timing");
            assert countdownString != null;
            for (String s : countdownString.replaceAll("\\s+", "").split(",")) {
                countdownList.add(Integer.parseInt(s));
            }
            countdownList.sort((o1, o2) -> o2 - o1);
        } catch (Exception e) {
            getLogger().warning("Failed to load countdown from config");
            return;
        }
        for (int i : countdownList) {
            Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(LockDown.class), () -> {
                if (player.isOnline() && !player.isWhitelisted()) {
                    String message = LockDown.getInstance().getConfig().getString("Settings.Countdown-Settings.countdown-messages-template");
                    if (message != null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%time%", String.valueOf(i))));
                    }
                }
            }, (maxTime - i) * 20L);
        }
    }
}
