package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.UpdateResult;
import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.cache.TransitionalType;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

/**
 * Created by caxerx on 2017/4/1.
 */
public final class BalanceModifier {

    private final MessageConfig msg;
    private final CurrencyController controller;

    public BalanceModifier(MessageConfig msg, CurrencyController controller) {
        this.msg = msg;
        this.controller = controller;
    }

    public void execute(TransitionalType type, CommandSender sender, List<String> args) {
        String operator = sender.getName();
        String target = args.get(0);
        double value;
        try{
            value = Double.parseDouble(args.get(1));
        }catch (NumberFormatException e){
            sender.sendMessage(msg.get("not-number"));
            return;
        }
        UUID uuid = Bukkit.getPlayerUniqueId(target);
        if (uuid == null) {
            sender.sendMessage(msg.getPrefix() + DragonNiteMC.getAPI().getCoreConfig().getNotFoundPlayer());
            return;
        }
        OfflinePlayer user = Bukkit.getOfflinePlayer(uuid);
        UpdateResult result;
        switch (type) {
            case WITHDRAW:
                result = controller.withdrawPlayer(user, value, operator);
                break;
            case SET:
                result = controller.setPlayer(user, value, operator);
                break;
            case DEPOSIT:
                result = controller.depositPlayer(user, value, operator);
                break;
            default:
                sender.sendMessage(msg.get("command-augs-error-message"));
                return;
        }
        if (result == UpdateResult.SUCCESS) {
            sender.sendMessage(msg.get("transitional-success-message"));
        } else if (result == UpdateResult.DATA_CACHING) {
            sender.sendMessage(msg.get("data-caching-message"));
        }else {
            sender.sendMessage(msg.get("transitional-failure-message"));
        }
    }
}
