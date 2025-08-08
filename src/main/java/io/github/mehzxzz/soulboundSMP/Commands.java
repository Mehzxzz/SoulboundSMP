package io.github.mehzxzz.soulboundSMP;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Commands implements CommandExecutor, TabCompleter {
    private final LifeManager lifeManager;

    public Commands(SoulboundPlugin plugin, LifeManager lifeManager) {
        this.lifeManager = lifeManager;
        plugin.getCommand("setlives").setExecutor(this);
        plugin.getCommand("donatelife").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setlives")) {
            if (!sender.hasPermission("soulbound.lives.set")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            if (args.length != 2) return false;

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            int lives;
            try {
                lives = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "Invalid number.");
                return true;
            }

            if (lives < 0 || lives > 5) {
                sender.sendMessage(ChatColor.RED + "Must be between 0–5.");
                return true;
            }

            lifeManager.setLives(target.getUniqueId(), lives);
            if (target.isOnline()) lifeManager.assignTeam((Player) target);

            sender.sendMessage(ChatColor.GREEN + target.getName() + " now has " + lives + " lives.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("donatelife")) {
            if (!(sender instanceof Player)) return true;
            Player donor = (Player) sender;

            if (args.length < 1) return false;
            OfflinePlayer recipient = Bukkit.getOfflinePlayer(args[0]);
            if (recipient.getUniqueId().equals(donor.getUniqueId())) {
                donor.sendMessage(ChatColor.RED + "Cannot donate to yourself.");
                return true;
            }

            int donorLives = lifeManager.getLives(donor.getUniqueId());
            int targetLives = lifeManager.getLives(recipient.getUniqueId());

            if (donorLives < 2) {
                donor.sendMessage(ChatColor.RED + "Need 2+ lives to donate.");
                return true;
            }
            if (targetLives >= 5) {
                donor.sendMessage(ChatColor.RED + "Recipient has max lives.");
                return true;
            }

            if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
                if (donorLives == 2) {
                    donor.sendMessage(ChatColor.RED + "This will cost all lives ARE YOU SURE?! Type /donatelife " + recipient.getName() + " confirm");
                } else {
                    donor.sendMessage(ChatColor.RED + "This will cost 2 lives. Type /donatelife " + recipient.getName() + " confirm");
                }
                return true;
            }

            lifeManager.removeLives(donor.getUniqueId(), 2);
            lifeManager.addLives(recipient.getUniqueId(), 1);
            lifeManager.assignTeam(donor);
            if (recipient.isOnline()) lifeManager.assignTeam((Player) recipient);

            donor.sendMessage(ChatColor.GREEN + "Donated 1 life to " + recipient.getName());
            if (recipient.isOnline()) ((Player) recipient).sendMessage(ChatColor.GREEN + donor.getName() + " gave you a life!");
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> names = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> names.add(p.getName()));
            return names;
        }
        return Collections.emptyList();
    }
}