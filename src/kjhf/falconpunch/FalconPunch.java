package kjhf.falconpunch;
import org.bukkit.plugin.java.*;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class FalconPunch extends JavaPlugin {
    protected static FalconPunch fp;
    protected static final Logger logger = Logger.getLogger("FalconPunch");
    protected static String Version;
    
    protected final fpPlayerListener playerListener = new fpPlayerListener();    
    private static PermissionHandler permissionHandler;
    protected static boolean permissionBukkit = false;
    
    protected static final String dataFolder = "plugins/FalconPunch/";
    protected static final File configFile = new File(dataFolder + "config.yml");
    
    protected static boolean AllowPVP = true;
    protected static boolean Criticals = true;
    protected static byte CriticalsChance = 1;
    protected static boolean Burn = true;
    protected static byte BurnChance = 1;
    protected static boolean Fail = true;
    protected static byte FailChance = 1;
    protected static boolean FailFire = true;
    protected static byte FailFireChance = 1;

    @Override
    public void onDisable() {
        logger.info("[FalconPunch] Version " + Version + " disabled.");
    }

    @Override
    public void onEnable() {
        fp = this;
        Version = this.getDescription().getVersion();
        
        FPConfig config = new FPConfig();
        config.checkConfig();
        
        // Permissions
        setupPermissions();
        
        PluginManager pm = getServer().getPluginManager();   
        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Monitor, this);
            
        logger.info("[FalconPunch] Version " + Version + " is enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((command.getName().equalsIgnoreCase("fp")) || (command.getName().equalsIgnoreCase("falconpunch"))) {
            FPConfig config = new FPConfig();
            config.checkConfig();
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
    
    protected static boolean hasPerm(Player player, String permission) { // Convenience. Checks both Permissions and PermissionsBukkit
        if (player.isOp()) {
            return true;
        }
        if (permissionBukkit) {
            Permission perm = Bukkit.getServer().getPluginManager().getPermission("falconpunch."+permission);
            if (player.hasPermission(perm)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (permissionHandler == null) {
                return false;
            } else {
                return permissionHandler.has(player, "falconpunch."+permission);
            }
        }
    }
}