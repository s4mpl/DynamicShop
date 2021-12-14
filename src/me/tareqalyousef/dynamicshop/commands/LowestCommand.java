package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LowestCommand implements CommandExecutor {
    private DynamicShop plugin;

    public LowestCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;
        HashMap<String, Double> changes = Util.getItemPriceChanges();

        changes = changes.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),
                        (entry1, entry2) -> entry2, LinkedHashMap::new));

        player.sendMessage(Settings.PREFIX_COLOR +
                plugin.getConfig().getString("prefix") +
                Settings.DEFAULT_COLOR +
                " Lowest Item Percentage Changes");

        int place = 1;
        for (String item : changes.keySet()) {
            ChatColor color = changes.get(item) >= 1 ? ChatColor.GREEN : ChatColor.RED;
            String sign = changes.get(item) >= 1 ? "+" : "-";

            player.sendMessage(Settings.HIGHLIGHT_COLOR + String.valueOf(place) + ") " + Settings.DEFAULT_COLOR + item.toLowerCase() +
                    " " + color + "(" + sign + String.format("%.2f%%", 100 * Math.abs(1 - changes.get(item))) + ")");

            if (place == 9)
                break;

            place++;
        }

        return true;
    }
}