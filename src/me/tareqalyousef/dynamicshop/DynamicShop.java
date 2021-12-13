package me.tareqalyousef.dynamicshop;

import me.tareqalyousef.dynamicshop.commands.BalanceCommand;
import me.tareqalyousef.dynamicshop.commands.BuyCommand;
import me.tareqalyousef.dynamicshop.commands.SellCommand;
import me.tareqalyousef.dynamicshop.listeners.JoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicShop extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("buy").setExecutor(new BuyCommand(this));
        this.getCommand("sell").setExecutor(new SellCommand(this));
        this.getCommand("balance").setExecutor(new BalanceCommand(this));
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }
}
