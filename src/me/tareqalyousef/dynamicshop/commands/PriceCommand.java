package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PriceCommand implements CommandExecutor {
    private DynamicShop plugin;

    public PriceCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;
        Material type;
        double price;

        try {
            type = Material.getMaterial(strings[0].toUpperCase());
            price = Util.getItemPrice(type.toString());

            if (price == -1) {
                player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " You cannot sell this item");
                return true;
            }
        } catch (Exception e) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Could not parse command");
            return false;
        }

        player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " The price of " +
                Settings.HIGHLIGHT_COLOR + strings[0].toLowerCase() + Settings.DEFAULT_COLOR + " is " + Settings.MONEY_COLOR + String.format("$%.2f", price));

        return true;
    }
}