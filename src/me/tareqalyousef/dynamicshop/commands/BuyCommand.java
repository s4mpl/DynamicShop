package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.TransactionType;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class BuyCommand implements CommandExecutor {
    private DynamicShop plugin;

    public BuyCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        int amount;
        int amountDropped;
        double pricePerUnit;
        double totalPrice;
        Material type;
        ItemStack content;
        String name;
        double playerBalance;

        try {
            type = Material.getMaterial(strings[0].toUpperCase());
            amount = Integer.parseInt(strings[1]);
            amountDropped = 0;
            name = type.toString().toLowerCase();
            content = new ItemStack(type, Math.min(amount, type.getMaxStackSize()));
        } catch (Exception e) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR +
                    " Could not parse command (try " + Settings.HIGHLIGHT_COLOR + "/buy <item> <amount>" + Settings.DEFAULT_COLOR + ")");

            return false;
        }

        if (amount <= 0) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Must be a positive value");

            return true;
        }

        if(amount > type.getMaxStackSize() * Settings.MAX_QUANTITY_RATIO) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR +
                    " Cannot buy more than " + Settings.HIGHLIGHT_COLOR + String.valueOf(type.getMaxStackSize() * Settings.MAX_QUANTITY_RATIO) + " " +
                    name + Settings.DEFAULT_COLOR + " at a time");

            return true;
        }

        pricePerUnit = Util.getItemPrice(type.toString());
        playerBalance = Util.getPlayerBalance(player.getUniqueId().toString());

        if (pricePerUnit == -1) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You cannot buy this item");

            return true;
        }

        totalPrice = Util.quoteItemPrice(strings[0].toUpperCase(), TransactionType.BUY, amount);
        if (playerBalance < totalPrice) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You do not have " +
                    Settings.MONEY_COLOR + String.format("$%.2f", totalPrice));

            return true;
        }

        while(amountDropped < amount) {
            player.getWorld().dropItem(player.getLocation(), content);
            amountDropped += content.getAmount();
            content.setAmount(Math.min(amount - amountDropped, type.getMaxStackSize()));
        }

        Util.setPlayerBalance(player.getUniqueId().toString(), playerBalance - totalPrice);
        Util.setItemPrice(strings[0].toUpperCase(), Util.quoteItemPriceChange(strings[0].toUpperCase(), TransactionType.BUY, amount));

        player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Bought " +
                Settings.HIGHLIGHT_COLOR + String.valueOf(amount) + " " + Settings.HIGHLIGHT_COLOR + name +
                Settings.DEFAULT_COLOR + " for " + Settings.MONEY_COLOR + "$" + String.format("%.2f", totalPrice) + Settings.DEFAULT_COLOR + " (" +
                Settings.MONEY_COLOR + "$" + String.format("%.2f", totalPrice / amount) + Settings.DEFAULT_COLOR + " each)");

        return true;
    }
}
