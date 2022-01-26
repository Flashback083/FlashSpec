package fr.flashback083.flashspec.config;

import fr.flashback083.flashspec.FlashSpec;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    private static final String CATEGORY_GENERAL = "General";
    private static String[] specflags = {};
    private static int aggrorange = 5;
    private static int aggrocooldown = 5;
    private static boolean allowRegionalFormSpec = false;

    //-----------------------------------------------------//
  
    
    public static void readConfig() {
        Configuration cfg = FlashSpec.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e1) {
            FlashSpec.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
       
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        specflags = cfg.getStringList("specflags", CATEGORY_GENERAL, specflags, "List of spec flag you wanna create. Be carefull that the flag isnt already used by another plugin!");
        aggrorange = cfg.getInt("aggrorange",CATEGORY_GENERAL,aggrorange,1,Integer.MAX_VALUE,"Set the range for aggro spec");
        aggrocooldown = cfg.getInt("aggrocooldown",CATEGORY_GENERAL,aggrocooldown,1,Integer.MAX_VALUE,"Set the cooldown before player can be aggro again with aggro spec");
        allowRegionalFormSpec = cfg.getBoolean("allowRegionalFormSpec", CATEGORY_GENERAL, allowRegionalFormSpec, "Set to true to allow regional region to be given with flashspec command");

    }
    

    

}
