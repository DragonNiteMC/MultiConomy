package com.caxerx.mc.interconomy.commandhandler;

import java.util.*;

/**
 * Created by caxerx on 2017/3/31.
 */
public class CommandManager {
    private static CommandManager instance;
    private static HashMap<String, SubCommand> commands = new HashMap<>();

    public CommandManager() {
        instance = this;
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public void registerCommand(String command, int args, SubCommand commandexe) {
        commands.put(command.toLowerCase() + "|" + args, commandexe);
    }

    public SubCommand getSubCommand(String command, int args) throws CommandNotFoundException, CommandArgsErrorException {
        List<Integer> sutiable = new ArrayList<>();
        boolean notfoundany = true;
        for (String key : commands.keySet()) {
            if (key.startsWith(command.toLowerCase())) {
                notfoundany = false;
                int cmdAug = Integer.parseInt(key.split("\\|")[1]);
                if (args >= cmdAug) {
                    sutiable.add(cmdAug);
                }
            }
        }
        Collections.sort(sutiable);
        if (sutiable.isEmpty()) {
            if (notfoundany) {
                throw new CommandNotFoundException();
            } else {
                throw new CommandArgsErrorException();
            }
        } else {
            return commands.get(command + "|" + sutiable.get(sutiable.size() - 1));
        }
    }

    public Collection<SubCommand> getAllSubCommand() {
        return commands.values();
        /*
        HashSet<String> subs = new HashSet<>();
        for (String key : commands.keySet()) {
            subs.add(key.split("\\|")[0]);
        }
        return subs;
        */
    }
}
