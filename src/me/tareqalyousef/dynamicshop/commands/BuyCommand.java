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
        double pricePerUnit;
        double totalPrice;
        Material type;
        ItemStack content;

        try {
            amount = Integer.parseInt(strings[0]);
            type = Material.getMaterial(strings[1]);
            content = new ItemStack(type, amount);
        } catch (Exception e) {
            player.sendMessage(Utilities.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Utilities.DEFAULT_COLOR +
                    " Could not parse command");

            return false;
        }
        pricePerUnit = Utilities.GetItemPrice(type.toString().toUpperCase());
        totalPrice = amount * pricePerUnit;

        if (!(Utilities.GetPlayerBalance(player.getUniqueId().toString()) < totalPrice)) {
            player.sendMessage(Utilities.PREFIX_COLOR +
                    plugin.getConfig().getString("prefix") +
                    Utilities.DEFAULT_COLOR +
                    " You do not have " +
                    Utilities.HIGHLIGHT_COLOR +
                    String.valueOf(totalPrice));

            return true;
        }
        player.getInventory().addItem(content);
        player.getWorld().dropItemNaturally(player.getLocation(), content);
        // subtract from player balance

        player.sendMessage(Utilities.PREFIX_COLOR +
                plugin.getConfig().getString("prefix") +
                Utilities.DEFAULT_COLOR +
                " Bought " +
                Utilities.HIGHLIGHT_COLOR +
                String.valueOf(amount) +
                Utilities.DEFAULT_COLOR +
                " " +
                Utilities.HIGHLIGHT_COLOR +
                type.toString() +
                Utilities.DEFAULT_COLOR +
                " for $" +
                Utilities.HIGHLIGHT_COLOR +
                String.valueOf(totalPrice) +
                Utilities.DEFAULT_COLOR +
                " ($" +
                Utilities.HIGHLIGHT_COLOR +
                String.valueOf(pricePerUnit) +
                Utilities.DEFAULT_COLOR +
                " each)");

        return true;
    }
}
