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
        double pricePerUnit;
        double totalPrice;
        Material type;
        ItemStack content;
        String name;
        double playerBalance;

        try {
            type = Material.getMaterial(strings[0].toUpperCase());
            amount = Integer.parseInt(strings[1]);
            name = type.toString().toLowerCase();
            content = new ItemStack(type, amount);
        } catch (Exception e) {
            player.sendMessage(Util.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Util.DEFAULT_COLOR +
                    " Could not parse command");

            return false;
        }

        pricePerUnit = Util.getItemPrice(type.toString());
        totalPrice = amount * pricePerUnit;
        playerBalance = Util.getPlayerBalance(player.getUniqueId().toString());

        if (!player.getInventory().contains(content)) {
            player.sendMessage(Util.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Util.DEFAULT_COLOR +
                    " You do not have " +
                    Util.HIGHLIGHT_COLOR +
                    String.valueOf(amount) +
                    Util.DEFAULT_COLOR +
                    " " +
                    Util.HIGHLIGHT_COLOR +
                    name);

            return true;
        }

        player.getInventory().removeItem(content);
        Util.setPlayerBalance(player.getUniqueId().toString(), playerBalance + totalPrice);

        player.sendMessage(Util.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Util.DEFAULT_COLOR + " Sold " +
                Util.HIGHLIGHT_COLOR + String.valueOf(amount) + Util.DEFAULT_COLOR + " " + Util.HIGHLIGHT_COLOR + name +
                Util.DEFAULT_COLOR + " for " + Util.MONEY_COLOR + "$" + String.format("%.2f", totalPrice) + Util.DEFAULT_COLOR + " (" +
                Util.MONEY_COLOR + "$" + String.format("%.2f", pricePerUnit) + Util.DEFAULT_COLOR + " each)");

        return true;
    }
}
