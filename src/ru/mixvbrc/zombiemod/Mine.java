package ru.mixvbrc.zombiemod;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Mine extends JavaPlugin {

    private final MonsterData monsterData = new MonsterData();

    @Override
    public void onEnable()
    {
        Bukkit.getPluginManager().registerEvents(new Handler(), this);

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                TickEvent tickEvent = new TickEvent();
                Bukkit.getServer().getPluginManager().callEvent(tickEvent);
            }
        }, 0, 5);

        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                Calculations calculations = new Calculations();
                long time = Bukkit.getWorlds().get(0).getTime();
                EventChangeNightAndDay eventChangeNightAndDay = new EventChangeNightAndDay(Bukkit.getWorlds().get(0).getTime());

                if (time == calculations.getStartDayTime() || time == calculations.getStartNightTime())
                    Bukkit.getServer().getPluginManager().callEvent(eventChangeNightAndDay);

            }
        }, 0, 1);

        for (Player player : Bukkit.getOnlinePlayers())
            for (Entity entity : player.getNearbyEntities(255,255,255))
                if (entity instanceof Monster)
                    entity.remove();
    }

    @Override
    public void onDisable()
    {
        this.monsterData.stopAllTimers();
    }
}