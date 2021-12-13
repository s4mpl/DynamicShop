package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        Material type;
        ItemStack content;

        try {
            amount = Integer.parseInt(strings[1]);
            type = Material.getMaterial(strings[0]);
            content = new ItemStack(type, amount);
        } catch (Exception e) {
            player.sendMessage(Utilities.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Utilities.DEFAULT_COLOR +
                    " Could not parse command");

            return false;
        }

        if (!player.getInventory().contains(content)) {
            player.sendMessage(Utilities.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Utilities.DEFAULT_COLOR +
                    " You do not have " +
                    Utilities.HIGHLIGHT_COLOR +
                    String.valueOf(amount) +
                    Utilities.DEFAULT_COLOR +
                    " quantity of " +
                    Utilities.HIGHLIGHT_COLOR +
                    type.toString());

            return true;
        }
        player.getInventory().removeItem(content);

        player.sendMessage(Utilities.PREFIX_COLOR +
                plugin.getConfig().getString("prefix") +
                Utilities.DEFAULT_COLOR +
                " Sold " +
                Utilities.HIGHLIGHT_COLOR +
                String.valueOf(amount) +
                Utilities.DEFAULT_COLOR +
                " quantity of " +
                Utilities.HIGHLIGHT_COLOR +
                type.toString() +
                Utilities.DEFAULT_COLOR +
                " for " +
                Utilities.HIGHLIGHT_COLOR +
                "$20.00");

        return true;
    }
}
