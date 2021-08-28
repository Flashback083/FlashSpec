package fr.flashback083.flashspec.config;

import fr.flashback083.flashspec.FlashSpec;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Lang {

    private static final String CATEGORY_LANG = "lang";
    private static String transformmessage = "&f[&dPixelmon&f]&7 Oh? &a%previouspoke% &7was actually a §a%newpokemon% §7in disguise!";
    private static String uncatchabletag = "%pokemonname% &c(Uncatchable)";
    private static String unbattleabletag = "%pokemonname% &c(Unbattleable)";
    private static String unbattleablemsg = "&cThis %pokemonname% is unbattleable!";
    private static String unevolvemsg = "&cThis %pokemonname% can't evolve!";
    private static String unlevelmsg = "&cThis %pokemonname% can't level up or gain XP!";
    private static String undeleteablemsg = "This %pokemonname% can't be deleted!";
    private static String unmegaoutmsg = "&cYou can't Mega evolve this %pokemonname% outside a battle!";
    private static String unnickablemsg = "&cYou can't change the name of this %pokemonname%!";
    private static String unhatchablemsg = "&cYou can't hatch this %pokemonname% with the hatch command!";

    //-----------------------------------------------------//
  
    
    public static void readConfig() {
        Configuration cfg = FlashSpec.lang;
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
        cfg.addCustomCategoryComment(CATEGORY_LANG, "Language configuration");
        transformmessage = cfg.getString("transformmessage", CATEGORY_LANG, transformmessage, "Message displayed when the onCatch spec is used. %previouspoke% and %newpokemon%");
        uncatchabletag = cfg.getString("uncatchabletag", CATEGORY_LANG, uncatchabletag, "Tag above the pokemon to tell the player its an uncatchable pokemon. %pokemonname%");
        unbattleabletag = cfg.getString("unbattleabletag", CATEGORY_LANG, unbattleabletag, "Tag above the pokemon to tell the player its an unbattleable pokemon. %pokemonname%");
        unbattleablemsg = cfg.getString("unbattleablemsg", CATEGORY_LANG, unbattleablemsg, "Message when player try to battle a non-battleable pokemon. %pokemonname%");
        unevolvemsg = cfg.getString("unevolvemsg", CATEGORY_LANG, unevolvemsg, "Message when player try to evolve a non-evolveable pokemon. %pokemonname%");
        unlevelmsg = cfg.getString("unlevelmsg", CATEGORY_LANG, unlevelmsg, "Message when player try to leveluop a non-levelable pokemon. %pokemonname%");
        undeleteablemsg = cfg.getString("undeleteablemsg", CATEGORY_LANG, undeleteablemsg, "Message when player try to delete a non-deleteable pokemon. %pokemonname%");
        unmegaoutmsg = cfg.getString("unmegaoutmsg", CATEGORY_LANG, unmegaoutmsg, "Message when player try to mega evo a non-mega evo able pokemon. %pokemonname%");
        unnickablemsg = cfg.getString("unnickablemsg", CATEGORY_LANG, unnickablemsg, "Message when player try to nick a non-unnickable pokemon. %pokemonname%");
        unhatchablemsg = cfg.getString("unhatchablemsg", CATEGORY_LANG, unhatchablemsg, "Message when player try to hatch a non-hatchable pokemon. %pokemonname%");
 }
    

    

}
