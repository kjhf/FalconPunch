package kjhf.falconpunch;

import java.io.IOException;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;

public class FalconPunch extends JavaPlugin {
    static FalconPunch fp;
    static final Logger logger = Logger.getLogger("FalconPunch");
    static String Version;
    
    final fpPlayerListener playerListener = new fpPlayerListener();    
    static PermissionHandler permissionHandler;
    static boolean permissionBukkit = false;
    
    static final String dataFolder = "plugins/FalconPunch/";
    static final File configFile = new File(dataFolder + "config.yml");
    
    static boolean AllowPVP = true;
    static boolean OnlyPVP = false;
    static boolean NoImmunity = false;
    
    static boolean UseContinuousSystem = true;
    
    static int CriticalsChance = 1;
    static int BurnChance = 1;
    
    static int FailChance = 1;
    static int FailNothingChance = 1;
    static int FailFireChance = 1;
    static int FailLightningChance = 1;

    @Override
    public void onDisable() {
        logger.info("[FalconPunch] Version " + Version + " disabled.");
    }

    @Override
    public void onEnable() {
        fp = this;
        Version = this.getDescription().getVersion();
        loadConfigs();
        
        // Permissions
        setupPermissions();
        
        PluginManager pm = getServer().getPluginManager();   
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Monitor, this);
            
        logger.info("[FalconPunch] Version " + Version + " is enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("fp")) || (command.getName().equalsIgnoreCase("falconpunch"))) {
            Version = this.getDescription().getVersion();
            loadConfigs();
            sender.sendMessage("[FalconPunch] Version " + Version + " reloaded.");
            return true;
        } else {
            return false;
        }
    }
        
    private void setupPermissions() { 
        if (permissionHandler != null) { 
            return; 
        }        
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
        if (permissionsPlugin == null) {
            Plugin permissionsbukkitPlugin = this.getServer().getPluginManager().getPlugin("PermissionsBukkit");
            if (permissionsbukkitPlugin == null) {
                logger.info("[FalconPunch] Permission system not detected. Attempting to use server permissions and defaulting to OP...");    
                return;
            } else {
            logger.info("[FalconPunch] Found PermissionsBukkit sucessfully!");
            permissionBukkit = true;
            }
        } else {
            permissionHandler = ((Permissions) permissionsPlugin).getHandler();
            logger.info("[FalconPunch] Found "+((Permissions)permissionsPlugin).getDescription().getFullName()+" sucessfully!");
        }
    }

    /**  
     * Test whether a player can use this falconpunch permission. 
     * It checks BukkitPermissions, PEX, and finally OPS.
     * @param player The player issuing the command
     * @param permission The permission to check against that are associated with the command. "falconpunch." is included.
     * @return True if sender has permission, else false. 
     */
    public static boolean hasPerm (Player player, String permission) { // Convenience. Checks both Permissions and PermissionsBukkit
        if (permissionBukkit) {
            // Server is using BukkitPermissions
            
            Permission perm;
            if (fp.getServer().getPluginManager().getPermission("falconpunch."+permission) != null) {
                perm = fp.getServer().getPluginManager().getPermission("falconpunch."+permission);
            } else {
                return player.isOp(); // The permission was not found. Return whether player should automatically get permission for being opped.
            }
            if (player.hasPermission(perm)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (permissionHandler == null) {
                return player.isOp(); // Permissions/PEX isn't installed either. Revert to OPS.
            } else {
                return permissionHandler.has(player, "falconpunch."+permission);
            }
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
            logger.warning("[FalconPunch] Could not load config.yml.");
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
                logger.warning("[FalconPunch] Could not write to config.");
            }
        }
    }
}