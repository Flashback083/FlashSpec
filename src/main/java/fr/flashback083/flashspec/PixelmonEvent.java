package fr.flashback083.flashspec;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.DeleteType;
import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import fr.flashback083.flashspec.Specs.UndeleteSpec.TrashListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;


public class PixelmonEvent {

    //Aggro spec
    private static HashMap<String,Integer> playertime = new HashMap<>();


    //Nickname spec
    @SubscribeEvent
    public void onEvo(EvolveEvent.PostEvolve evolveEvent) {
        String prevName = evolveEvent.preEvo.getNickname();
        EnumSpecies preSpecies = evolveEvent.preEvo.getSpecies();
        evolveEvent.pokemon.getPokemonData().setNickname(prevName.replace(preSpecies.name, evolveEvent.pokemon.getSpecies().name));
    }


    //Uncatchable & Unbattleable spec
    @SubscribeEvent
    public void onPixelmonUpdateEvent(PixelmonUpdateEvent event){
        if (event.phase.equals(TickEvent.Phase.START)) {
            EntityPixelmon pixelmon = event.pokemon;
            if (pixelmon.getPokemonData().hasSpecFlag("uncatchable")){
                event.pokemon.getPokemonData().setNickname(pixelmon.getPokemonName() + TextFormatting.RED + " (Uncatchable)");
            }
            if (pixelmon.getPokemonData().hasSpecFlag("unbattleable")){
                event.pokemon.getPokemonData().setNickname(pixelmon.getName() + TextFormatting.RED + " (Unbattleable)");
            }
        }
    }


