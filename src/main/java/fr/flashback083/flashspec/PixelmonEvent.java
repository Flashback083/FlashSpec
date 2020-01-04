package fr.flashback083.flashspec;

import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.ParticipantType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static fr.flashback083.flashspec.FlashSpec.*;

public class PixelmonEvent {

    //Nickname spec
    @SubscribeEvent
    public void onEvo(EvolveEvent.PostEvolve evolveEvent) {
        String prevName = evolveEvent.preEvo.getNickname();
        EnumSpecies preSpecies = evolveEvent.preEvo.getSpecies();
        evolveEvent.pokemon.getPokemonData().setNickname(prevName.replace(preSpecies.name, evolveEvent.pokemon.getSpecies().name));
    }


    /*//Uncatchable spec
    @SubscribeEvent
    public void onPokemonCapture(CaptureEvent.StartCapture event) {
        if (UNCATCHABLE.matches(event.getPokemon())) {
            event.player.sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon is uncatchable!"));
            event.player.inventory.addItemStackToInventory(new ItemStack(event.pokeball.getType().getItem()));
            event.setCanceled(true);
        }
    }*/

    //Uncatchable spec
    @SubscribeEvent
    public void onEntityCreated(PixelmonUpdateEvent event){
        if (event.phase.equals(TickEvent.Phase.START)) {
            EntityPixelmon pixelmon = event.pokemon;
            if (UNCATCHABLE.matches(pixelmon)){
                event.pokemon.getPokemonData().setNickname(pixelmon.getPokemonName() + TextFormatting.RED + " (Uncatchable)");
            }
            if (UNBATTLEABLE.matches(pixelmon)){
                event.pokemon.getPokemonData().setNickname(pixelmon.getName() + TextFormatting.RED + " (Unbattleable)");
            }
        }
    }


    //Unbattleable spec
    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(event.participant2[0].getType().equals(ParticipantType.WildPokemon)){
            EntityPixelmon pokemon = (EntityPixelmon) event.participant2[0].getEntity();
            if (UNBATTLEABLE.matches(pokemon)){
                event.setCanceled(true);
                if (event.participant1[0].getType().equals(ParticipantType.Player)){
                    EntityPlayerMP player = (EntityPlayerMP) event.participant1[0].getEntity();
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon is unbattleable!"));
                }
            }
        }

    }


    //Evolve spec
    @SubscribeEvent
    public void onEvolve(EvolveEvent.PreEvolve event){
        if (UNEVO.matches(event.preEvo.getPokemonData())){
            event.setCanceled(true);
            event.player.sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon can't evolve!"));
        }
    }

    //Level spec
    @SubscribeEvent
    public void onXP(ExperienceGainEvent event){
        if (UNLEVEL.matches(event.pokemon.getPokemon())){
            event.setCanceled(true);
            event.pokemon.getPlayerOwner().sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon can't level up or gain XP!"));
        }
    }



}
