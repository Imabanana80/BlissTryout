package com.imabanana80.blisstryout.command;

import com.imabanana80.blisstryout.item.SpidermanGem;
import com.imabanana80.potassium.item.ItemRegistry;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

public class GiveGemCommand {
    @Command("givegem")
    @Permission("blisstryout.givegem")
    public void onGiveGemCommand(
            CommandSender sender
    ) {
        if (sender instanceof Player player) {
            player.getInventory().addItem(ItemRegistry.get(SpidermanGem.class).getItemStack());
        }
    }
}
