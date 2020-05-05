package com.jamesdpeters.minecraft.chests.runnables;

import com.jamesdpeters.minecraft.chests.ChestsPlusPlus;
import com.jamesdpeters.minecraft.chests.filters.HopperFilter;
import com.jamesdpeters.minecraft.chests.misc.Utils;
import com.jamesdpeters.minecraft.chests.serialize.InventoryStorage;
import com.jamesdpeters.minecraft.chests.serialize.SpigotConfig;
import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class VirtualChestToHopper extends BukkitRunnable {

    InventoryStorage storage;
    BukkitTask task;

    public VirtualChestToHopper(InventoryStorage storage){
        this.storage = storage;
    }

    public void start(){
        task = runTaskTimer(ChestsPlusPlus.PLUGIN,1,8);
    }

    public void stop(){
        task.cancel();
    }

    @Override
    public void run() {
        for(Location location : storage.getLocations()) {
            if(location != null) {
                Location below = location.clone().subtract(0, 1, 0);
                if (below.getBlock().getState() instanceof Hopper) {
                    Hopper hopper = (Hopper) below.getBlock().getState();
                    if(below.getBlock().isBlockIndirectlyPowered()|| below.getBlock().isBlockPowered()){
                        continue;
                    }
                    int hopperAmount = SpigotConfig.getWorldSettings(location.getWorld().getName()).getHopperAmount();
                    Utils.moveToOtherInventory(storage.getInventory(), hopperAmount , hopper.getInventory(), HopperFilter.getHopperFilters(below.getBlock()));
                    storage.sort();
                }
            }
        }
    }
}
