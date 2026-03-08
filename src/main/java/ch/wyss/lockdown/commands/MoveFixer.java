package ch.wyss.lockdown.commands;

import ch.wyss.lockdown.LockDown;
import ch.wyss.lockdown.utils.Lang;
import ch.wyss.lockdown.utils.MovementHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoveFixer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.isOp()) {
            commandSender.sendMessage(Lang.PLAYER_NOT_OP.getTranslation((Player) commandSender));
            return false;
        }
        if (strings.length > 0) {
            try {
                Player player = LockDown.getInstance().getServer().getPlayer(strings[0]);
                if (player != null) {
                    MovementHelper.moveablePlayer(player);
                    return true;
                }
            } catch (Exception e) {
                commandSender.sendMessage(Lang.NONE_VALID_PLAYER.getTranslation((Player) commandSender));
                return false;
            }
        }
        return false;
    }
}
