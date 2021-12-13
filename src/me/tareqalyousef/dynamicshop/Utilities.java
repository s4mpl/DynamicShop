package me.tareqalyousef.dynamicshop;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class Utilities {
    public static final ChatColor PREFIX_COLOR = ChatColor.DARK_BLUE;
    public static final ChatColor DEFAULT_COLOR = ChatColor.GRAY;
    public static final ChatColor HIGHLIGHT_COLOR = ChatColor.YELLOW;

    public static File GetBalancesConfig() {
        File file = new File(getServer().getWorldContainer() + "/plugins/DynamicShop/balances.yml");

        if (!file.exists())
            throw new RuntimeException("Failed to load balances config!");

        return file;
    }

    public static File GetDefaultPricesConfig() {
        File file = new File(getServer().getWorldContainer() + "/plugins/DynamicShop/defaults.yml");

        if (!file.exists())
            throw new RuntimeException("Failed to load default prices config!");

        return file;
    }

    public static double GetPlayerBalance(String playerId) {
        File balances = Utilities.GetBalancesConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(balances);

        if (!config.contains(playerId))
            throw new RuntimeException("Requested player id does not exist!");

        return config.getDouble(playerId);
    }

    public static File GetCurrentPricesConfig() {
        File file = new File(getServer().getWorldContainer() + "/plugins/DynamicShop/prices.yml");

        if (!file.exists())
            throw new RuntimeException("Failed to load current prices config!");

        return file;
    }

    public static double GetItemPrice(String materialName) {
        File prices = Utilities.GetCurrentPricesConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            throw new RuntimeException("Requested material does not exist!");

        return config.getDouble(materialName);
    }
}
