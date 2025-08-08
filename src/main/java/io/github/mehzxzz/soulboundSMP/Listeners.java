package io.github.mehzxzz.soulboundSMP;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Listeners implements Listener {
    private final LifeManager lifeManager;

    public Listeners(SoulboundPlugin plugin, LifeManager lifeManager) {
        this.lifeManager = lifeManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        lifeManager.assignTeam(p);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        lifeManager.removeLives(p.getUniqueId(), 1);
        lifeManager.assignTeam(p);
    }
}