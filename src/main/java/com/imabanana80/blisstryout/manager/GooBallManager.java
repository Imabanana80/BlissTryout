package com.imabanana80.blisstryout.manager;

import com.imabanana80.blisstryout.BlissTryout;
import com.imabanana80.potassium.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.joml.Matrix4f;

public class GooBallManager {
    public static Snowball spawnGooball(Location location) {
        Snowball snowball = location.getWorld().spawn(location, Snowball.class);
        snowball.setCustomName("gooball");
        snowball.setItem(new ItemStack(Material.COBWEB));
        return snowball;
    }
    public static Snowball spawnGooball(LivingEntity entity) {
        Snowball snowball = entity.launchProjectile(Snowball.class);
        snowball.setCustomName("gooball");
        snowball.setItem(new ItemStack(Material.COBWEB));
        return snowball;
    }
    public static void cobweb(Entity entity) {
        if (!entity.getPassengers().stream().filter(passanger -> {
            return passanger instanceof Display;
        }).toList().isEmpty()) return;
        Location loc = entity.getLocation();
        BlockDisplay display = entity.getWorld().spawn(new Location(entity.getWorld(), loc.getX(), loc.getY(), loc.getZ()), BlockDisplay.class);
        display.setBlock(Bukkit.createBlockData(Material.COBWEB));
        display.setTransformationMatrix(
                new Matrix4f()
                        .translate(-0.5f, (float) -(entity.getHeight()/1.5), -0.5f)
        );
        int duration = Random.between(20*3, 20*6);
        Bukkit.getScheduler().runTaskTimer(BlissTryout.getInstance(), task -> {
            if (entity.isDead()) {
                display.remove();
                task.cancel();
                return;
            }
        }, 0, 1);
        Bukkit.getScheduler().runTaskLater(BlissTryout.getInstance(), () -> {
            if (!display.isDead()) {
                display.remove();
            }
        }, duration);
        entity.addPassenger(display);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addPotionEffect(PotionEffectType.SLOWNESS.createEffect(duration, 4));
        }
    }
}

