package fr.flashback083.flashspec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.pokemon.SpecFlag;
import fr.flashback083.flashspec.Specs.*;
import fr.flashback083.flashspec.Specs.IvsSpec.MinIVPercent;
import fr.flashback083.flashspec.Specs.IvsSpec.NumIVs;
import fr.flashback083.flashspec.Specs.IvsSpec.NumMaxIVs;
import fr.flashback083.flashspec.Specs.LegUbSpec.LegSpec;
import fr.flashback083.flashspec.Specs.LegUbSpec.LegUbSpec;
import fr.flashback083.flashspec.Specs.LegUbSpec.UbSpec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move1Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move2Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move3Spec;
import fr.flashback083.flashspec.Specs.MoveSpec.Move4Spec;
import fr.flashback083.flashspec.Specs.TypeSpec.Type1Spec;
import fr.flashback083.flashspec.Specs.TypeSpec.Type2Spec;
import fr.flashback083.flashspec.Specs.TypeSpec.TypeSpec;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.FileNotFoundException;

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
    public static final String VERSION = "2.2.9";

    //Boolean spec
    public static PokemonSpec UNCATCHABLE;
    public static PokemonSpec UNBATTLEABLE;
    public static PokemonSpec UNEVO;
    public static PokemonSpec UNLEVEL;

    private static MinecraftServer server;
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
    public void preinit(FMLPreInitializationEvent event) {
        PokemonSpec.extraSpecTypes.add(new Move1Spec(Lists.newArrayList("move1","m1"), null));
        PokemonSpec.extraSpecTypes.add(new Move2Spec(Lists.newArrayList("move2","m2"), null));
        PokemonSpec.extraSpecTypes.add(new Move3Spec(Lists.newArrayList("move3","m3"), null));
        PokemonSpec.extraSpecTypes.add(new Move4Spec(Lists.newArrayList("move4","m4"), null));
        PokemonSpec.extraSpecTypes.add(new HelpItemSpec(Lists.newArrayList("helditem","hi"), null));
        //PokemonSpec.extraSpecTypes.add(new UltrabeastSpec(false));
        PokemonSpec.extraSpecTypes.add(new LegUbSpec(false));
        PokemonSpec.extraSpecTypes.add(new Type1Spec(Lists.newArrayList("type1"), null));
        PokemonSpec.extraSpecTypes.add(new TypeSpec(Lists.newArrayList("type"), null));
        PokemonSpec.extraSpecTypes.add(new Type2Spec(Lists.newArrayList("type2"), null));
        PokemonSpec.extraSpecTypes.add(new AbsSpec(Lists.newArrayList("abs"), null));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unbattleable"));
        PokemonSpec.extraSpecTypes.add(new NumIVs(0));
        PokemonSpec.extraSpecTypes.add(new AISpec(Lists.newArrayList("ai"), null));
        PokemonSpec.extraSpecTypes.add(new SpecialTextureSpec(Lists.newArrayList("sptexture"), null));
        PokemonSpec.extraSpecTypes.add(new LegSpec(false));
        PokemonSpec.extraSpecTypes.add(new UbSpec(false));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unevo"));
        PokemonSpec.extraSpecTypes.add(new SpecFlag("unlevel"));
        PokemonSpec.extraSpecTypes.add(new UncatchableSpec(false));


        //CustomSpec import
        PokemonSpec.extraSpecTypes.add(new TextureSpec(Lists.newArrayList("texture"), null));
        //PokemonSpec.extraSpecTypes.add(new SpecFlag("uncatchable"));
        PokemonSpec.extraSpecTypes.add(new OTSpec(Lists.newArrayList("ot", "originaltrainer"), null));
        PokemonSpec.extraSpecTypes.add(new NicknameSpec(null));
        PokemonSpec.extraSpecTypes.add(new MinIVPercent(0));
        PokemonSpec.extraSpecTypes.add(new NumMaxIVs(0));

        //Variable
        UNCATCHABLE = new PokemonSpec("uncatchable");
        UNBATTLEABLE = new PokemonSpec("unbattleable");
        UNEVO = new PokemonSpec("unevo");
        UNLEVEL = new PokemonSpec("unlevel");
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Pixelmon.EVENT_BUS.register(new PixelmonEvent());
        MinecraftForge.EVENT_BUS.register(new PixelmonEvent());

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) throws FileNotFoundException {
            System.out.println("FlashSpec loaded !");
            server = event.getServer();
    }

    public static EntityPlayerMP getPlayer(String playername){
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
        return server.getPlayerList().getPlayerByUsername(playername);
    }

    public static MinecraftServer getServer(){
        return server;
    }


}
