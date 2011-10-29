package kjhf.falconpunch;
import java.io.File;
import org.bukkit.util.config.*;

public class FPConfig extends FalconPunch {
    public Configuration load(File file) {
        try { 
            Configuration configuration = new Configuration(file);
            configuration.load();
            return configuration; 
        } catch (Exception ex) {
            FalconPunch.logger.info("[FalconPunch] Failed to load file: "+file+ ". Error: "+ex);
        }
        return null;
    }
    
    public void checkConfig() {
        new File(dataFolder).mkdir(); //Set-up data folder unless it exists.
        if (!configFile.exists()) {
            setDefaults();
        } else {
            String temp = getProperty("AllowPVPFalconPunch", "'true'", configFile).replace("'","");
            if ((temp.equalsIgnoreCase("true")) || (temp.equalsIgnoreCase("t")) || (temp.equalsIgnoreCase("yes")) || (temp.equalsIgnoreCase("y"))) {
                FalconPunch.AllowPVP = true;
            } else {
                FalconPunch.AllowPVP = false;
            }
            
            temp = getProperty("Criticals.UseCriticalsSystem", "'true'", configFile).replace("'","");
            if ((temp.equalsIgnoreCase("true")) || (temp.equalsIgnoreCase("t")) || (temp.equalsIgnoreCase("yes")) || (temp.equalsIgnoreCase("y"))) {
                FalconPunch.Criticals = true;
            } else {
                FalconPunch.Criticals = false;
            }
            temp = getProperty("Criticals.CriticalsChance", "1", configFile).replace("'","");
            try {
                FalconPunch.CriticalsChance = Byte.parseByte(temp);
            } catch (Exception e) {
                try {
                    FalconPunch.CriticalsChance = (byte) Double.parseDouble(temp);
                } catch (Exception ex) {
                    FalconPunch.logger.info("[FalconPunch] Your CriticalsChance in config is not a recognised number. Please use a pure number between 0 and 100. Using default 1.");
                    FalconPunch.logger.info("[FalconPunch] "+ex);
                    FalconPunch.CriticalsChance = 1;
                }
            } 
            
            temp = getProperty("Criticals.UseBurnSystem", "'true'", configFile).replace("'","");
            if ((temp.equalsIgnoreCase("true")) || (temp.equalsIgnoreCase("t")) || (temp.equalsIgnoreCase("yes")) || (temp.equalsIgnoreCase("y"))) {
                FalconPunch.Burn = true;
            } else {
                FalconPunch.Burn = false;
            } 
            temp = getProperty("Criticals.BurnChance", "1", configFile).replace("'","");
            try {
                FalconPunch.BurnChance = Byte.parseByte(temp);
            } catch (Exception e) {
                try {
                    FalconPunch.BurnChance = (byte) Double.parseDouble(temp);
                } catch (Exception ex) {
                    FalconPunch.logger.info("[FalconPunch] Your BurnChance in config is not a recognised number. Please use a pure number between 0 and 100. Using default 1.");
                    FalconPunch.logger.info("[FalconPunch] "+ex);
                    FalconPunch.BurnChance = 1;
                }
            } 
            
            temp = getProperty("Fails.UseFailHitsSystem", "'true'", configFile).replace("'","");
            if ((temp.equalsIgnoreCase("true")) || (temp.equalsIgnoreCase("t")) || (temp.equalsIgnoreCase("yes")) || (temp.equalsIgnoreCase("y"))) {
                FalconPunch.Fail = true;
            } else {
                FalconPunch.Fail = false;
            } 
            temp = getProperty("Fails.FailChance", "1", configFile).replace("'","");
            try {
                FalconPunch.FailChance = Byte.parseByte(temp);
            } catch (Exception e) {
                try {
                    FalconPunch.FailChance = (byte) Double.parseDouble(temp);
                } catch (Exception ex) {
                    FalconPunch.logger.info("[FalconPunch] Your FailChance in config is not a recognised number. Please use a pure number between 0 and 100. Using default 1.");
                    FalconPunch.logger.info("[FalconPunch] "+ex);
                    FalconPunch.FailChance = 1;
                }
            }
            
            temp = getProperty("Fails.UseFailFireHitsSystem", "'true'", configFile).replace("'","");
            if ((temp.equalsIgnoreCase("true")) || (temp.equalsIgnoreCase("t")) || (temp.equalsIgnoreCase("yes")) || (temp.equalsIgnoreCase("y"))) {
                FalconPunch.FailFire = true;
            } else {
                FalconPunch.FailFire = false;
            } 
            temp = getProperty("Fails.FailFireChance", "1", configFile).replace("'","");
            try {
                FalconPunch.FailFireChance = Byte.parseByte(temp);
            } catch (Exception e) {
                try {
                    FalconPunch.FailFireChance = (byte) Double.parseDouble(temp);
                } catch (Exception ex) {
                    FalconPunch.logger.info("[FalconPunch] Your FailFireChance in config is not a recognised number. Please use a pure number between 0 and 100. Using default 1.");
                    FalconPunch.logger.info("[FalconPunch] "+ex);
                    FalconPunch.FailFireChance = 1;
                }
            }
        }
    }
    
