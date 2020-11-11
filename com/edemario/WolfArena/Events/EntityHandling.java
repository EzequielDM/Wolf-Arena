package com.edemario.WolfArena.Events;

import com.edemario.WolfArena.Commands.Arena;
import com.edemario.WolfArena.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

public class EntityHandling implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity ent = e.getEntity();
        LivingEntity killer = e.getEntity().getKiller();

        if(!ent.hasMetadata("arena") && !ent.hasMetadata("the_wolf")) return;

        if(ent.hasMetadata("arena")) {
            Arena.wolf.setSitting(true);
            Arena.wolf.teleport(Arena.pos1);
            Arena.wolf.setTarget(null);
            Arena.wolf.getWorld().playSound(Arena.wolf.getLocation(), Sound.WOLF_BARK, 1, 1.1f);

            Bukkit.broadcastMessage("§a[WolfArena] - The Wolf is the winner!\n" +
                    "§aHealth: §e" + Arena.wolf.getHealth() + "§a/" + Arena.wolf.getMaxHealth() + "\n" +
                    "Round: " + Arena.round);

            Arena.wolf.setHealth(0);

            giveReward(Arena.wolf.getLocation());

            Main.get().getConfig().set("arena.round", Main.get().getConfig().getInt("arena.round") + 1);
            Main.get().saveConfig();
        } else if(ent.hasMetadata("the_wolf")) {
            Bukkit.broadcastMessage("§a[WolfArena] - §cThe Wolf lost the battle, " + killer.getCustomName() + " wins.\n" +
                    "§aHealth: §e" + killer.getHealth() + "/§a" + killer.getMaxHealth() + "\n" +
                    "Round: " + Arena.round);

            if(killer.getType() != EntityType.PLAYER) killer.setHealth(0);

            Main.get().getConfig().set("arena.round", 0);
            Main.get().saveConfig();
        }
    }

    public void giveReward(Location l) {
        int round = Main.get().getConfig().getInt("arena.round");
        switch (round) {
            case 0:
                l.getWorld().dropItemNaturally(l, new ItemStack(Material.COAL, 64));
                break;
            case 1:
                l.getWorld().dropItemNaturally(l, new ItemStack(Material.IRON_INGOT, 64));
                break;
        }
    }

}
