package me.tareqalyousef.dynamicshop;

import org.bukkit.ChatColor;

import static org.bukkit.Bukkit.getServer;

public class Settings {
    public static final double SALES_TAX = 0.85;
    public static final double BASE_RATE = 0.0007;

    public static final String DATA_PATH = getServer().getWorldContainer() + "/plugins/DynamicShop";
    public static final String BALANCES_PATH = DATA_PATH + "/balances.yml";
    public static final String DEFAULT_PRICES_PATH = DATA_PATH + "/defaults.yml";
    public static final String CURRENT_PRICES_PATH = DATA_PATH + "/prices.yml";
    public static final String ALIASES_PATH = DATA_PATH + "/aliases.yml";

    public static final ChatColor PREFIX_COLOR = ChatColor.BLUE;
    public static final ChatColor DEFAULT_COLOR = ChatColor.GRAY;
    public static final ChatColor HIGHLIGHT_COLOR = ChatColor.GOLD;
    public static final ChatColor MONEY_COLOR = ChatColor.GREEN;
}
