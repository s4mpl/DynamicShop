package me.tareqalyousef.dynamicshop.listeners;

import me.tareqalyousef.dynamicshop.DynamicShop;
import me.tareqalyousef.dynamicshop.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class JoinListener implements Listener {
    private DynamicShop plugin;

    public JoinListener(DynamicShop plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        File balances = Utilities.GetBalancesConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(balances);

        if (!config.contains(player.getUniqueId().toString())) {
            config.set(player.getUniqueId().toString(), 0.0);
            Bukkit.getLogger().info("Added " + player.getDisplayName() + " to balances.yml");

            try {
                config.save(balances);
            } catch (Exception exception) {
                Bukkit.getLogger().info("Could not save balances.yml");
            }
        }
    }
}
