package com.caxerx.mc.interconomy.commandhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

/**
 * Created by caxerx on 2017/3/31.
 */
public class TabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length <= 1) {
            String lastaug = strings[strings.length - 1];
            Collection<SubCommand> tab = CommandManager.getInstance().getAllSubCommand();
            HashSet<String> tabset = new HashSet<>();
            tab.forEach(sub -> {
                if ((sub.getPermission() == null || commandSender.hasPermission(sub.getPermission())) && (lastaug == "" || sub.getName().startsWith(lastaug))) {
                    tabset.add(sub.getName());
                }
            });
            return new ArrayList<>(tabset);
        } else {
            try {
                String lastaug = strings[strings.length - 1];
                SubCommand sub = CommandManager.getInstance().getSubCommand(strings[0], strings.length - 1);
                if (commandSender.hasPermission(sub.getPermission())) {
                    List<String> tab = sub.getTabList(strings.length - 1);
                    if (tab != null) {
                        tab = new ArrayList<>(tab);
                        tab.removeIf(aug -> lastaug != "" && !aug.startsWith(lastaug));
                        return tab;
                    }
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    }

}
