package com.edemario.WolfArena;

import com.edemario.WolfArena.Commands.Arena;
import com.edemario.WolfArena.Events.EntityHandling;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/*
* Actually I don't know what I'm doing anymore
* I think i'll just go back to making Minecraft plugins cuz I have no idea what to do lol
* I'm so bored, sheesh
*/

public class Main extends JavaPlugin {

    private static Main plugin;
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage("§cWolf Arena is being loaded\n\n" +
                "§aWolf Arena loaded successfully.\n" +
                "Version: 1.0.0\n" +
                "Author: Zeki#0001\n");

        this.getCommand("arena").setExecutor(new Arena());
        this.getCommand("arena").setExecutor(new Arena());

        Bukkit.getPluginManager().registerEvents(new EntityHandling(), this);

        World world = Bukkit.getWorlds().get(0);
        config.addDefault("arena.pos1", new Location(world, 0, 0,0));
        config.addDefault("arena.pos2", new Location(world, 0, 0,0));
        config.addDefault("arena.set", false);
        config.addDefault("arena.round", 0);
        config.options().copyDefaults(true);
        saveConfig();


        plugin = this;
    }

    public static Main get() {
        return plugin;
    }

}
