package me.tareqalyousef.dynamicshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class Util {
    public static String DATA_PATH = getServer().getWorldContainer() + "/plugins/DynamicShop";
    public static String BALANCES_PATH = DATA_PATH + "/balances.yml";
    public static String DEFAULT_PRICES_PATH = DATA_PATH + "/defaults.yml";
    public static String CURRENT_PRICES_PATH = DATA_PATH + "/prices.yml";
    public static String ALIASES_PATH = DATA_PATH + "/aliases.yml";

    public static final ChatColor PREFIX_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor DEFAULT_COLOR = ChatColor.GRAY;
    public static final ChatColor HIGHLIGHT_COLOR = ChatColor.YELLOW;
    public static final ChatColor MONEY_COLOR = ChatColor.GREEN;

    public static File getBalancesFile() {
        File file = new File(BALANCES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load balances config");

        return file;
    }

    public static File getDefaultPricesFile() {
        File file = new File(DEFAULT_PRICES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load default prices config");

        return file;
    }

    public static File getAliasesFile() {
        File file = new File(ALIASES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load aliases config");

        return file;
    }

    public static File getCurrentPricesFile() {
        File file = new File(CURRENT_PRICES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load current prices config");

        return file;
    }

    public static String getPlayerAlias(String playerId) {
        File aliases = Util.getAliasesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(aliases);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist");

        return config.getString(playerId);
    }

    public static void checkPlayerData(Player player) {
        checkPlayerBalanceData(player);
        checkPlayerAliasData(player);
    }

    public static void checkPlayerBalanceData(Player player) {
        File balances = Util.getBalancesFile();
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
        File aliases = Util.getAliasesFile();
        YamlConfiguration aliasesConfig = YamlConfiguration.loadConfiguration(aliases);

        aliasesConfig.set(player.getUniqueId().toString(), player.getDisplayName());

        try {
            aliasesConfig.save(aliases);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save aliases.yml");
        }
    }

    public static double getPlayerBalance(String playerId) {
        File balances = Util.getBalancesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(balances);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist");

        return config.getDouble(playerId);
    }

    public static void setPlayerBalance(String playerId, double newBalance) {
        File balances = Util.getBalancesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(balances);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist");

        config.set(playerId, newBalance);

        try {
            config.save(balances);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save balances.yml");
        }
    }

    public static double getItemPrice(String materialName) {
        File prices = Util.getCurrentPricesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            throw new RuntimeException("Requested material does not exist");

        return config.getDouble(materialName);
    }

    public static void setItemPrice(String materialName, double newPrice) {
        File prices = Util.getCurrentPricesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            throw new RuntimeException("Requested material does not exist");

        config.set(materialName, newPrice);

        try {
            config.save(prices);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save prices.yml");
        }
    }

    public static void createDefaultPricesFile() {
        File defaults = new File(Util.DEFAULT_PRICES_PATH);

        try{
            defaults.createNewFile();
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not create default prices file");
        }

        // todo: data
    }

    public static void createCurrentPricesFile() {
        File prices = new File(Util.CURRENT_PRICES_PATH);

        try{
            prices.createNewFile();
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not create current prices file");
        }

        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(Util.getDefaultPricesFile());
        YamlConfiguration pricesConfig = YamlConfiguration.loadConfiguration(Util.getCurrentPricesFile());

        for (String material : defaultConfig.getKeys(false))
            pricesConfig.set(material, defaultConfig.getDouble(material));

        try {
            pricesConfig.save(prices);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save prices.yml");
        }
    }
}