    //Unbattleable spec
    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(event.participant2[0].getType().equals(ParticipantType.WildPokemon)){
            EntityPixelmon pokemon = (EntityPixelmon) event.participant2[0].getEntity();
            if (pokemon.getPokemonData().hasSpecFlag("unbattleable")){
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
        if (event.preEvo.getPokemonData().hasSpecFlag("unevo")){
            event.setCanceled(true);
            event.player.sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon can't evolve!"));
        }
    }

    //Level spec
    @SubscribeEvent
    public void onXP(ExperienceGainEvent event){
        if (event.pokemon.getPokemon().hasSpecFlag("unlevel")){
            event.setCanceled(true);
            // && !event.pokemon.getPlayerOwner().capabilities.isCreativeMode) event.pokemon.getPlayerOwner().addItemStackToInventory(new ItemStack(PixelmonItems.rareCandy));
            if (!event.getType().equals(ExperienceGainType.RARE_CANDY)) event.pokemon.getPlayerOwner().sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon can't level up or gain XP!"));
        }
    }
    
    //Level spec
    @SubscribeEvent
    public void onEntityRightClick(PlayerInteractEvent.EntityInteract event){
        if (event.getTarget() instanceof EntityPixelmon && event.getEntityPlayer().inventory.getCurrentItem().getItem().equals(PixelmonItems.rareCandy) && event.getHand().equals(EnumHand.MAIN_HAND)){
            EntityPixelmon pixelmon = (EntityPixelmon) event.getTarget();
            if (pixelmon.getPokemonData().hasSpecFlag("unlevel")) {
                event.setCanceled(true);
                event.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED + "This Pokemon can't level up or gain XP!"));
            }
        }
    }


    //AGGRO spec
    @SubscribeEvent
    public void onPixelmonUpdateEvent2(PixelmonUpdateEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            EntityPixelmon pixelmon = event.pokemon;
            if (!pixelmon.getPokemonData().hasSpecFlag("aggro")) return;
            if (pixelmon.battleController != null) return;
            if (pixelmon.getEntityWorld().getClosestPlayerToEntity(pixelmon, 5.0) == null) return;
            EntityPlayerMP player = (EntityPlayerMP) pixelmon.getEntityWorld().getClosestPlayerToEntity(pixelmon, 5.0);
            if (BattleRegistry.getBattle(player) != null) return;
            if (Pixelmon.storageManager.getParty(player).countAblePokemon() == 0) return;
            if (playertime.containsKey(player.getName())) return;
            PlayerParticipant p1 = new PlayerParticipant(player, Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player));
            WildPixelmonParticipant p2 = new WildPixelmonParticipant(pixelmon);
            BattleRegistry.startBattle(p1, p2);
        }
    }

    //Aggro spec
    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
         if (event.bc.containsParticipantType(WildPixelmonParticipant.class) && event.bc.containsParticipantType(PlayerParticipant.class)){
             BattleParticipant playerParticipant = event.bc.getParticipantForEntity(event.getPlayers().get(0));
             EntityPixelmon wildpokemon = (EntityPixelmon) event.bc.otherParticipant(playerParticipant).getEntity();
             EntityPlayerMP player = (EntityPlayerMP) playerParticipant.getEntity();
             if (wildpokemon.getPokemonData().hasSpecFlag("aggro")){
                 playertime.putIfAbsent(player.getName(),100);
             }
         }
    }


    //Aggro spec
    @SubscribeEvent
    public void onCapture(CaptureEvent.SuccessfulCapture event){
        if (event.getPokemon().getPokemonData().hasSpecFlag("aggro")){
            event.getPokemon().getPokemonData().removeSpecFlag("aggro");
        }
    }

    //Aggro spec
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase.equals(TickEvent.ServerTickEvent.Phase.START)){
            if (!playertime.isEmpty()) {

                for (Iterator<String> keys = playertime.keySet().iterator(); keys.hasNext();) {
                    String key = keys.next();
                    int value = playertime.get(key);
                    if (value == 1){
                        playertime.remove(key);
                    }else {
                        int cooldown = value - 1;
                        playertime.replace(key,cooldown);
                    }
                }

                /*playertime.entrySet().iterator().forEachRemaining(entrySet -> {
                    String key = entrySet.getKey();
                    Integer value = entrySet.getValue();
                    if (value == 1){
                        playertime.remove(key);
                    }else {
                        int cooldown = value - 1;
                        playertime.replace(key,cooldown);
                    }
                });*/
            }
        }
    }


    //Untrashable & Undeleteable spec
    @SubscribeEvent
    public void onTrash(PixelmonDeletedEvent event) {
            /*if (UNDELETEABLE.matches(event.pokemon) || (event.deleteType == DeleteType.PC && UNTRASHABLE.matches(event.pokemon))) {
                event.player.sendMessage(new TextComponentString(TextFormatting.RED + "This pokemon can't be deleted!"));
                new TrashListener.UnTrashable(event.pokemon);
            }*/

        if (event.pokemon.hasSpecFlag("undeleteable") || (event.deleteType == DeleteType.PC && event.pokemon.hasSpecFlag("untrashable"))) {
            event.player.sendMessage(new TextComponentString(TextFormatting.RED + "This pokemon can't be deleted!"));
            new TrashListener.UnTrashable(event.pokemon);
        }
    }

    //Unmega & unmega battle spec
    /*@SubscribeEvent
    public void onMegaEvo(UseMoveSkillEvent event){
        EntityPixelmon p = event.pixelmon;
        if (p.getOwner() instanceof EntityPlayerMP){
            EntityPlayerMP playerowner = (EntityPlayerMP) p.getOwner();
            if (BattleRegistry.getBattle(playerowner) != null){
                if (p.getPokemonData().hasSpecFlag("unmegabattle")){
                    playerowner.sendMessage(new TextComponentString(TextFormatting.RED + "Yu can't Mega evolve this pokemon in battle!"));
                    event.setCanceled(true);
                }
            }else {
                if (p.getPokemonData().hasSpecFlag("unmega")){
                    playerowner.sendMessage(new TextComponentString(TextFormatting.RED + "Yu can't Mega evolve this pokemon!"));
                    event.setCanceled(true);
                }
            }
        }
    }*/

}
