package com.imabanana80.blisstryout.listener;

import com.imabanana80.blisstryout.item.SpidermanGem;
import com.imabanana80.potassium.item.ItemRegistry;
import com.imabanana80.potassium.particle.DustParticle;
import com.imabanana80.potassium.particle.shape.ParticleCircle;
import org.bukkit.Color;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if (!event.getDamageSource().getDamageType().equals(DamageType.FALL)) return;
        if (event.getEntity() instanceof Player player) {
            SpidermanGem gem = (SpidermanGem) ItemRegistry.get(SpidermanGem.class);
            ParticleCircle circle = new ParticleCircle(player.getLocation().toVector(), 1);
            if (gem.is(player.getInventory().getItemInMainHand())) {
                circle.render(player.getWorld(), new DustParticle(Color.WHITE));
                event.setCancelled(true);
                return;
            }
            if (gem.is(player.getInventory().getItemInOffHand())) {
                circle.render(player.getWorld(), new DustParticle(Color.WHITE));
                event.setCancelled(true);
                return;
            }
        }
    }
}
