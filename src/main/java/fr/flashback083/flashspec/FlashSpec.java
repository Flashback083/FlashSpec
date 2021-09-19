package fr.flashback083.flashspec;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.pokemon.SpecFlag;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import fr.flashback083.flashspec.Specs.*;
import fr.flashback083.flashspec.Specs.EvsSpec.*;
import fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec.LegGen1Spec;
import fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec.LegSpec;
import fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec.LegUbSpec;
import fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec.UbSpec;
import fr.flashback083.flashspec.Specs.GiveSpec.evospec.LastEvoSpec;
import fr.flashback083.flashspec.Specs.IvsSpec.*;
import fr.flashback083.flashspec.Specs.IvsSpec.changeStats.*;
import fr.flashback083.flashspec.Specs.LevelSpec.LevelRangeSpec;
import fr.flashback083.flashspec.Specs.LevelSpec.LevelSpec;
import fr.flashback083.flashspec.Specs.MatchSpec.*;
import fr.flashback083.flashspec.Specs.MatchSpec.EggGroupSpec.EggGroupSpec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move1Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move2Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move3Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move4Spec;
import fr.flashback083.flashspec.Specs.TransformSpec.CatchTransformSpec;
import fr.flashback083.flashspec.Specs.TransformSpec.TransformChanceSpec;
import fr.flashback083.flashspec.cmds.CatchSpecCmd;
import fr.flashback083.flashspec.cmds.FlashSpecCallback;
import fr.flashback083.flashspec.cmds.FlashSpecCmd;
import fr.flashback083.flashspec.config.Config;
import fr.flashback083.flashspec.config.Lang;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Mod(
        modid = FlashSpec.MOD_ID,
        name = FlashSpec.MOD_NAME,
        version = FlashSpec.VERSION,
        serverSideOnly = true,
        dependencies = "required-after:pixelmon",
        acceptableRemoteVersions = "*"
)
public class FlashSpec {

    public static final String MOD_ID = "flashspec";
    public static final String MOD_NAME = "FlashSpec";
    public static final String VERSION = "2.6.4";


    public static File speclist;
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Configuration lang;
    public static Configuration config;

    public static List<String> leg1 = Lists.newArrayList();
    public static List<String> leg2 = Lists.newArrayList();
    public static List<String> leg3 = Lists.newArrayList();
    public static List<String> leg4 = Lists.newArrayList();
    public static List<String> leg5 = Lists.newArrayList();
    public static List<String> leg6 = Lists.newArrayList();
    public static List<String> leg7 = Lists.newArrayList();
    public static List<String> leg8 = Lists.newArrayList();



    //Boolean spec
    //public static PokemonSpec UNCATCHABLE;
    //public static PokemonSpec UNBATTLEABLE;
    //public static PokemonSpec UNEVO;
    //public static PokemonSpec UNLEVEL;
    //public static PokemonSpec AGGRO;
    //public static PokemonSpec UNTRASHABLE;
    //public static PokemonSpec UNDELETEABLE;

    private static MinecraftServer server;
    public static Logger logger;
    //public static PokemonSpec NICKNAME;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static FlashSpec INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) throws IOException {
        logger = event.getModLog();
        File directory = new File(event.getModConfigurationDirectory(), MOD_NAME);
        directory.mkdir();
        speclist = new File(directory, "specdata.json");
        boolean check = speclist.exists();
        if (!check) {
            PrintWriter start = new PrintWriter(speclist,"UTF-8");
            String json = "{}";
            start.write(json);
            start.close();
        }
        lang = new Configuration(new File(directory.getPath(), "lang.cfg"));
        Lang.readConfig();
        config = new Configuration(new File(directory.getPath(), "config.cfg"));
        Config.readConfig();

