package fr.flashback083.flashspec.config;

import fr.flashback083.flashspec.FlashSpec;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    private static final String CATEGORY_GENERAL = "General";
    private static String[] specflags = {};
    private static int aggrorange = 5;
    private static int aggrocooldown = 5;

    //-----------------------------------------------------//
  
    
    public static void readConfig() {
        Configuration cfg = FlashSpec.config;
        try {
            cfg.load();
            initGeneralConfig(cfg,CATEGORY_GENERAL);
        } catch (Exception e1) {
            FlashSpec.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
       
    }

    private static void initGeneralConfig(Configuration cfg,String category) {
        cfg.addCustomCategoryComment(category, "General configuration");
        specflags = cfg.getStringList("specflags", category, specflags, "List of spec flag you wanna create. Be carefull that the flag isnt already used by another plugin!");
        aggrorange = cfg.getInt("aggrorange",category,aggrorange,1,Integer.MAX_VALUE,"Set the range for aggro spec");
        aggrocooldown = cfg.getInt("aggrocooldown",category,aggrocooldown,1,Integer.MAX_VALUE,"Set the cooldown before player can be aggro again with aggro spec");

    }
    

    

}
