package io.github.mehzxzz.soulboundSMP;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LifeManager {
    private final SoulboundPlugin plugin;
    private File dataFile;
    private YamlConfiguration config;

    public LifeManager(SoulboundPlugin plugin) {
        this.plugin = plugin;
        createDataFile();
        loadConfig();
    }

    private void createDataFile() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.getDataFolder().mkdirs();
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void loadConfig() {
        config = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveConfig() {
        try { config.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public int getLives(UUID uuid) {
        return config.getInt("lives." + uuid, 5);
    }

    public void setLives(UUID uuid, int lives) {
        config.set("lives." + uuid, lives);
        saveConfig();
    }

    public void addLives(UUID uuid, int amount) {
        setLives(uuid, getLives(uuid) + amount);
    }

    public void removeLives(UUID uuid, int amount) {
        int newLives = Math.max(0, getLives(uuid) - amount);
        setLives(uuid, newLives);
    }

    public void assignTeam(Player player) {
        int lives = getLives(player.getUniqueId());
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        removeFromAllTeams(player, sb);

        switch (lives) {
            case 5 -> joinTeam(player, sb, "5Lives");
            case 4 -> joinTeam(player, sb, "4Lives");
            case 3 -> joinTeam(player, sb, "3Lives");
            case 2 -> joinTeam(player, sb, "2Lives");
            case 1 -> joinTeam(player, sb, "1Life");
            default -> {
                player.setGameMode(GameMode.SPECTATOR);
                Bukkit.broadcastMessage("§4" + player.getName() + " has lost all of their lives!");
            }
        }
    }

    private void removeFromAllTeams(Player player, Scoreboard sb) {
        String[] teams = {"5Lives","4Lives","3Lives","2Lives","1Life"};
        for (String t : teams) {
            Team team = sb.getTeam(t);
            if (team != null && team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    private void joinTeam(Player player, Scoreboard sb, String teamName) {
        Team team = sb.getTeam(teamName);
        if (team == null) TeamSetupListener.createTeam(sb, teamName);
        team = sb.getTeam(teamName);
        if (team != null) {
            team.addEntry(player.getName());
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}