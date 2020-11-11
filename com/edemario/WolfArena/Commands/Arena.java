package com.edemario.WolfArena.Commands;

import com.edemario.WolfArena.Main;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Arena implements CommandExecutor, TabCompleter {

    private static final String[] COMMAND_0 = { "pos1", "pos2", "start" };

    public static Location pos1;
    public static Location pos2;
    public static int round = 0;
    private LivingEntity opponent;

    public static Wolf wolf;
    public static Skeleton skelly;
    public static Zombie zombie;

    FileConfiguration config;


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        config = Main.get().getConfig();
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by players.");
            return false;
        }

        Player p = (Player) sender;

        if(args.length == 0) {
            p.sendMessage("§c[Wolf Arena] - Usage: " + label + " <command>");
            return false;
        }

        switch (args[0]) {
            case "pos1":
                pos1 = p.getLocation();
                config.set("arena.pos1", pos1);
                if(pos2 != null) config.set("arena.set", true);
                Main.get().saveConfig();
                p.sendMessage("§a[WolfArena] - Position one set up.");
                return true;
            case "pos2":
                pos2 = p.getLocation();
                config.set("arena.pos2", pos2);
                if(pos1 != null) config.set("arena.set", true);
                Main.get().saveConfig();
                p.sendMessage("§a[WolfArena] - Position two set up.");
                return true;
            case "start":
                arenaStart(p);
                return true;
            case "kill":
                arenaKill();
                return true;
            case "restart":
                config.set("arena.round", 0);
                Main.get().saveConfig();
                p.sendMessage("§a[WolfArena] - Arena reset to round 0");
                return true;
            default:
                p.sendMessage("§c[WolfArena] - Invalid command usage.");
                return false;
        }
    }

    public void arenaStart(Player p) {
        if(config.getBoolean("arena.set")) {
            pos1 = (Location) config.get("arena.pos1");
            pos2 = (Location) config.get("arena.pos2");
            round = config.getInt("arena.round");
        }

        if(pos1 == null || pos2 == null) {
            p.sendMessage("§c[WolfArena] - This is an invalid setup. Please set both pos1 and pos2 before trying" +
                    "to start the arena");
            return;
        }

        switch (round) {
            case 0:
                wolf = (Wolf) p.getWorld().spawnEntity(pos1, EntityType.WOLF);
                wolf.setCollarColor(DyeColor.BLACK);
                wolf.setCustomName("§c§k|| §cWolf §c§k||");
                wolf.setCustomNameVisible(true);
                wolf.setMetadata("the_wolf", new FixedMetadataValue(Main.get(), "wa"));
                double wolf_hp = Math.floor(Math.floor(Math.random() * 100) + 20);
                wolf.setMaxHealth(wolf_hp); wolf.setHealth(wolf_hp);

                skelly = (Skeleton) p.getWorld().spawnEntity(pos1, EntityType.SKELETON);
                skelly.setCustomName("§c§k|| §cSkeleton §c§k||");
                skelly.setCustomNameVisible(true);
                skelly.setMetadata("arena", new FixedMetadataValue(Main.get(), "wa"));
                double skelly_health = Math.floor(Math.floor(Math.random() * 100) + 20);
                skelly.setMaxHealth(skelly_health); skelly.setHealth(skelly_health);

                skelly.setTarget(wolf);
                wolf.setTarget(skelly);

                opponent = skelly;
                break;
            case 1:
                wolf = (Wolf) p.getWorld().spawnEntity(pos1, EntityType.WOLF);
                wolf.setCollarColor(DyeColor.BLACK);
                wolf.setCustomName("§c§k|| §cWolf §c§k||");
                wolf.setCustomNameVisible(true);
                wolf.setMetadata("the_wolf", new FixedMetadataValue(Main.get(), "wa"));
                double wolf_hp1 = Math.floor(Math.floor(Math.random() * 100) + 20);
                wolf.setMaxHealth(wolf_hp1); wolf.setHealth(wolf_hp1);

                zombie = (Zombie) p.getWorld().spawnEntity(pos2, EntityType.ZOMBIE);
                zombie.setCustomName("§c§k|| §cZombie §c§k||");
                zombie.setCustomNameVisible(true);
                zombie.setMetadata("arena", new FixedMetadataValue(Main.get(), "wa"));
                double zombie_hp = Math.floor(Math.floor(Math.random() * 130) + 35);
                zombie.setMaxHealth(zombie_hp); zombie.setHealth(zombie_hp);

                wolf.setTarget(zombie);
                zombie.setTarget(wolf);

                opponent = zombie;
                break;
        }
    }

    private void arenaKill() {
        if(!wolf.isDead()) {
            opponent.setHealth(0);
            wolf.setHealth(0);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(strings[0], Arrays.asList(COMMAND_0), completions);

        Collections.sort(completions);
        return completions;
    }
}
