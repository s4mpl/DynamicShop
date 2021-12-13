package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
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
            player.sendMessage(Util.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Util.DEFAULT_COLOR +
                    " Could not parse command");

            return false;
        }

        if (amount <= 0) {
            player.sendMessage(Util.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Util.DEFAULT_COLOR +
                    " Must be a positive value");

            return true;
        }

        if(amount > type.getMaxStackSize() * 16) {
            player.sendMessage(Util.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Util.DEFAULT_COLOR +
                    " Cannot buy more than " + Util.HIGHLIGHT_COLOR + String.valueOf(type.getMaxStackSize() * 16) + " " +
                    name +Util.DEFAULT_COLOR + " at a time");

            return true;
        }

        pricePerUnit = Util.getItemPrice(type.toString());
        totalPrice = amount * pricePerUnit;
        playerBalance = Util.getPlayerBalance(player.getUniqueId().toString());

        if (playerBalance < totalPrice) {
            player.sendMessage(Util.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Util.DEFAULT_COLOR +
                    " You do not have " + Util.MONEY_COLOR + String.format("$%.2f", totalPrice));

            return true;
        }

        while(amountDropped < amount) {
            player.getWorld().dropItem(player.getLocation(), content);
            amountDropped += content.getAmount();
            content.setAmount(Math.min(amount - amountDropped, type.getMaxStackSize()));
        }
        Util.setPlayerBalance(player.getUniqueId().toString(), playerBalance - totalPrice);

        player.sendMessage(Util.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Util.DEFAULT_COLOR + " Bought " +
                Util.HIGHLIGHT_COLOR + String.valueOf(amount) + Util.DEFAULT_COLOR + " " + Util.HIGHLIGHT_COLOR + name +
                Util.DEFAULT_COLOR + " for " + Util.MONEY_COLOR + "$" + String.format("%.2f", totalPrice) + Util.DEFAULT_COLOR + " (" +
                Util.MONEY_COLOR + "$" + String.format("%.2f", pricePerUnit) + Util.DEFAULT_COLOR + " each)");

        return true;
    }
}
