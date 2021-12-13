package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
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
        } catch (Exception e) {
            player.sendMessage(Util.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Util.DEFAULT_COLOR +
                    " Could not parse command");

            return false;
        }

        player.sendMessage(Util.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Util.DEFAULT_COLOR + " The price of " +
                Util.HIGHLIGHT_COLOR + strings[0].toLowerCase() + Util.DEFAULT_COLOR + " is " + Util.MONEY_COLOR + String.format("$%.2f", price));

        return true;
    }
}