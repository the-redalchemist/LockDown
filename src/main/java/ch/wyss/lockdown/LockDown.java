package ch.wyss.lockdown;

import ch.wyss.lockdown.checker.PasswordChecker;
import ch.wyss.lockdown.commands.MoveFixer;
import ch.wyss.lockdown.commands.PasswordCheck;
import ch.wyss.lockdown.commands.LockdownCommand;
import ch.wyss.lockdown.commands.completer.ToggleCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LockDown extends JavaPlugin {

    private static FileConfiguration config;
    private static LockDown plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig(); // creates a config.yml file if it does not exist
        config = getConfig(); // loads the config.yml file

        setUpEvents();
        setUpCommands();
        setUpCompleters();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LockDown getInstance() {
        return plugin;
    }

    private void setUpEvents() {
        getServer().getPluginManager().registerEvents(new PasswordChecker(), this);
    }

    private void setUpCommands() {
        Objects.requireNonNull(getCommand("password")).setExecutor(new PasswordCheck());
        Objects.requireNonNull(getCommand("lockdown")).setExecutor(new LockdownCommand());
        Objects.requireNonNull(getCommand("moveFix")).setExecutor(new MoveFixer());
    }

    private void setUpCompleters() {
        Objects.requireNonNull(getCommand("lockdown")).setTabCompleter(new ToggleCompleter());
    }
}
