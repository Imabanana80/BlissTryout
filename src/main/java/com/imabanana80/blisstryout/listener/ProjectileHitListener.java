package com.imabanana80.blisstryout.listener;

import com.imabanana80.blisstryout.BlissTryout;
import com.imabanana80.blisstryout.item.SpidermanGem;
import com.imabanana80.potassium.item.ItemRegistry;
import com.imabanana80.potassium.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ProjectileHitListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Snowball snowball) {
            if (Objects.equals(snowball.getCustomName(), "gooball")) {
                snowball.getWorld().playSound(snowball.getLocation(), Sound.BLOCK_COBWEB_HIT, 0.5f, 1.0f);
                if (event.getHitBlock() != null) {
                    //hit block, bounce
                    Vector velocity = snowball.getVelocity();
                    double speed = Math.sqrt(
                            Math.pow(velocity.getX(), 2) +
                                    Math.pow(velocity.getY(), 2) +
                                    Math.pow(velocity.getZ(), 2)
                    );
                    if (speed < 0.5) {
                        setCobweb(snowball.getLocation(), 0, 20*6);
                        return;
                    }
                    switch (event.getHitBlockFace()) {
                        case NORTH, SOUTH -> velocity.setZ(-velocity.getZ());
                        case EAST, WEST -> velocity.setX(-velocity.getX());
                        case UP, DOWN -> velocity.setY(-velocity.getY());
                    }
                    Snowball newball = snowball.getWorld().spawn(snowball.getLocation(), Snowball.class);
                    newball.setCustomName("gooball");
                    newball.setVelocity(velocity.multiply(0.9));
                    return;
                }
                if (event.getHitEntity() != null) {
                    //hit entity, go kaput
                    Entity hitEntity = event.getHitEntity();
                    if (hitEntity instanceof Player player) {
                        SpidermanGem gem = (SpidermanGem) ItemRegistry.get(SpidermanGem.class);
                        if (gem.is(player.getInventory().getItemInMainHand()) || gem.is(player.getInventory().getItemInOffHand())) return;
                    }
                    setCobweb(hitEntity.getLocation(), 20*5, 20*10);
                }
            }
        }
    }

    private static void setCobweb(Location location, int minTime, int maxTime) {
        if (!location.getBlock().isReplaceable()) return;
        location.getBlock().setType(Material.COBWEB);
        Bukkit.getScheduler().runTaskLater(BlissTryout.getInstance(), () -> {
            if (location.getBlock().getType().equals(Material.AIR)) return;
            location.getBlock().setType(Material.AIR);
        }, Random.between(minTime, maxTime));
    }
}
