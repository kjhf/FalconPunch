package kjhf.falconpunch;

import java.io.IOException;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public class FalconPunch extends JavaPlugin {
    
    boolean AllowPVP = true;
    boolean OnlyPVP = false;
    boolean NoImmunity = false;
    
    boolean UseContinuousSystem = true;
    
    int CriticalsChance = 1;
    int BurnChance = 1;
    
    int FailChance = 1;
    int FailNothingChance = 1;
    int FailFireChance = 1;
    int FailLightningChance = 1;

    @Override
    public void onDisable() {
        this.getLogger().info("Version " + this.getDescription().getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        loadConfigs();
        this.getServer().getPluginManager().registerEvents(new fpPlayerListener(this), this);
        this.getLogger().info("Version " + this.getDescription().getVersion() + " is enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("fp")) || (command.getName().equalsIgnoreCase("falconpunch"))) {
            loadConfigs();
            sender.sendMessage("[FalconPunch] Version " + this.getDescription().getVersion() + " reloaded.");
            return true;
        } else {
            return false;
        }
    }
        
    /** Load up the FalconPunch Config. */
    private void loadConfigs () {
        File configFile=new File(this.getDataFolder(),"config.yml");
        if(!configFile.exists()){
            this.saveDefaultConfig();
        }
        FileConfiguration config = this.getConfig();

        AllowPVP = config.getBoolean("Settings.AllowPVPFalconPunch", true);
        OnlyPVP = config.getBoolean("Settings.OnlyPVPFalconPunch", false);
        NoImmunity = config.getBoolean("Settings.NoImmunity", false);
        UseContinuousSystem = config.getBoolean("Criticals.UseContinuousSystem", true);
        CriticalsChance = config.getInt("Criticals.CriticalsChance", 1);
        BurnChance = config.getInt("Burns.BurnChance", 1);
        FailChance = config.getInt("Fails.FailChance", 1);
        FailNothingChance = config.getInt("Fails.AfterFailDoNothingChance", 34);
        FailFireChance = config.getInt("Fails.AfterFailDoFireChance", 33);
        FailLightningChance = config.getInt("Fails.AfterFailDoLightningChance", 33);
    }
}