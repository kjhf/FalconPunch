package kjhf.falconpunch;

import java.io.IOException;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

public class FalconPunch extends JavaPlugin {
    
    final String dataFolder = "plugins/FalconPunch/";
    final File configFile = new File(dataFolder + "config.yml");
    
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
        Configuration config = null;
        try {
            configFile.createNewFile();
        } catch (IOException ex) {}
        try {
            config = new Configuration(configFile);
            config.load();
        } catch (Exception e) {
            this.getLogger().warning("Could not load config.yml.");
        }
        if (config != null) {
            String Header[] = new String[] {
                "# AllowPVPFalconPunch - Enables/disables PVP Falcon Punch. Does not affect immune permission.",
                "# OnlyPVPFalconPunch - Enables/disables PVP ONLY Falcon Punching (i.e. FalconPunches won't work on Entities other than Players).",
                "",
                "# NoImmunity - Turn off the immune system completely",
                "",
                "# UseContinuousSystem - Use the system which has a force of Falcon Punch, rather than just a crit or not. (False reverts to old crit system)",
                "# CriticalsChance - If the old system is enabled, this sets the chance of a critical hit [0-100]. Clearly, 0 disables all crits.",
                "",
                "# BurnChance - Set the chance of a burn hit [0-100]. Clearly, 0 disables this.",
                "",
                "# FailChance - Set the chance of the punch failing [0-100]. Clearly, 0 disables this.",
                "",
                "# -- These values do not have to add up to 100, but it makes it much easier to see the probabilities ! --",
                "# AfterFailDoNothingChance - If a hit fails, set the chance that the punch fails with no side-effects [0-100]",
                "# AfterFailDoFireChance - If a hit fails, set the chance that the puncher will be set on fire [0-100]",
                "# AfterFailDoLightningChance - If a hit fails, set the chance that the puncher will be set on fire [0-100]"
            };
            config.setHeader(Header);

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
            
            try {
                config.save();
            } catch (Exception ex) {
                this.getLogger().warning("Could not write to config.");
            }
        }
    }
}