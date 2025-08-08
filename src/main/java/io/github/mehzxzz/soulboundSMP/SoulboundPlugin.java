package io.github.mehzxzz.soulboundSMP;

import org.bukkit.plugin.java.JavaPlugin;

public class SoulboundPlugin extends JavaPlugin {

    private LifeManager lifeManager;

    @Override
    public void onEnable() {
        lifeManager = new LifeManager(this);
        TeamSetupListener.setupTeams();
        getServer().getPluginManager().registerEvents(new Listeners(this, lifeManager), this);
        new Commands(this, lifeManager);
    }

    @Override
    public void onDisable() {
        lifeManager.saveConfig();
    }

    public LifeManager getLifeManager() {
        return lifeManager;
    }
}