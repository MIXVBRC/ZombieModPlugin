package ru.mixvbrc.zombiemod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class Calculations {

    public int getRandom(int min, int max) { return (int) (Math.random() * (max - min) + min); }

    public double getDistanceXYZ(Location aLocation, Location bLocation) { return Math.sqrt(Math.pow(aLocation.getX() - bLocation.getX(), 2) + Math.pow(aLocation.getY() - bLocation.getY(), 2) + Math.pow(aLocation.getZ() - bLocation.getZ(), 2)); }

    public Vector getVector(Location aLocation, Location bLocation, int decrease)
    {
        if (decrease <= 0) decrease = 1;

        double X = ((bLocation.getX() - aLocation.getX()) / decrease);
        double Y = ((bLocation.getY() - aLocation.getY()) / decrease);
        double Z = ((bLocation.getZ() - aLocation.getZ()) / decrease);

        return new Vector(X, Y, Z);
    }

    public long getStartDayTime() { return 23850; }

    public long getStartNightTime() { return 12600; }

    public Block[] getArrayBlocks(Location location, int num) {

        int count = 1 + num * 2;

        Block[] blocks = new Block[count * count * count];

        int blockNum = 0;

        int x = (int) (Math.floor(location.getX()) - num);
        int y = (int) (Math.floor(location.getY()) - num);
        int z = (int) (Math.floor(location.getZ()) - num);

        int endX = x + num * 2;
        int endY = y + num * 2;
        int endZ = z + num * 2;

        for (int nowX = x; nowX <= endX; nowX++) {
            for (int nowY = y; nowY <= endY; nowY++) {
                for (int nowZ = z; nowZ <= endZ; nowZ++) {

                    location.setX(nowX);
                    location.setY(nowY);
                    location.setZ(nowZ);

                    blocks[blockNum] = location.getBlock();
                    blockNum++;

                }
            }
        }

        return blocks;
    }
}
