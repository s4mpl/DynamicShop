package me.tareqalyousef.dynamicshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class Util {
    public static File getBalancesFile() {
        File file = new File(Settings.BALANCES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load balances config");

        return file;
    }

    public static File getDefaultPricesFile() {
        File file = new File(Settings.DEFAULT_PRICES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load default prices config");

        return file;
    }

    public static File getAliasesFile() {
        File file = new File(Settings.ALIASES_PATH);

        if (!file.exists())
            throw new RuntimeException("Failed to load aliases config");

        return file;
    }

    public static File getCurrentPricesFile() {
        File file = new File(Settings.CURRENT_PRICES_PATH);

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

    public static void createDefaultPricesFile() {
        File defaults = new File(Settings.DEFAULT_PRICES_PATH);

        try{
            defaults.createNewFile();
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not create default prices file");
        }

        YamlConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(Util.getDefaultPricesFile());

        try {
            InputStream input = Util.class.getClassLoader().getResourceAsStream("defaults.txt");
            InputStreamReader streamReader = new InputStreamReader(input, "UTF-8");
            BufferedReader in = new BufferedReader(streamReader);

            for (String line; (line = in.readLine()) != null;) {
                String[] content = line.split(":");
                defaultsConfig.set(content[0], Double.parseDouble(content[1]));
            }

        } catch (Exception e) {
            Bukkit.getLogger().info("Could not write to defaults.yml");
        }

        try {
            defaultsConfig.save(defaults);
        } catch (Exception exception) {
            Bukkit.getLogger().info("Could not save defaults.yml");
        }
    }

    public static void createCurrentPricesFile() {
        File prices = new File(Settings.CURRENT_PRICES_PATH);

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

    public static double getDefaultItemPrice(String materialName) {
        File prices = Util.getDefaultPricesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            return -1;

        return config.getDouble(materialName);
    }

    public static double getItemPrice(String materialName) {
        File prices = Util.getCurrentPricesFile();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(prices);

        if (!config.contains(materialName))
            return -1;

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

    public static double getItemPriceChange(String materialName) {
        File prices = Util.getCurrentPricesFile();
        File defaults = Util.getDefaultPricesFile();
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(prices);
        YamlConfiguration config2 = YamlConfiguration.loadConfiguration(defaults);

        if (!config1.contains(materialName) || !config2.contains(materialName))
            return -1;

        return config1.getDouble(materialName) / config2.getDouble(materialName);
    }

    public static HashMap<String, Double> getItemPriceChanges() {
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(Util.getDefaultPricesFile());
        YamlConfiguration pricesConfig = YamlConfiguration.loadConfiguration(Util.getCurrentPricesFile());

        HashMap<String, Double> changes = new HashMap<String, Double>();

        try {
            for (String mat : defaultConfig.getKeys(false)) {
                double ratio = pricesConfig.getDouble(mat) / defaultConfig.getDouble(mat);
                changes.put(mat, ratio);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not read price changes");
        }

        return changes;
    }

    public static HashMap<String, Double[]> getItemPricesAndChanges() {
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(Util.getDefaultPricesFile());
        YamlConfiguration pricesConfig = YamlConfiguration.loadConfiguration(Util.getCurrentPricesFile());

        HashMap<String, Double[]> prices = new HashMap<String, Double[]>();

        try {
            for (String mat : defaultConfig.getKeys(false)) {
                double price = pricesConfig.getDouble(mat);
                double ratio = price / defaultConfig.getDouble(mat);
                Double[] data = {price, ratio};
                prices.put(mat, data);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Could not read prices and price changes");
        }

        return prices;
    }

    public static double quoteItemPrice(String materialName, TransactionType mode, int amount) {
        double quote = getItemPrice(materialName);
        double total = 0;

        if (mode == TransactionType.BUY) {
            for(int i = 0; i < amount; i++) {
                total += quote;
                quote *= 1 + Settings.BASE_RATE * Math.pow(getDefaultItemPrice(materialName), Settings.GROWTH_RATE);
            }

            return total;
        } else if (mode == TransactionType.SELL) {
            for(int i = 0; i < amount; i++) {
                total += quote;
                quote /= 1 + Settings.BASE_RATE * Math.pow(getDefaultItemPrice(materialName), Settings.GROWTH_RATE);
            }

            return total * Settings.SALES_TAX;
        } else {
            return -1;
        }
    }

    public static double quoteItemPriceChange(String materialName, TransactionType mode, int amount) {
        double quote = getItemPrice(materialName);

        if (mode == TransactionType.BUY) {
            quote *= Math.pow(1 + Settings.BASE_RATE * Math.pow(getDefaultItemPrice(materialName), Settings.GROWTH_RATE), amount);
            return quote;
        } else if (mode == TransactionType.SELL) {
            quote /= Math.pow(1 + Settings.BASE_RATE * Math.pow(getDefaultItemPrice(materialName), Settings.GROWTH_RATE), amount);
            return quote;
        } else {
            return -1;
        }
    }

    public static int getInventoryQuantity(Player player, Material material) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getSize(); i++)
            if (player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == material)
                count += player.getInventory().getItem(i).getAmount();
        return count;
    }

    // From https://github.com/MarvinKlar/Shop/blob/master/src/mr/minecraft15/shop/commands/ShopCommand.java (genius)
    public static void removeItem(Player p, Material m) {
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (p.getInventory().getItem(i) != null  && p.getInventory().getItem(i).getType() == m) {
                if (p.getInventory().getItem(i).getAmount() == 1) {
                    p.getInventory().setItem(i, new ItemStack(Material.AIR));
                    p.updateInventory();
                } else {
                    p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 1);
                    p.updateInventory();
                }
                // Avoid bug where it subtracts from all stacks of that material type
                return;
            }
        }
    }
}
