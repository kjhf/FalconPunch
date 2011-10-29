package kjhf.falconpunch;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.util.Vector;

public class fpPlayerListener extends PlayerListener{
    
    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() != Material.AIR) {
            return;
        }
        
        if (!FalconPunch.hasPerm(player, "punch")) {
            return;
        }
        
        Entity targetEntity = event.getRightClicked();

        if (targetEntity instanceof Wolf) {
            Wolf wolf = (Wolf)targetEntity;
            if (wolf.isTamed()) {
                if ((Player)wolf.getOwner() != null) {
                    Player owner = (Player)wolf.getOwner();
                    if (player == owner) {
                        return;
                    }
                }
            }
        }

        if (targetEntity instanceof Player) {
            if (FalconPunch.AllowPVP == false) {
                return;
            }
            Player targetplayer = (targetEntity instanceof Player) ? (Player)targetEntity : null;
            if (FalconPunch.hasPerm(targetplayer, "immune")) {
                player.sendMessage("[FalconPunch] That person cannot be falcon punched. They have immune permission.");
                targetplayer.sendMessage("[FalconPunch] " +player.getName()+ " tried to falcon punch you!");
                return;
            }
        }
        
        Random random = new Random();
        int i = random.nextInt(99)+1;
        if (i <= FalconPunch.FailChance && FalconPunch.Fail) {
            player.sendMessage("FALCON... Fail?!");
            return;
        }

        random = new Random();
        i = random.nextInt(99)+1;
        if (i <= FalconPunch.FailFireChance && FalconPunch.FailFire) {
            player.setFireTicks(200);
            player.sendMessage("FALCON... Fail? [Burn Hit! Oh Noes!]");
            return;
        }

        int crit = 1;
        random = new Random();
        i = random.nextInt(99)+1;
        if (i <= FalconPunch.CriticalsChance && FalconPunch.Criticals) {
            crit = 2;
        }
        boolean burncrit = false;
        random = new Random();
        i = random.nextInt(99)+1;
        if (i <= FalconPunch.BurnChance && FalconPunch.Burn) {
             burncrit = true;
        }
        Vector direction = player.getLocation().getDirection();
        Vector additionalverticle = null;
        if (direction.getY() >= -0.5 && direction.getY() < 0.6) {
             additionalverticle = new Vector(0,0.5,0);    
        } else {
            additionalverticle = new Vector(0,0,0);   
        }
        Vector velocity = new Vector(0,0,0);
        if (player.getVelocity() != null ) {
            velocity = player.getVelocity().add(direction).add(additionalverticle).multiply(10).multiply(crit);
        } else {
            velocity = velocity.add(direction).add(additionalverticle).multiply(10).multiply(crit);
        }
        Entity punched = event.getRightClicked();
        punched.setVelocity(velocity);
        if (burncrit == true) {
            punched.setFireTicks(200);
            if (crit != 1) {
                player.sendMessage("FALCON... PAUNCH! [Burn + Critical Hit!]");
                return;
            } else {
                player.sendMessage("FALCON... PAUNCH! [Burn Hit!]");
                return;
            }
        } else {
            if (crit != 1) {
                player.sendMessage("FALCON... PAUNCH! [Critical Hit!]");
            } else {
                player.sendMessage("FALCON... PAUNCH!");
            }
        }
    }
}