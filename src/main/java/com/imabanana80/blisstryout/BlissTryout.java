package com.imabanana80.blisstryout;

import com.imabanana80.blisstryout.command.GiveGemCommand;
import com.imabanana80.blisstryout.item.SpidermanGem;
import com.imabanana80.blisstryout.listener.EntityDamageListener;
import com.imabanana80.blisstryout.listener.EntityTargetPlayer;
import com.imabanana80.blisstryout.listener.ProjectileHitListener;
import com.imabanana80.potassium.item.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.LegacyPaperCommandManager;

import java.util.logging.Level;

public class BlissTryout extends JavaPlugin {

    private static BlissTryout instance;

    @Override
    public void onEnable(){
        instance = this;
        final LegacyPaperCommandManager<CommandSender> commandManager = new LegacyPaperCommandManager<>(
                instance,
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.identity()
        );
        if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)){
            commandManager.registerBrigadier();
        } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }
        AnnotationParser<CommandSender> parser = new AnnotationParser<>(commandManager, CommandSender.class);
        parser.parse(new GiveGemCommand());

        if (!Bukkit.getServer().getName().equalsIgnoreCase("paper")){
            instance.getLogger().log(Level.SEVERE, "Server is not running paper and thus may not be supported by this plugin!");
        }

        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityTargetPlayer(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);

        ItemRegistry.register(new SpidermanGem(this));
    }

    public static BlissTryout getInstance(){return instance;}
}
