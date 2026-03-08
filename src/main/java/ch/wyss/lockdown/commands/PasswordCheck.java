package ch.wyss.lockdown.commands;

import ch.wyss.lockdown.LockDown;
import ch.wyss.lockdown.utils.Lang;
import ch.wyss.lockdown.utils.MovementHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class PasswordCheck implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (((Player) commandSender).isWhitelisted()) {
            commandSender.sendMessage(Lang.YOU_ARE_ALREADY_WHITELISTED.getTranslation((Player) commandSender));
            return true;
        }
        if (strings.length > 0) {
            String password = LockDown.getInstance().getConfig().getString("Settings.Password");
            if (password == null) {
                getLogger().warning("No password set in the config");
                return false;
            }
            if (password.equals(strings[0])) {
                commandSender.sendMessage(Lang.CORRECT_PASSWORD.getTranslation((Player) commandSender));
                MovementHelper.makePlayerMoveable((Player) commandSender);
                ((Player) commandSender).setWhitelisted(true);
                LockDown.getInstance().getServer().broadcastMessage("§e" + commandSender.getName() + " joined the game");
                return true;
            } else {
                commandSender.sendMessage(Lang.INCORRECT_PASSWORD.getTranslation((Player) commandSender));
                return false;
            }
        } else {
            commandSender.sendMessage(Lang.NO_PASSWORD_PROVIDED.getTranslation((Player) commandSender));
            return false;
        }
    }
}