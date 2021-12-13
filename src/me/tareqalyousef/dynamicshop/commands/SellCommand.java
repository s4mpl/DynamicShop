package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class SellCommand implements CommandExecutor {
    private DynamicShop plugin;

    public SellCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        int amount;
        int actualAmount;
        double pricePerUnit;
        double totalPrice;
        Material type;
        ItemStack content;
        String name;
        double playerBalance;
        boolean all;

        try {
            type = Material.getMaterial(strings[0].toUpperCase());
            all = strings[1].equalsIgnoreCase("all");
            amount = all ? 9999 : Integer.parseInt(strings[1]);
            actualAmount = 0;
            name = type.toString().toLowerCase();
            content = new ItemStack(type, 1);
        } catch (Exception e) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Could not parse command");
            return false;
        }

        if (amount <= 0) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Must be a positive value");
            return true;
        }

        pricePerUnit = Util.getItemPrice(type.toString());
        playerBalance = Util.getPlayerBalance(player.getUniqueId().toString());

        if (pricePerUnit == -1) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You cannot sell this item");
            return true;
        }

        // Check if the player has the amount of items
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null) {
                if (actualAmount >= amount) break;
                if (content.isSimilar(i)) {
                    actualAmount += i.getAmount();
                }
            }
        }

        if (actualAmount < amount && !all) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You do not have " +
                    Settings.HIGHLIGHT_COLOR + String.valueOf(amount) + Settings.DEFAULT_COLOR + " " + Settings.HIGHLIGHT_COLOR + name);
        }

        // Optimize to only sell as many as the player has
        actualAmount = Math.min(amount, actualAmount);
        for (int i = 0; i < actualAmount; i++) {
            Util.removeItem(player, type);
            Util.setPlayerBalance(player.getUniqueId().toString(), playerBalance + Settings.SALES_TAX * pricePerUnit);
        }

        if (actualAmount > 0) {
            totalPrice = actualAmount * pricePerUnit * Settings.SALES_TAX;
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Sold " +
                    Settings.HIGHLIGHT_COLOR + String.valueOf(actualAmount) + Settings.DEFAULT_COLOR + " " + Settings.HIGHLIGHT_COLOR + name +
                    Settings.DEFAULT_COLOR + " for " + Settings.MONEY_COLOR + "$" + String.format("%.2f", totalPrice) + Settings.DEFAULT_COLOR + " (" +
                    Settings.MONEY_COLOR + "$" + String.format("%.2f", pricePerUnit) + Settings.DEFAULT_COLOR + " each at " +
                    Settings.HIGHLIGHT_COLOR + String.format("%.2f", Settings.SALES_TAX * 100) + "%" + Settings.DEFAULT_COLOR + " market value)");
        }

        return true;
    }
}
