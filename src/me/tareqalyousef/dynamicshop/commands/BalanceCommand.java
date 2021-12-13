package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    private DynamicShop plugin;

    public BalanceCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;
        double balance = Utilities.getPlayerBalance(player.getUniqueId().toString());

        player.sendMessage(Utilities.PREFIX_COLOR +
                           plugin.getConfig().getString("prefix") +
                           Utilities.DEFAULT_COLOR +
                           " You have a balance of " +
                           Utilities.HIGHLIGHT_COLOR +
                           String.format("$%.2f", balance));
        return true;
    }
}
