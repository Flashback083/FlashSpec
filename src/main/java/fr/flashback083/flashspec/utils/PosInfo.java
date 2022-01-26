package fr.flashback083.flashspec.utils;

import net.minecraft.world.World;

public class PosInfo {

    private final World world;
    private final int posX, posY, posZ;

    public PosInfo(World world, int posX, int posY, int posZ){
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public World getWorld() {
        return world;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }
}
