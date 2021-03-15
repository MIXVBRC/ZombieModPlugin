package ru.mixvbrc.zombiemod;

import org.bukkit.Bukkit;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.TimerTask;

public class MonsterAngry extends TimerTask {

    public Monster monster;
    private final Calculations calculations = new Calculations();
    private final Collection<? extends Player> playerList = Bukkit.getOnlinePlayers();

    public MonsterAngry(Monster monster) { this.monster = monster; }

    @Override
    public void run() {
        if (!this.monster.isDead())
        {
            Player playerTarget = this.playerList.iterator().next();

            double distance = this.calculations.getDistanceXYZ(this.monster.getLocation(), this.playerList.iterator().next().getLocation());

            for (Player player : this.playerList)
                if (distance >= this.calculations.getDistanceXYZ(this.monster.getLocation(), player.getLocation()))
                    playerTarget = player;

            this.monster.setTarget(playerTarget);

        } else {
            this.monster.remove();
            this.cancel();
        }
    }
}