        //Add spec
        PokemonSpec.extraSpecTypes.add(new Move1Spec(Lists.newArrayList("move1","m1"), null));
        PokemonSpec.extraSpecTypes.add(new Move2Spec(Lists.newArrayList("move2","m2"), null));
        PokemonSpec.extraSpecTypes.add(new Move3Spec(Lists.newArrayList("move3","m3"), null));
        PokemonSpec.extraSpecTypes.add(new Move4Spec(Lists.newArrayList("move4","m4"), null));
        logger.info("[FlashSpec] Registered move1/move2/move3/move4 spec");
        PokemonSpec.extraSpecTypes.add(new HeldItemSpec(Lists.newArrayList("helditem","hi"), null));
        logger.info("[FlashSpec] Registered helditem spec");
        PokemonSpec.extraSpecTypes.add(new LegUbSpec(false));
        logger.info("[FlashSpec] Registered uborleg spec");
        //PokemonSpec.extraSpecTypes.add(new Type1Spec(Lists.newArrayList("type1"), null));
        //PokemonSpec.extraSpecTypes.add(new Type2Spec(Lists.newArrayList("type2"), null));
        //PokemonSpec.extraSpecTypes.add(new TypeSpec(Lists.newArrayList("type"), null));
        PokemonSpec.extraSpecTypes.add(new AbsSpec(Lists.newArrayList("abs"), null));
        logger.info("[FlashSpec] Registered abs spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unbattleable"));
        logger.info("[FlashSpec] Registered unbattleable spec");
        PokemonSpec.extraSpecTypes.add(new NumIVs(0));
        logger.info("[FlashSpec] Registered numivs spec");
        PokemonSpec.extraSpecTypes.add(new AISpec(Lists.newArrayList("ai"), null));
        logger.info("[FlashSpec] Registered ai spec");
        //PokemonSpec.extraSpecTypes.add(new SpecialTextureSpec(Lists.newArrayList("sptexture"), null));
        PokemonSpec.extraSpecTypes. add(new LegSpec(false));
        logger.info("[FlashSpec] Registered leg spec");
        PokemonSpec.extraSpecTypes.add(new UbSpec(false));
        logger.info("[FlashSpec] Registered ub spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unevo"));
        logger.info("[FlashSpec] Registered unevo spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unlevel"));
        logger.info("[FlashSpec] Registered unlevel spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("uncatchable"));
        logger.info("[FlashSpec] Registered uncatchable spec");
        //PokemonSpec.extraSpecTypes.add(new UncatchableSpec(false));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("aggro"));
        logger.info("[FlashSpec] Registered aggro spec");
        PokemonSpec.extraSpecTypes.add(new LevelSpec(Lists.newArrayList("lvlc","levelc"), null));
        logger.info("[FlashSpec] Registered levelc spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("untrashable"));
        logger.info("[FlashSpec] Registered untrashable spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("undeleteable"));
        logger.info("[FlashSpec] Registered undeleteable spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unmega"));
        logger.info("[FlashSpec] Registered unmega spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unmegabattle"));
        logger.info("[FlashSpec] Registered unmegabattle spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("undropable"));
        logger.info("[FlashSpec] Registered undropable spec");
        PokemonSpec.extraSpecTypes.add(new DropSpec(Lists.newArrayList("dropitem","di"), null));
        PokemonSpec.extraSpecTypes.add(new DropChanceSpec(Lists.newArrayList("dropchance","dc"), null));
        PokemonSpec.extraSpecTypes.add(new DropISSpec(Lists.newArrayList("dropitemsaver","dis"), null));
        logger.info("[FlashSpec] Registered dropitem/dropchance/dropitemsaver spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unmegaout"));
        logger.info("[FlashSpec] Registered unmegaout spec");
        //PokemonSpec.extraSpecTypes.add(new SpecFlag("unmegain"));
        PokemonSpec.extraSpecTypes.add(new LevelRangeSpec(Lists.newArrayList("lvlr","levelr"), null));
        logger.info("[FlashSpec] Registered levelr spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unnickable"));
        logger.info("[FlashSpec] Registered unnickable spec");
        PokemonSpec.extraSpecTypes.add(new Ivs31AndMinPercent(Lists.newArrayList("iv31&min%"), null));
        logger.info("[FlashSpec] Registered iv31&min% spec");
        PokemonSpec.extraSpecTypes.add(new EggStepSpec(Lists.newArrayList("reggsteps"), null));
        logger.info("[FlashSpec] Registered reggsteps spec");
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unhatchable"));
        logger.info("[FlashSpec] Registered unhatchable spec");
        PokemonSpec.extraSpecTypes.add(new NumMaxIVs(0));
        logger.info("[FlashSpec] Registered nummaxivs spec");
        //PokemonSpec.extraSpecTypes.add(new EggGroup1Spec(Lists.newArrayList("egggroup1"), null));
        //PokemonSpec.extraSpecTypes.add(new EggGroup2Spec(Lists.newArrayList("egggroup2"), null));

        //PokemonSpec.extraSpecTypes.add(new MaxIvs31AndMinPercent(Lists.newArrayList("maxiv31&min%"), null));
        //PokemonSpec.extraSpecTypes.add(new YRangeSpec(Lists.newArrayList("yrange"), null));
        //PokemonSpec.extraSpecTypes.add(new YChangeSpec(Lists.newArrayList("ychange"), null));
        PokemonSpec.extraSpecTypes.add(new CatchTransformSpec(Lists.newArrayList("oncatch"), null));
        PokemonSpec.extraSpecTypes.add(new TransformChanceSpec(Lists.newArrayList("transformchance"), null));
        logger.info("[FlashSpec] Registered oncatch/transformchance spec");
        PokemonSpec.extraSpecTypes.add(new HappinessSpec(Lists.newArrayList("happiness"), null));
        logger.info("[FlashSpec] Registered happiness spec");
        //PokemonSpec.extraSpecTypes.add(new SpecFlag("unpokedexable"));
        //logger.info("[FlashSpec] Registered unpokedexable spec");
        //Time spec
        PokemonSpec.extraSpecTypes.add(new SpecFlag("tdusk"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("tday"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("tdawn"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("tnight"));
        logger.info("[FlashSpec] Registered tdusk/tday/tdawn/tnight spec");
        PokemonSpec.extraSpecTypes.add(new LegGen1Spec(false));
        logger.info("[FlashSpec] Registered leg1 spec");
        //CustomSpec import
        //PokemonSpec.extraSpecTypes.add(new TextureSpec(Lists.newArrayList("texture"), null));
        PokemonSpec.extraSpecTypes.add(new OTSpec(Lists.newArrayList("ot", "originaltrainer"), null));
        logger.info("[FlashSpec] Registered originaltrainer spec");
        PokemonSpec.extraSpecTypes.add(new NicknameSpec(null));
        logger.info("[FlashSpec] Registered nickname spec");
        PokemonSpec.extraSpecTypes.add(new MinIVPercent(0));
        logger.info("[FlashSpec] Registered miniv% spec");
        if (!Loader.isModLoaded("aquaauras")){
            PokemonSpec.extraSpecTypes.add(new AuraSpec(null));
            logger.info("[FlashSpec] Registered aura spec");
        }
        //Leg spec
        //PokemonSpec.extraSpecTypes.add(new SpecFlag("leg1"));

        //MatchSpec
        PokemonSpec.extraSpecTypes.add(new MovesSpec(Lists.newArrayList("moves","knowsmove"), null));
        logger.info("[FlashSpec] Registered moves spec [MatchOnly]");
        PokemonSpec.extraSpecTypes.add(new AllSpec(false));
        logger.info("[FlashSpec] Registered all spec [MatchOnly]");
        PokemonSpec.extraSpecTypes.add(new SpawnerSpec(false));
        logger.info("[FlashSpec] Registered spawner spec [MatchOnly]");
        PokemonSpec.extraSpecTypes.add(new DimSpec(Lists.newArrayList("dim"), null));
        logger.info("[FlashSpec] Registered dim spec [MatchOnly]");
        PokemonSpec.extraSpecTypes.add(new DimNameSpec(Lists.newArrayList("dimname"), null));
        logger.info("[FlashSpec] Registered dimname spec [MatchOnly]");
        PokemonSpec.extraSpecTypes.add(new EggGroupSpec(Lists.newArrayList("egggroup"), null));
        logger.info("[FlashSpec] Registered egggroup spec [MatchOnly]");
        //LegUbLevel
        PokemonSpec.extraSpecTypes.add(new fr.flashback083.flashspec.Specs.GiveSpec.LegUbLevelSpec.LegGen1Spec(false));
        PokemonSpec.extraSpecTypes.add(new fr.flashback083.flashspec.Specs.GiveSpec.LegUbLevelSpec.LegSpec(false));
        PokemonSpec.extraSpecTypes.add(new fr.flashback083.flashspec.Specs.GiveSpec.LegUbLevelSpec.UbSpec(false));
        PokemonSpec.extraSpecTypes.add(new fr.flashback083.flashspec.Specs.GiveSpec.LegUbLevelSpec.LegUbSpec(false));
        logger.info("[FlashSpec] Registered leg1l/legl/ubl/uborlegl spec");
        //PokemonSpec.extraSpecTypes.add(new HasCustomTextureSpec(false));
        //logger.info("[FlashSpec] Registered hascustomtexture spec");
        PokemonSpec.extraSpecTypes.add(new HAChanceSpec(null));
        logger.info("[FlashSpec] Registered hachancespec spec");
        PokemonSpec.extraSpecTypes.add(new HappinessChangeSpec(Lists.newArrayList("happinessc"), null));
        logger.info("[FlashSpec] Registered happinesschange spec");
        PokemonSpec.extraSpecTypes.add(new ShinyChanceSpec(null));
        logger.info("[FlashSpec] Registered shinychance spec");
        PokemonSpec.extraSpecTypes.add(new CloneSpec(Lists.newArrayList("clone"), null));
        logger.info("[FlashSpec] Registered Clone spec");
        PokemonSpec.extraSpecTypes.add(new HPSpec(Lists.newArrayList("hp"), null));
        logger.info("[FlashSpec] Registered HP spec");
        PokemonSpec.extraSpecTypes.add(new MinNumIVs(0));
        logger.info("[FlashSpec] Registered minnumivs spec");
        PokemonSpec.extraSpecTypes.add(new MinIvs31AndMinPercent(Lists.newArrayList("miniv31&min%"), null));
        logger.info("[FlashSpec] Registered miniv31&min% spec");

        PokemonSpec.extraSpecTypes.add(new SpecFlag("unexpable"));
        logger.info("[FlashSpec] unexpable spec");

        //Weather spec
        PokemonSpec.extraSpecTypes.add(new SpecFlag("rain"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("thunder"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("clear"));
        logger.info("[FlashSpec] Registered rain/thunder/clear spec");

        PokemonSpec.extraSpecTypes.add(new CustomTextureChanceSpec(null));
        logger.info("[FlashSpec] Registered customtexturechance spec");
        PokemonSpec.extraSpecTypes.add(new isBossSpec(false));
        logger.info("[FlashSpec] Registered isBoss spec [MatchOnly]");
        //PokemonSpec.extraSpecTypes.add(new RibbonSpec(null));
        //logger.info("[FlashSpec] Registered fribbon spec");
        PokemonSpec.extraSpecTypes.add(new RealTimeSpec(Lists.newArrayList("realtime"), null));
        logger.info("[FlashSpec] Registered rtime spec");
        PokemonSpec.extraSpecTypes.add(new FormChanceSpec(null));
        logger.info("[FlashSpec] Registered formchance spec");
        PokemonSpec.extraSpecTypes.add(new isMegaSpec(false));
        logger.info("[FlashSpec] Registered isMega spec [MatchOnly]");
        //IV Spec
        PokemonSpec.extraSpecTypes.add(new IVHPSpec(Lists.newArrayList("ivhpf"), null));
        logger.info("[FlashSpec] Registered ivhpf spec");
        PokemonSpec.extraSpecTypes.add(new IVAtkSpec(Lists.newArrayList("ivatkf"), null));
        logger.info("[FlashSpec] Registered ivatkf spec");
        PokemonSpec.extraSpecTypes.add(new IVDefSpec(Lists.newArrayList("ivdeff"), null));
        logger.info("[FlashSpec] Registered ivdeff spec");
        PokemonSpec.extraSpecTypes.add(new IVSpeAtkSpec(Lists.newArrayList("ivspeatkf"), null));
        logger.info("[FlashSpec] Registered ivspeatkf spec");
        PokemonSpec.extraSpecTypes.add(new IVSpeDefSpec(Lists.newArrayList("ivspedeff"), null));
        logger.info("[FlashSpec] Registered ivspedeff spec");
        PokemonSpec.extraSpecTypes.add(new IVSpeedSpec(Lists.newArrayList("ivspeedf"), null));
        logger.info("[FlashSpec] Registered ivspeedf spec");
        //EV Spec
        PokemonSpec.extraSpecTypes.add(new GiveEVHPSpec(Lists.newArrayList("gevhp"), null));
        logger.info("[FlashSpec] Registered gevhp spec");
        PokemonSpec.extraSpecTypes.add(new GiveEVAtkSpec(Lists.newArrayList("gevatk"), null));
        logger.info("[FlashSpec] Registered gevatk spec");
        PokemonSpec.extraSpecTypes.add(new GiveEVDefSpec(Lists.newArrayList("gevdef"), null));
        logger.info("[FlashSpec] Registered gevdef spec");
        PokemonSpec.extraSpecTypes.add(new GiveEVSpeAtkSpec(Lists.newArrayList("gevspeatk"), null));
        logger.info("[FlashSpec] Registered gevspeatk spec");
        PokemonSpec.extraSpecTypes.add(new GiveEVSpeDefSpec(Lists.newArrayList("gevspedef"), null));
        logger.info("[FlashSpec] Registered gevspedef spec");
        PokemonSpec.extraSpecTypes.add(new GiveEVSpeedSpec(Lists.newArrayList("gevspeed"), null));
        logger.info("[FlashSpec] Registered gevspeed spec");

        PokemonSpec.extraSpecTypes.add(new DateSpec(Lists.newArrayList("fdate"), null));
        logger.info("[FlashSpec] Registered fdate spec");
        PokemonSpec.extraSpecTypes.add(new DynamaxLevelSpec(Lists.newArrayList("dynlvl"), null));
        logger.info("[FlashSpec] Registered dynlvl spec");

        PokemonSpec.extraSpecTypes.add(new SpecFlag("despawnable"));
        logger.info("[FlashSpec] Registered despawnable spec");
        PokemonSpec.extraSpecTypes.add(new isWildSpec(false));
        logger.info("[FlashSpec] Registered isWild spec [MatchOnly]");

        PokemonSpec.extraSpecTypes.add(new LastEvoSpec(false));
        logger.info("[FlashSpec] Registered lastevo spec");

        //PokemonSpec.extraSpecTypes.add(new TrioBirdSpec(false));
        //logger.info("[FlashSpec] Registered triobird spec [MatchOnly]");

        PokemonSpec.extraSpecTypes.add(new isEnumSpeciesSpec(null));
        logger.info("[FlashSpec] Registered isEnumSpecies spec");

        PokemonSpec.extraSpecTypes.add(new isNotEnumSpeciesSpec(null));
        logger.info("[FlashSpec] Registered isNotEnumSpecies spec");


        PokemonSpec.extraSpecTypes.add(new isSpecSpec(null));
        logger.info("[FlashSpec] Registered isSpecSpecies spec");

        PokemonSpec.extraSpecTypes.add(new isNotSpecSpec(null));
        logger.info("[FlashSpec] Registered isNotSpecSpec spec");

        PokemonSpec.extraSpecTypes.add(new GigaMaxSpec(false));
        logger.info("[FlashSpec] Registered gigamax spec");



        if (config.getCategory("General").get("specflags").getStringList().length>0){
            ArrayList<String> list = Lists.newArrayList(config.getCategory("General").get("specflags").getStringList());
            list.forEach(spec -> {
                PokemonSpec.extraSpecTypes.add(new SpecFlag(spec));
                logger.info("[FlashSpec] Registered custom specflag " + spec);
            });
        }



    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Pixelmon.EVENT_BUS.register(new PixelmonEvent());
        MinecraftForge.EVENT_BUS.register(new PixelmonEvent());
    }


    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        loadleg1();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
            System.out.println("FlashSpec loaded!");
            server = event.getServer();
            event.registerServerCommand(new CatchSpecCmd());
            event.registerServerCommand(new FlashSpecCallback());
            event.registerServerCommand(new FlashSpecCmd());
    }

    public static EntityPlayerMP getPlayer(String playername){
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
        return server.getPlayerList().getPlayerByUsername(playername);
    }

    public static MinecraftServer getServer(){
        return server;
    }

    private void loadleg1(){
        for (EnumSpecies p : EnumSpecies.LEGENDARY_ENUMS){
            if (p.getGeneration() == 1){
                leg1.add(p.getPokemonName());
            }else if (p.getGeneration() == 2){
                leg2.add(p.getPokemonName());
            }else if (p.getGeneration() == 3){
                leg3.add(p.getPokemonName());
            }else if (p.getGeneration() == 4){
                leg4.add(p.getPokemonName());
            }else if (p.getGeneration() == 5){
                leg5.add(p.getPokemonName());
            }else if (p.getGeneration() == 6){
                leg6.add(p.getPokemonName());
            }else if (p.getGeneration() == 7){
                leg7.add(p.getPokemonName());
            }else if (p.getGeneration() ==8){
                leg8.add(p.getPokemonName());
            }
        }
        /*EnumSpecies.legendaries.forEach(s -> {
            EnumSpecies p = EnumSpecies.getFromNameAnyCaseNoTranslate(s);
            if (p != null){
                if (p.getGeneration() == 1){
                    leg1.add(p.getPokemonName());
                }else if (p.getGeneration() == 2){
                    leg2.add(p.getPokemonName());
                }else if (p.getGeneration() == 3){
                    leg3.add(p.getPokemonName());
                }else if (p.getGeneration() == 4){
                    leg4.add(p.getPokemonName());
                }else if (p.getGeneration() == 5){
                    leg5.add(p.getPokemonName());
                }else if (p.getGeneration() == 6){
                    leg6.add(p.getPokemonName());
                }else if (p.getGeneration() == 7){
                    leg7.add(p.getPokemonName());
                }else if (p.getGeneration() ==8){
                    leg8.add(p.getPokemonName());
                }
            }
        });*/
    }

}
