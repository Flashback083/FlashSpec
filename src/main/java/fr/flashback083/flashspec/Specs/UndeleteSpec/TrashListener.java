package fr.flashback083.flashspec.Specs.UndeleteSpec;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TrashListener {

        public static class UnTrashable {
            StoragePosition pos;
            Pokemon poke;
            PokemonStorage storage;

            public UnTrashable(Pokemon poke) {
                this.poke = poke;
                this.pos = poke.getPosition();
                this.storage = poke.getStorage();
                MinecraftForge.EVENT_BUS.register(this);
            }

            @SubscribeEvent
            public void onTick(TickEvent.ServerTickEvent event) {
                if (event.phase != TickEvent.Phase.START) return;
                MinecraftForge.EVENT_BUS.unregister(this);
                storage.set(pos, poke);
            }
        }
}
