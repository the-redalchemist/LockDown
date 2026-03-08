package ch.wyss.lockdown.commands.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ToggleCompleter  implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            List<String> returnList = new ArrayList<>();
            returnList.add("enable");
            returnList.add("disable");
            return returnList;
        }
        return new ArrayList<>();
    }
}
