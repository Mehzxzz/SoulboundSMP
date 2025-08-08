package io.github.mehzxzz.soulboundSMP;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamSetupListener {
    public static void setupTeams() {
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
        createTeam(sb, "5Lives");
        createTeam(sb, "4Lives");
        createTeam(sb, "3Lives");
        createTeam(sb, "2Lives");
        createTeam(sb, "1Life");
    }

    public static void createTeam(Scoreboard sb, String name) {
        Team team = sb.getTeam(name);
        if (team == null) {
            team = sb.registerNewTeam(name);
            switch (name) {
                case "5Lives" -> team.setColor(ChatColor.DARK_GREEN);
                case "4Lives" -> team.setColor(ChatColor.GREEN);
                case "3Lives" -> team.setColor(ChatColor.YELLOW);
                case "2Lives" -> team.setColor(ChatColor.RED);
                case "1Life" -> team.setColor(ChatColor.DARK_RED);
            }
        }
    }
}