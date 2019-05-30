package fr.flashback083.flashspec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import fr.flashback083.flashspec.MoveSpec.Move1Spec;
import fr.flashback083.flashspec.MoveSpec.Move2Spec;
import fr.flashback083.flashspec.MoveSpec.Move3Spec;
import fr.flashback083.flashspec.MoveSpec.Move4Spec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.FileNotFoundException;
import java.util.Scanner;

@Mod(
        modid = Flashspec.MOD_ID,
        name = Flashspec.MOD_NAME,
        version = Flashspec.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class Flashspec {

    public static final String MOD_ID = "flashspec";
    public static final String MOD_NAME = "FlashSpec";
    public static final String VERSION = "1.2";

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static Flashspec INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) throws FileNotFoundException {
        Scanner scanner = new Scanner(event.getServer().getFile("/logs/latest.log"));
        boolean result = false;
        while(scanner.hasNextLine() && !result) {
            result = scanner.nextLine().contains("PixelmonGenerations");
        }
        scanner.close();
        if (!result) {
            System.out.println("FlashSpec loaded !");
            PokemonSpec.extraSpecTypes.add(new Move1Spec(Lists.newArrayList("move1","m1"), null));
            PokemonSpec.extraSpecTypes.add(new Move2Spec(Lists.newArrayList("move2","m2"), null));
            PokemonSpec.extraSpecTypes.add(new Move3Spec(Lists.newArrayList("move3","m3"), null));
            PokemonSpec.extraSpecTypes.add(new Move4Spec(Lists.newArrayList("move4","m4"), null));
            PokemonSpec.extraSpecTypes.add(new HelpItemSpec(Lists.newArrayList("helditem","hi"), null));
        }

    }


}
