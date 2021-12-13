package me.tareqalyousef.dynamicshop;

import jdk.jshell.execution.Util;
import me.tareqalyousef.dynamicshop.commands.BalanceCommand;
import me.tareqalyousef.dynamicshop.commands.BuyCommand;
import me.tareqalyousef.dynamicshop.commands.LeaderboardCommand;
import me.tareqalyousef.dynamicshop.commands.SellCommand;
import me.tareqalyousef.dynamicshop.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class DynamicShop extends JavaPlugin {
    @Override
    public void onEnable() {
        // configs
        saveDefaultConfig();
        verifyDirectories();

        // commands
        this.getCommand("buy").setExecutor(new BuyCommand(this));
        this.getCommand("sell").setExecutor(new SellCommand(this));
        this.getCommand("balance").setExecutor(new BalanceCommand(this));
        this.getCommand("leaderboard").setExecutor(new LeaderboardCommand(this));

        // listeners
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);

        // verify all player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            Utilities.checkPlayerData(player);
        }
    }

    public void verifyDirectories() {
        //saveResource(Utilities.BALANCES_PATH, true);
        File data = new File(Utilities.DATA_PATH);
        File balances = new File(Utilities.BALANCES_PATH);
        File defaults = new File(Utilities.DEFAULT_PRICES_PATH);
        File aliases = new File(Utilities.ALIASES_PATH);

        try {
            if (!data.exists()) data.mkdir();
            if (!balances.exists()) balances.createNewFile();
            if (!defaults.exists()) defaults.createNewFile();
            if (!aliases.exists()) aliases.createNewFile();
        } catch (Exception e) {
            Bukkit.getLogger().info("Error with directory verification");
        }
    }
}
