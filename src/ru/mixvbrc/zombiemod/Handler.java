package ru.mixvbrc.zombiemod;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Handler implements Listener {

    private final MonsterData monsterData = new MonsterData();
    private final Calculations calculations = new Calculations();

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        if (event.getEntity() instanceof Monster)
            if (event.getEntity() instanceof Skeleton)
                this.monsterData.add((Monster) event.getEntity());

            else event.getEntity().remove();

        else if (event.getEntity() instanceof Phantom)
            event.getEntity().remove();
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Monster)
            this.monsterData.remove((Monster) event.getEntity());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        for (Entity entity : event.getPlayer().getNearbyEntities(255,255,255))
            if (entity instanceof Zombie)
            {
                Zombie zombie = (Zombie) entity;
                if (zombie.getTarget() == event.getPlayer() && Bukkit.getOnlinePlayers().size() > 0)
                    zombie.damage(zombie.getHealth() * zombie.getHealth());
            }
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {

        Calculations calculations = new Calculations();
        World world = Bukkit.getWorlds().get(0);

        if (event.getMessage().contains("setday"))
            world.setTime(calculations.getStartDayTime());

        else if(event.getMessage().contains("setnight"))
            world.setTime(calculations.getStartNightTime());
    }

    @EventHandler
    public void onTickEvent(TickEvent event) { this.monsterData.crush(); }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        event.getEntity().remove();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
//        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
//        {
//            Block block = event.getClickedBlock();
//
//            if(block != null)
//            {
//                event.getPlayer().getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0, 0.5), block.getBlockData());
//                block.setType(Material.AIR);
//            }
//        }
    }
}
