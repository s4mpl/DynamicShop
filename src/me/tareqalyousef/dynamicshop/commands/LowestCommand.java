package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Settings;
import me.tareqalyousef.dynamicshop.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
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

        boolean type;
        int place = 1;

        try {
            if (strings[0].equalsIgnoreCase("price"))
                type = true;
            else if (strings[0].equalsIgnoreCase("change"))
                type = false;
            else
                throw new RuntimeException("Could not parse");
        } catch (Exception e) {
            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR +
                    " Could not parse command (try " + Settings.HIGHLIGHT_COLOR + "/lowest [price | change]" + Settings.DEFAULT_COLOR + ")");
            return false;
        }

        if(type) {
            HashMap<String, Double[]> prices = Util.getItemPricesAndChanges();

            // Create a list from elements of HashMap
            List<Map.Entry<String, Double[]> > list = new LinkedList<Map.Entry<String, Double[]>>(prices.entrySet());
            // Sort the list
            Collections.sort(list, new Comparator <Map.Entry<String, Double[]>>() {
                public int compare(Map.Entry<String, Double[]> o1, Map.Entry<String, Double[]> o2) {
                    return o1.getValue()[0].compareTo(o2.getValue()[0]);
                }
            });
            prices = list.stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),
                    (entry1, entry2) -> entry2, LinkedHashMap::new));

            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Lowest Item Prices");

            for (String item : prices.keySet()) {
                ChatColor color = prices.get(item)[1] >= 1 ? ChatColor.GREEN : ChatColor.RED;
                String sign = prices.get(item)[1] >= 1 ? "+" : "-";

                player.sendMessage(Settings.HIGHLIGHT_COLOR + String.valueOf(place) + ") " + Settings.DEFAULT_COLOR + item.toLowerCase() + " " +
                        Settings.MONEY_COLOR + "$" + String.format("%.2f%%", prices.get(item)[0]) + color + " (" + sign +
                        String.format("%.2f%%", 100 * Math.abs(1 - prices.get(item)[1])) + ")");

                if (place == 9)
                    break;

                place++;
            }
        } else {
            HashMap<String, Double> changes = Util.getItemPriceChanges();

            changes = changes.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),
                            (entry1, entry2) -> entry2, LinkedHashMap::new));

            player.sendMessage(Settings.PREFIX_COLOR + plugin.getConfig().getString("prefix") + Settings.DEFAULT_COLOR + " Lowest Item Percentage Changes");

            for (String item : changes.keySet()) {
                ChatColor color = changes.get(item) >= 1 ? ChatColor.GREEN : ChatColor.RED;
                String sign = changes.get(item) >= 1 ? "+" : "-";

                player.sendMessage(Settings.HIGHLIGHT_COLOR + String.valueOf(place) + ") " + Settings.DEFAULT_COLOR + item.toLowerCase() + " " + color + "(" + sign +
                        String.format("%.2f%%", 100 * Math.abs(1 - changes.get(item))) + ")");

                if (place == 9)
                    break;

                place++;
            }
        }

        return true;
    }
}