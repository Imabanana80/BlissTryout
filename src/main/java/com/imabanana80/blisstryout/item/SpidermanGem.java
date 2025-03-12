package com.imabanana80.blisstryout.item;

import com.imabanana80.blisstryout.BlissTryout;
import com.imabanana80.blisstryout.manager.GooBallManager;
import com.imabanana80.potassium.item.CustomItem;
import com.imabanana80.potassium.item.ItemStackBuilder;
import com.imabanana80.potassium.particle.DustParticle;
import com.imabanana80.potassium.particle.shape.ParticleCircle;
import com.imabanana80.potassium.particle.shape.ParticleLine;
import com.imabanana80.potassium.util.Random;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.concurrent.atomic.AtomicInteger;

public class SpidermanGem extends CustomItem {
    public SpidermanGem(JavaPlugin plugin) {
        super(plugin, "SPIDERMAN_GEM");
    }

    @Override
    protected ItemStack createItem() {
        ItemStack item = new ItemStackBuilder(Material.AMETHYST_SHARD).setDisplayName(Component.text("Spiderman Gem")).setCustomModelData(80).build();
        ItemMeta meta = item.getItemMeta();
        meta.setItemModel(new NamespacedKey("bliss", "spiderman_gem"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    protected void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        int delay = 0;
        for (int s = 10; s > 0; s = s-1) {
            delay = (int) (delay + Math.sqrt(s));
            Bukkit.getScheduler().runTaskLater(BlissTryout.getInstance(), () -> {
                for (int i = 0; i < 3; i++) {
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_COBWEB_HIT, 0.5f, 1.0f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FOX_SPIT, 0.75f, 2.0f);
                    Snowball snowball = GooBallManager.spawnGooball(player);
                    Vector velocity = player.getEyeLocation().getDirection();
                    velocity.setX(velocity.getX() + Math.random()/10);
                    velocity.setY(velocity.getY() + Math.random()/10);
                    velocity.setZ(velocity.getZ() + Math.random()/10);
                    snowball.setVelocity(velocity);
                }
            }, delay);
        }
    }

    @Override
    protected void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {
            RayTraceResult rayTraceResult = player.getWorld().rayTrace(player.getEyeLocation(), player.getEyeLocation().getDirection(), 256, FluidCollisionMode.NEVER,  true, 1, entity -> {
                if (entity instanceof Player target) {
                    return !target.equals(player);
                }
                return true;
            } );
            if (rayTraceResult == null) return;
            if (rayTraceResult.getHitBlock() != null) {
                Block block = rayTraceResult.getHitBlock();
                ParticleLine line = new ParticleLine(player.getEyeLocation().toVector(), block.getLocation().add(0.5, 0.5, 0.5).toVector(),2);
                line.render(player.getWorld(), new DustParticle(Color.WHITE, 1, 1), true);
                BlockData data = block.getBlockData();
                block.setType(Material.AIR);
                Vector dir = player.getEyeLocation().toVector().subtract(block.getLocation().add(new Vector(0.5, 0, 0.5)).toVector()).normalize();
                double strength = player.getEyeLocation().distance(block.getLocation())/8;
                FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0, 0.5), data);
                fallingBlock.setVelocity(dir.multiply(strength));
                fallingBlock.setGlowing(true);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 0.5f, 2.0f);
                player.getWorld().playSound(block.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
                return;
            }
            if (rayTraceResult.getHitEntity() != null) {
                Entity entity = rayTraceResult.getHitEntity();
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(0, player);
                }
                ParticleLine line = new ParticleLine(entity.getLocation().add(0, 0.5, 0).toVector(), player.getEyeLocation().toVector(),2);
                line.render(player.getWorld(), new DustParticle(Color.WHITE, 1, 1), true);
                Vector dir = player.getEyeLocation().toVector().subtract(entity.getLocation().add(0, 0.5, 0).toVector()).normalize();
                double strength = entity.getLocation().distance(player.getLocation())/4;
                entity.setVelocity(dir.multiply(strength));
                GooBallManager.cobweb(entity);
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 0.5f, 2.0f);
                entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
            }
            return;
        }
        RayTraceResult rayTraceResult = player.rayTraceBlocks(256, FluidCollisionMode.NEVER);
        if (rayTraceResult == null) return;
        Block block = rayTraceResult.getHitBlock();
        ParticleLine line = new ParticleLine(player.getEyeLocation().toVector(), block.getLocation().add(0.5, 0.5, 0.5).toVector(),2);
        line.render(player.getWorld(), new DustParticle(Color.WHITE, 1, 1), true);
        Vector dir = block.getLocation().add(0.5 ,0.5, 0.5).toVector().subtract(player.getEyeLocation().toVector()).normalize();
        double strength = player.getEyeLocation().distance(block.getLocation())/2;
        if (!player.getLocation().subtract(new Vector(0, 0.5, 0)).getBlock().getType().equals(Material.AIR)) {
            dir.add(new Vector(0, 0.25, 0));
        }
        player.setVelocity(dir.multiply(strength));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LEASH_KNOT_PLACE, 0.5f, 2.0f);
        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_COBWEB_STEP, 1.0f, 1.0f);
        AtomicInteger previousFallDistance = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(BlissTryout.getInstance(), task -> {
            if (player.getVelocity().getY() > 0) {
                task.cancel();
                return;
            }
            if (!player.getLocation().subtract(0, 0.5, 0).getBlock().isReplaceable()) {
                if (previousFallDistance.get() < 4.0) return;
                for (int i = 0; i < 4;i++) {
                    final int finalI = i;
                    Location playerLoc = player.getLocation().add(0, 0.1, 0);
                    Bukkit.getScheduler().runTaskLater(BlissTryout.getInstance(), () -> {
                        ParticleCircle circle = new ParticleCircle(playerLoc.toVector(),6,  finalI + 1);
                        for (Vector vec : circle.points) {
                            Location location = vec.toLocation(player.getWorld()).add(0, 1, 0);
                            location.getWorld().playSound(location, Sound.BLOCK_COBWEB_BREAK, 0.25f, 1.0f);
                            Snowball snowball = GooBallManager.spawnGooball(location);

                            Vector velocity = snowball.getLocation().toVector().subtract(playerLoc.toVector()).normalize();
                            velocity.setX(velocity.getX() + Math.random()/10);
                            velocity.setY(velocity.getY() + Math.random()/10);
                            velocity.setZ(velocity.getZ() + Math.random()/10);
                            snowball.setVelocity(velocity);
                        }
                        player.getWorld().spawn(Random.from(circle.points).toLocation(player.getWorld()), Spider.class, entity -> {
                            entity.getAttribute(Attribute.SCALE).setBaseValue(0.5);
                            Bukkit.getScheduler().runTaskLater(BlissTryout.getInstance(), () -> {
                                GooBallManager.spawnGooball(entity);
                                entity.damage(255);
                            }, Random.between(20*15, 20*20));
                        });
                        circle.render(player.getWorld(), new DustParticle(Color.WHITE, 2, 1));
                    }, finalI);
                }
                task.cancel();
                return;
            }
            previousFallDistance.set((int) player.getFallDistance());
        }, 0, 1);
    }
}
