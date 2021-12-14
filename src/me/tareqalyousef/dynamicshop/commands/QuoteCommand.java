package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.TransactionType;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class QuoteCommand implements CommandExecutor {
    private DynamicShop plugin;

    public QuoteCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        Material type;
        int amount;
        String name;
        TransactionType mode;
        boolean all;
        
        try {
            if (strings[0].equalsIgnoreCase(TransactionType.BUY.toString()))
                mode = TransactionType.BUY;
            else if (strings[0].equalsIgnoreCase(TransactionType.SELL.toString()))
                mode = TransactionType.SELL;
            else
                throw new RuntimeException("Could not parse");

            type = Material.getMaterial(strings[1].toUpperCase());
            name = type.toString().toLowerCase();
            all = strings[2].equalsIgnoreCase("all") && mode == TransactionType.SELL;
            amount = all ? Util.getInventoryQuantity(player, type) : Integer.parseInt(strings[2]);
        } catch (Exception e) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Could not parse command");
            return false;
        }

        if (amount <= 0 ) {
            if (!all) {
                player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Must be a positive value");
            } else {
                player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You do not have any " +
                        Settings.HIGHLIGHT_COLOR + name);
            }
            return true;
        }

        if (mode == TransactionType.SELL && amount > 2304) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You cannot quote this amount");
            return true;
        }

        if (mode == TransactionType.BUY && amount > type.getMaxStackSize() * Settings.MAX_QUANTITY_RATIO) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You cannot quote this amount");
            return true;
        }

        double finalPrice = Util.quoteItemPriceChange(type.name(), mode, amount);
        double totalPrice = Util.quoteItemPrice(type.name(), mode, amount);
        double change = finalPrice / Util.getItemPrice(type.name());
        ChatColor changeColor = change >= 1.0 ? ChatColor.GREEN : ChatColor.RED;
        String sign = change >= 1.0 ? "+" : "-";

        player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + (mode == TransactionType.SELL ? " Selling " : " Buying ") +
                Settings.HIGHLIGHT_COLOR + amount + " " + name + Settings.DEFAULT_COLOR + (mode == TransactionType.SELL ? " will yield " : " will cost ") + Settings.MONEY_COLOR +
                String.format("$%.2f", totalPrice) + Settings.DEFAULT_COLOR + (mode == TransactionType.SELL ? ", lowering the price to " : ", increasing the price to ") + Settings.MONEY_COLOR +
                String.format("$%.2f", finalPrice) + " " + changeColor + "(" + sign + String.format("%.2f%%)", 100 * Math.abs(1 - change)));


        return true;
    }
}