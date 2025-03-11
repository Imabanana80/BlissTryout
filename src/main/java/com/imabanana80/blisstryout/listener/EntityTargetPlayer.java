package com.imabanana80.blisstryout.listener;

import com.imabanana80.blisstryout.item.SpidermanGem;
import com.imabanana80.potassium.item.ItemRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetPlayer implements Listener {
    @EventHandler
    public void onEntityTargetPlayer(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player player) {
            SpidermanGem gem = (SpidermanGem) ItemRegistry.get(SpidermanGem.class);
            if (gem.is(player.getInventory().getItemInMainHand())) {
                event.setCancelled(true);
                return;
            }
            if (gem.is(player.getInventory().getItemInOffHand())) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
