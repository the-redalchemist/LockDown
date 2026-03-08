package ch.wyss.lockdown.commands;

import ch.wyss.lockdown.LockDown;
import ch.wyss.lockdown.utils.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class LockdownCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Lang.PLAYER_NOT_OP.getTranslation((Player) sender));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(Lang.NO_ACTION_PROVIDED.getTranslation((Player) sender));
            return false;
        }

        boolean enable = args[0].equals("enable");
        boolean disable = args[0].equals("disable");

        if (enable || disable) {
            LockDown.getInstance().getConfig().set("Settings.passwordProtected", enable);
            JavaPlugin.getPlugin(LockDown.class).saveConfig();
            sender.sendMessage(enable ? Lang.PASSWORD_PROTECTION_ENABLED.getTranslation((Player) sender) : Lang.PASSWORD_PROTECTION_DISABLED.getTranslation((Player) sender));
            return true;
        } else {
            sender.sendMessage(Lang.NO_VALID_ACTION.getTranslation((Player) sender));
            return false;
        }
    }
}