    public void setDefaults() {
        if (!configFile.exists()) {
            FalconPunch.logger.info("[FalconPunch] Creating config file.");
            String Header[] = new String[] {
                "# Enables/disables PVP Falcon Punch. Does not affect immune permission.",    
                "AllowPVPFalconPunch: true",
                "",
                "Criticals:",
                "    # Enable/disable critical hit system and set chance out of 100.",
                "    UseCriticalsSystem: true",
                "    CriticalsChance: 1",
                "    # Enable/disable burn hit system and set chance out of 100.",
                "    UseBurnSystem: true",
                "    BurnChance: 1",
                "",
                "Fails:",
                "    # Enable/disable fails hit system (does nothing but fails the hit) and set chance out of 100.",
                "    UseFailHitsSystem: true",
                "    FailChance: 1",
                "    # Enable/disable fails hit system (that sets the puncher on fire!) and set chance out of 100. ",
                "    # Independant to the UseFailHitsSystem, i.e. if they are both turned on, there is a greater chance of the hit failing!",
                "    UseFailFireHitsSystem: true" 
            };
            try {
                configFile.createNewFile();
                Configuration configConfig = load(configFile);
                configConfig.setHeader(Header);
                configConfig.setProperty("Fails.FailFireChance", "1"); // Annoying {} occurs at the end if a property is not set.
                configConfig.save();
            } catch(Exception ex) { 
                FalconPunch.logger.info("[FalconPunch] Failed to create config file." +ex);   
            }
        }
    }
    
    public void setProperty(String root, Object object, File file) {
        try {
            Configuration configuration = load(file);
            configuration.setProperty(root, object);
            configuration.save();
        } catch (Exception ex) {
            FalconPunch.logger.info("[FalconPunch] An error occured in setting a property. Please rectify the files or send this to kjhf:");
            FalconPunch.logger.info(ex+" when trying to setProperty "+root+", "+object+" ("+object.toString()+"), "+file+" ("+file.toString()+").");
        }
    }
    public String getProperty(String root, String defaultval, File file) {
        Configuration configuration = load(file);
        Object property = configuration.getProperty(root);
        if (property == null) {
            try {
                configuration.setProperty(root, defaultval);
                configuration.save();
                property = defaultval;
            } catch (Exception ex) {
                FalconPunch.logger.info("[FalconPunch] An error occured in setting a default value. Please rectify the files or send this to kjhf:");
                FalconPunch.logger.info(ex+" when trying to setProperty "+root+", "+defaultval+" ("+defaultval.toString()+"), "+file+" ("+file.toString()+").");
            }
        }
        return property.toString();
    } 
}
