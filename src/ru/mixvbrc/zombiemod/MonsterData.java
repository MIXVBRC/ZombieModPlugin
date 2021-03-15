package ru.mixvbrc.zombiemod;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class MonsterData {

    private final Calculations calculations = new Calculations();
    private final World world = Bukkit.getWorlds().get(0);
    private final Map<UUID, Monster> monsterMap = new HashMap<>();
    private final List<Timer> timerList = new ArrayList<>();

    public void add(Monster monster)
    {
        if (this.checkPlayers() && this.checkTime() && this.checkDistance(monster)) {

            if (monster instanceof Zombie) {
                Zombie zombie = (Zombie) monster;
                if (!zombie.isAdult())
                    zombie.setAdult();
            }

            AttributeInstance movementSpeed = monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

            if (movementSpeed != null)
                movementSpeed.setBaseValue(0.33);

            this.monsterMap.put(monster.getUniqueId(), monster);

//            this.teleport(monster);

            monster.teleport(new Location(this.world, 1190,74,264));
            Timer timer = new Timer();
            timer.schedule(new MonsterAngry(monster), 0,1000);
            this.timerList.add(timer);

        } else {
            monster.remove();
        }
    }

    public void crush()
    {
        this.world.setStorm(false);
        if (this.checkTime())
            for (Map.Entry<UUID,Monster> entry : this.monsterMap.entrySet())
                if (this.calculations.getRandom(0,10) < 9 && !this.placeBlock(entry.getValue()))
                    this.blockManipulation(entry.getValue());
    }

    public void blockManipulation(Monster monster)
    {
        for (Block block : monster.getLastTwoTargetBlocks(null, 5))
        {
            if (!this.checkBlock(block))
            {
                Location location = block.getLocation();
                if (!this.checkBlock(block.getWorld().getBlockAt(block.getLocation().add(0,-1,0)))) {
                    location = monster.getLocation();
                }

                this.world.spawnFallingBlock(location.add(0.5, 0, 0.5), block.getBlockData());
                block.setType(Material.AIR);
            }
        }
    }

    public boolean placeBlock(Monster monster)
    {
        EntityEquipment entityEquipment = monster.getEquipment();
        if (entityEquipment != null && !this.world.getBlockAt(monster.getLocation().add(0,-1,0)).getType().equals(Material.AIR) && this.world.getBlockAt(monster.getLocation()).getType().equals(Material.AIR))
        {
            ItemStack equipmentItemInMainHand = entityEquipment.getItemInMainHand();
            if (!equipmentItemInMainHand.getType().equals(Material.AIR) && equipmentItemInMainHand.getType().isBlock())
            {
                if (equipmentItemInMainHand.getAmount() - 1 > 0)
                {
                    ItemStack newEquipmentItemInMainHand = new ItemStack(equipmentItemInMainHand.getType());
                    newEquipmentItemInMainHand.setAmount(equipmentItemInMainHand.getAmount() - 1);
                    entityEquipment.setItemInMainHand(newEquipmentItemInMainHand);

                } else {
                    entityEquipment.setItemInMainHand(new ItemStack(Material.AIR));
                }

                this.customJump(monster);
                this.world.getBlockAt(monster.getLocation()).setType(equipmentItemInMainHand.getType());

                return true;
            } else {
                entityEquipment.setItemInMainHand(new ItemStack(Material.AIR));
            }
            return false;
        }
        return false;
    }

    public void customJump(Monster monster)
    {
        monster.setVelocity(this.calculations.getVector(monster.getLocation(), monster.getLocation().add(0,1,0), 2));
    }

    public boolean checkBlock(Block block)
    {
        return block.getType().equals(Material.AIR)
                || block.getType().equals(Material.WATER)
                || block.getType().equals(Material.FIRE)
                || block.getType().equals(Material.LAVA);
    }

    private void teleport(Monster monster)
    {
        for (int Y = 255; Y > 0; Y--)
        {
            if (!this.world.getBlockAt(new Location(this.world, monster.getLocation().getX(), Y, monster.getLocation().getZ())).getType().equals(Material.AIR))
            {
                monster.teleport(new Location(this.world, monster.getLocation().getX(), Y+1, monster.getLocation().getZ()));
                break;
            }
        }
    }

    public void remove(Monster monster)
    {
        if (this.monsterMap.get(monster.getUniqueId()) != null)
        {
            Monster removeMonster = this.monsterMap.get(monster.getUniqueId());

            if (removeMonster !=null)
                removeMonster.remove();

            this.monsterMap.remove(monster.getUniqueId());
        }
    }

    private boolean checkDistance(Monster monster)
    {
        double distance = 64;

        for (Player player : Bukkit.getOnlinePlayers())
            if (this.calculations.getDistanceXYZ(new Location(this.world, player.getLocation().getX(), 0, player.getLocation().getZ()), new Location(this.world, monster.getLocation().getX(), 0, monster.getLocation().getZ())) < distance)
                return false;

        return true;
    }

    public boolean checkTime()
    {
        if (this.world.getTime() >= this.calculations.getStartDayTime() || this.world.getTime() < this.calculations.getStartNightTime()) {
            this.clearMap();
            return false;
        }

        return true;
    }

    public void clearMap()
    {
        for (Map.Entry<UUID,Monster> entry : this.monsterMap.entrySet())
            entry.getValue().remove();

        this.monsterMap.clear();
    }

    public void stopAllTimers()
    {
        this.clearMap();

        for (Timer timer : this.timerList)
            timer.cancel();

        this.timerList.clear();
    }

    public boolean checkPlayers()
    {
        if (Bukkit.getOnlinePlayers().size() <= 0) {
            this.clearMap();
            return false;
        }

        return true;
    }
}
