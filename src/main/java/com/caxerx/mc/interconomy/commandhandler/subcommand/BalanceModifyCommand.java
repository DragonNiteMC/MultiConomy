package com.caxerx.mc.interconomy.commandhandler.subcommand;

import com.caxerx.mc.interconomy.InterConomyConfig;
import com.caxerx.mc.interconomy.UpdateResult;
import com.caxerx.mc.interconomy.api.InterConomyAPI;
import com.caxerx.mc.interconomy.cache.DataCachingException;
import com.caxerx.mc.interconomy.cache.TransitionalType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * Created by caxerx on 2017/4/1.
 */
public class BalanceModifyCommand {
    public static void execute(TransitionalType type, CommandSender sender, String[] args) {
        try {
            String operator = sender.getName();
            UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
            if (uuid == null) {
                sender.sendMessage("§c找不到玩家");
                return;
            }
            OfflinePlayer user = Bukkit.getOfflinePlayer(uuid);
            double value = Double.parseDouble(args[1]);
            UpdateResult result;
            switch (type) {
                case WITHDRAW:
                    result = InterConomyAPI.getInstance().withdraw(user, value, operator);
                    break;
                case SET:
                    result = InterConomyAPI.getInstance().set(user, value, operator);
                    break;
                case DEPOSIT:
                    result = InterConomyAPI.getInstance().deposit(user, value, operator);
                    break;
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageCommandArgsError));
                    return;
            }
            if (result == UpdateResult.SUCCESS) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageTransitionalSuccess));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageTransitionalFailure));
            }

        } catch (DataCachingException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageDataCaching));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageCommandArgsError));
        }
    }
}
