package me.tareqalyousef.dynamicshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class Utilities {
    public static String DATA_PATH = getServer().getWorldContainer() + "/plugins/DynamicShop";
    public static String BALANCES_PATH = DATA_PATH + "/balances.yml";
    public static String DEFAULT_PRICES_PATH = DATA_PATH + "/defaults.yml";
    public static String ALIASES_PATH = DATA_PATH + "/aliases.yml";

    public static final ChatColor PREFIX_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor DEFAULT_COLOR = ChatColor.GRAY;
    public static final ChatColor HIGHLIGHT_COLOR = ChatColor.YELLOW;

    public static File getBalancesFile() {
        File file = new File(BALANCES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load balances config!");

        return file;
    }

    public static File getDefaultPricesFile() {
        File file = new File(DEFAULT_PRICES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load default prices config!");

        return file;
    }

    public static File getAliasesFile() {
        File file = new File(ALIASES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load aliases config!");

        return file;
    }

    public static double getPlayerBalance(String playerId) {
        File balances = Utilities.getBalancesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(balances);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist!");

        return config.getDouble(playerId);
    }

    public static File getCurrentPricesConfig() {
        File file = new File(getServer().getWorldContainer() + "/plugins/DynamicShop/prices.yml");

        if (!file.exists())
            throw new RuntimeException("Failed to load current prices config!");

        return file;
    }

    public static double getItemPrice(String materialName) {
        File prices = Utilities.getCurrentPricesConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            throw new RuntimeException("Requested material does not exist!");

        return config.getDouble(materialName);
    }

    public static String getPlayerAlias(String playerId) {
        File aliases = Utilities.getAliasesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(aliases);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist!");

        return config.getString(playerId);
    }

    public static void checkPlayerData(Player player) {
        checkPlayerBalanceData(player);
        checkPlayerAliasData(player);
    }

    public static void checkPlayerBalanceData(Player player) {
        File balances = Utilities.getBalancesFile();
        YamlConfiguration balancesConfig = YamlConfiguration.loadConfiguration(balances);

        if (!balancesConfig.contains(player.getUniqueId().toString())) {
            balancesConfig.set(player.getUniqueId().toString(), 0.0);
        }

        try {
            balancesConfig.save(balances);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save balances.yml");
        }
    }

    public static void checkPlayerAliasData(Player player) {
        File aliases = Utilities.getAliasesFile();
        YamlConfiguration aliasesConfig = YamlConfiguration.loadConfiguration(aliases);

        aliasesConfig.set(player.getUniqueId().toString(), player.getDisplayName());

        try {
            aliasesConfig.save(aliases);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save aliases.yml");
        }
    }
}
