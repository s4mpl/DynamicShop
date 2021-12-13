package me.tareqalyousef.dynamicshop;

import me.tareqalyousef.dynamicshop.commands.BalanceCommand;
import me.tareqalyousef.dynamicshop.commands.BuyCommand;
import me.tareqalyousef.dynamicshop.commands.LeaderboardCommand;
import me.tareqalyousef.dynamicshop.commands.SellCommand;
import me.tareqalyousef.dynamicshop.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
            Util.checkPlayerData(player);
        }
    }

    public void verifyDirectories() {
        File data = new File(Util.DATA_PATH);
        File balances = new File(Util.BALANCES_PATH);
        File defaults = new File(Util.DEFAULT_PRICES_PATH);
        File prices = new File(Util.CURRENT_PRICES_PATH);
        File aliases = new File(Util.ALIASES_PATH);

        try {
            if (!data.exists()) data.mkdir();
            if (!balances.exists()) balances.createNewFile();
            if (!aliases.exists()) aliases.createNewFile();
            if (!defaults.exists()) Util.createDefaultPricesFile();
            if (!prices.exists()) Util.createCurrentPricesFile();
        } catch (Exception e) {
            Bukkit.getLogger().info("Error with directory verification");
        }
    }
}
