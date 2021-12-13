package me.tareqalyousef.dynamicshop.commands;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderboardCommand implements CommandExecutor {
    private DynamicShop plugin;

    public LeaderboardCommand(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;

        Player player = (Player)commandSender;

        File balancesFile = Utilities.getBalancesFile();
        File aliasesFile = Utilities.getAliasesFile();

        YamlConfiguration balancesConfig = YamlConfiguration.loadConfiguration(balancesFile);
        YamlConfiguration aliasesConfig = YamlConfiguration.loadConfiguration(aliasesFile);

        HashMap<String, Double> balances = new HashMap<String, Double>();

        for (String playerId : balancesConfig.getKeys(false))
            balances.put(aliasesConfig.getString(playerId), balancesConfig.getDouble(playerId));

        balances = balances.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue(),
                        (entry1, entry2) -> entry2, LinkedHashMap::new));

        player.sendMessage(Utilities.PREFIX_COLOR +
                plugin.getConfig().getString("prefix") +
                Utilities.DEFAULT_COLOR +
                " Leaderboard ");

        int place = 1;
        for (String name : balances.keySet()) {
            player.sendMessage(Utilities.HIGHLIGHT_COLOR +
                    String.valueOf(place) +
                    ") " +
                    Utilities.DEFAULT_COLOR +
                    name +
                    " " +
                    ChatColor.GREEN +
                    String.format("$%.2f", balances.get(name)));

            if (place == 9)
                break;

            place++;
        }

        return true;
    }
}