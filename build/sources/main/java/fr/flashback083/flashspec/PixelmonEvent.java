package fr.flashback083.flashspec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.DeleteType;
import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.moveskills.UseMoveSkillEvent;
import com.pixelmonmod.pixelmon.api.events.pokemon.SetNicknameEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.PixelmonSpawnerEvent;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import fr.flashback083.flashspec.Specs.UndeleteSpec.TrashListener;
import fr.pokepixel.itemsaver.GsonMethods;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.LocalTime;
import java.util.*;

import static fr.flashback083.flashspec.TimerApi.removeExpiredTasks;
import static fr.flashback083.flashspec.TimerApi.tasks;
import static fr.flashback083.flashspec.config.ChatColor.translateAlternateColorCodes;
import static fr.flashback083.flashspec.config.Functions.getOrder;
import static fr.flashback083.flashspec.config.Functions.getRemainingEVs;
import static fr.flashback083.flashspec.config.GsonUtils.getSpecforKey;


public class PixelmonEvent {

    //Aggro spec
    private static final HashMap<String,Integer> playertime = new HashMap<>();
    ConfigCategory lang = FlashSpec.lang.getCategory("lang");
    private static final List<UUID> noexp = Lists.newArrayList();

    //EVS spec
    //private static final HashMap<UUID,Integer> evhp = new HashMap<>();

    //Nickname spec
    @SubscribeEvent
    public void onEvo(EvolveEvent.PostEvolve evolveEvent) {
        String prevName = evolveEvent.preEvo.getNickname();
        if (!evolveEvent.preEvo.getName().equalsIgnoreCase(prevName)){
            EnumSpecies preSpecies = evolveEvent.preEvo.getSpecies();
            evolveEvent.pokemon.getPokemonData().setNickname(prevName.replace(preSpecies.name, evolveEvent.pokemon.getSpecies().name));
        }
    }


    //Uncatchable & Unbattleable spec
    /*@SubscribeEvent
    public void onPixelmonUpdateEvent(PixelmonUpdateEvent event){
        if (event.phase.equals(TickEvent.Phase.START)) {
            EntityPixelmon pixelmon = event.pokemon;
            if (pixelmon.hasOwner()) return;
            String nickname = event.pokemon.getNickname();
            if (pixelmon.getPokemonData().hasSpecFlag("uncatchable")){
                if (!event.pokemon.getPokemonData().getBonusStats().preventsCapture()){
                    event.pokemon.getPokemonData().getBonusStats().setPreventsCapture(true);
                }
                if (event.pokemon.getPokemonData().getNickname() == null){
                    event.pokemon.getPokemonData().setNickname(stripColor(lang.get("uncatchabletag").getString().replace("%pokemonname%",pixelmon.getLocalizedName()).replaceAll("%nickname%",nickname)));
                }
            }
            if (pixelmon.getPokemonData().hasSpecFlag("unbattleable")){
                if (event.pokemon.getPokemonData().getNickname() == null){
                    event.pokemon.getPokemonData().setNickname(stripColor(lang.get("unbattleabletag").getString().replace("%pokemonname%",pixelmon.getLocalizedName()).replaceAll("%nickname%",nickname)));
                }
            }
        }
    }*/

    //Uncatchable spec
    @SubscribeEvent
    public void onCapture(CaptureEvent.StartCapture event){
        if (event.getPokemon().getPokemonData().hasSpecFlag("uncatchable")){
            event.getPokemon().getPokemonData().getBonusStats().setPreventsCapture(true);
            /*if (event.getPokemon().getPokemonData().getNickname() == null){
                String nickname = event.getPokemon().getNickname();
                event.getPokemon().getPokemonData().setNickname(stripColor(lang.get("uncatchabletag").getString().replace("%pokemonname%",event.getPokemon().getLocalizedName()).replaceAll("%nickname%",nickname)));
            }*/
            event.setCanceled(true);
        }
    }

    //Unbattleable spec
    @SubscribeEvent
    public void onCaptureUnBattleAble(CaptureEvent.SuccessfulCapture event){
        if (event.getPokemon().getPokemonData().hasSpecFlag("unbattleable")){
            event.getPokemon().getPokemonData().removeSpecFlag("unbattleable");
            event.getPokemon().getPokemonData().setNickname(event.getPokemon().getLocalizedName());
        }
        if (event.getPokemon().getPokemonData().hasSpecFlag("aggro")){
            event.getPokemon().getPokemonData().removeSpecFlag("aggro");
        }
    }


    //Unbattleable spec
    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        if(event.participant2[0].getType().equals(ParticipantType.WildPokemon)){
            EntityPixelmon pokemon = (EntityPixelmon) event.participant2[0].getEntity();
            if (pokemon.getPokemonData().hasSpecFlag("unbattleable")){
                event.setCanceled(true);
                if (pokemon.getPokemonData().getNickname() == null){
                    String nickname = pokemon.getNickname();
                    pokemon.getPokemonData().setNickname(translateAlternateColorCodes('&',lang.get("unbattleabletag").getString().replace("%pokemonname%",pokemon.getLocalizedName()).replaceAll("%nickname%",nickname)));
                }
                if (event.participant1[0].getType().equals(ParticipantType.Player)){
                    EntityPlayerMP player = (EntityPlayerMP) event.participant1[0].getEntity();
                    player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unbattleablemsg").getString().replace("%pokemonname%",pokemon.getPokemonName()))));
                }
            }
        }

    }


    //Evolve spec
    @SubscribeEvent
    public void onEvolve(EvolveEvent.PreEvolve event){
        if (event.preEvo.getPokemonData().hasSpecFlag("unevo")){
            event.setCanceled(true);
            event.player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unevolvemsg").getString().replace("%pokemonname%",event.preEvo.getPokemonName()))));
        }
    }

    //Level spec
    @SubscribeEvent
    public void onXP(ExperienceGainEvent event){
        if (event.pokemon.getPokemon().hasSpecFlag("unlevel")){
            event.setCanceled(true);
            // && !event.pokemon.getPlayerOwner().capabilities.isCreativeMode) event.pokemon.getPlayerOwner().addItemStackToInventory(new ItemStack(PixelmonItems.rareCandy));
            if (!event.getType().equals(ExperienceGainType.RARE_CANDY)) event.pokemon.getPlayerOwner().sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unlevelmsg").getString().replace("%pokemonname%",event.pokemon.getRealNickname()))));
        }
    }
    
    //Level spec
    @SubscribeEvent
    public void onEntityRightClick(PlayerInteractEvent.EntityInteract event){
        if (event.getTarget() instanceof EntityPixelmon && event.getEntityPlayer().inventory.getCurrentItem().getItem().equals(PixelmonItems.rareCandy) && event.getHand().equals(EnumHand.MAIN_HAND)){
            EntityPixelmon pixelmon = (EntityPixelmon) event.getTarget();
            if (pixelmon.getPokemonData().hasSpecFlag("unlevel")) {
                event.setCanceled(true);
                event.getEntityPlayer().sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unlevelmsg").getString().replace("%pokemonname%",pixelmon.getPokemonName()))));
            }
        }
    }


    //AGGRO spec
    @SubscribeEvent
    public void onPixelmonUpdateEvent2(PixelmonUpdateEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            //Thread offthread = new Thread(() -> {
                EntityPixelmon pixelmon = event.pokemon;
                if (!pixelmon.getPokemonData().hasSpecFlag("aggro")) return;
                if (pixelmon.battleController != null) return;
                EntityPlayerMP player = (EntityPlayerMP) pixelmon.getEntityWorld().getClosestPlayerToEntity(pixelmon, 5.0);
                if (player == null) return;
                if (BattleRegistry.getBattle(player) != null) return;
                PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
                if (party.countAblePokemon() == 0) return;
                if (playertime.containsKey(player.getName())) return;
                PlayerParticipant p1 = new PlayerParticipant(player, party.getAndSendOutFirstAblePokemon(player));
                WildPixelmonParticipant p2 = new WildPixelmonParticipant(pixelmon);
                BattleRegistry.startBattle(p1, p2);
            //});
            //offthread.start();
        }
    }

    //Aggro spec
    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event){
         if (event.bc.containsParticipantType(WildPixelmonParticipant.class) && event.bc.containsParticipantType(PlayerParticipant.class)){
             BattleParticipant playerParticipant = event.bc.getParticipantForEntity(event.getPlayers().get(0));
             EntityPixelmon wildpokemon = (EntityPixelmon) event.bc.otherParticipant(playerParticipant).getEntity();
             EntityPlayerMP player = (EntityPlayerMP) playerParticipant.getEntity();
             if (wildpokemon != null && wildpokemon.getPokemonData().hasSpecFlag("aggro")){
                 playertime.putIfAbsent(player.getName(),100);
             }
         }
    }

    //Aggro spec
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase.equals(TickEvent.ServerTickEvent.Phase.START)){
            if (!playertime.isEmpty()) {
                List<String> keys = new ArrayList<>(playertime.keySet());
                for (String key : keys) {
                    int value = playertime.get(key);
                    if (value == 1) {
                        playertime.remove(key);
                    } else {
                        playertime.put(key, value - 1);
                    }
                }
            }
        }else{
            for(Task task : new ArrayList<>(tasks)){
                task.tick();
            }
            removeExpiredTasks();
        }
    }


    //Untrashable & Undeleteable spec
    @SubscribeEvent
    public void onTrash(PixelmonDeletedEvent event) {
        if (event.pokemon.hasSpecFlag("undeleteable") || (event.deleteType == DeleteType.PC && event.pokemon.hasSpecFlag("untrashable"))) {
            event.player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("undeleteablemsg").getString().replace("%pokemonname%",event.pokemon.getSpecies().getPokemonName()))));
            new TrashListener.UnTrashable(event.pokemon);
        }
    }

    //undropable
    @SubscribeEvent
    public void onUndrop(DropEvent event){
        if (event.isPokemon()){
            EntityPixelmon pokemon = (EntityPixelmon) event.entity;
            if (pokemon.getPokemonData().hasSpecFlag("undropable")){
                event.setCanceled(true);
            }
        }
    }


    //Dropitem + dropchance + dropis
    @SubscribeEvent
    public void onDropItem(DropEvent event){
        if (event.isPokemon()){
            EntityPixelmon pokemon = (EntityPixelmon) event.entity;
            if (pokemon.getEntityData().hasKey("custom-drop")){
                if (pokemon.getEntityData().hasKey("drop-chance")){
                    int chance = Integer.parseInt(pokemon.getEntityData().getString("drop-chance"));
                    Random r = new Random();
                    int choose = r.nextInt((100 - 1) + 1) + 1;
                    if (choose <= chance){
                        ItemStack item = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(pokemon.getEntityData().getString("custom-drop"))));
                        event.addDrop(item);
                    }
                }else {
                    ItemStack item = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(pokemon.getEntityData().getString("custom-drop"))));
                    event.addDrop(item);
                }
            }
            if (pokemon.getEntityData().hasKey("drop-itemsaver")){
                String key = pokemon.getEntityData().getString("drop-itemsaver");
                if (pokemon.getEntityData().hasKey("drop-chance")){
                    int chance = Integer.parseInt(pokemon.getEntityData().getString("drop-chance"));
                    Random r = new Random();
                    int choose = r.nextInt((100 - 1) + 1) + 1;
                    if (choose <= chance){
                        ArrayList<ItemStack> items;
                        items = GsonMethods.getItems(key);
                        items.forEach(event::addDrop);
                    }
                }else {
                    ArrayList<ItemStack> items;
                    items = GsonMethods.getItems(key);
                    items.forEach(event::addDrop);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDropItem2(BeatWildPixelmonEvent event){
        EntityPixelmon pixelmon = (EntityPixelmon) event.wpp.getEntity();
        try {
            if (DropItemRegistry.getDropsForPokemon(pixelmon).size() > 0) return;
            if (pixelmon.getEntityData().hasKey("custom-drop") || pixelmon.getEntityData().hasKey("drop-itemsaver")){
                DropItemQueryList.register(pixelmon,Lists.newArrayList(),event.player);
            }
        }catch (NullPointerException ignored){

        }

    }


    //Mega evolve spec
    @SubscribeEvent
    public void onMegaEvolve(UseMoveSkillEvent event){
        if (event.pixelmon.getPokemonData().hasSpecFlag("unmegaout") && event.moveSkill.name.equalsIgnoreCase("pixelmon.moveskill.mega_evolve.name")){
            event.pixelmon.getOwner().sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unmegaoutmsg").getString().replace("%pokemonname%",event.pixelmon.getPokemonName()))));
            event.setCanceled(true);
        }
    }

    //Unnickable
    @SubscribeEvent
    public void onNick(SetNicknameEvent event){
        if (event.pokemon.hasSpecFlag("unnickable")){
            event.player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unnickablemsg").getString().replace("%pokemonname%",event.pokemon.getSpecies().getPokemonName()))));
            event.setCanceled(true);
        }
    }

    //Fix intrasec spec for leg & ub
    @SubscribeEvent
    public void onHatch(EggHatchEvent event){
        event.pokemon.setLevel(1);
    }



    @SubscribeEvent
    public void onCommand(CommandEvent event){
        if (!(event.getSender() instanceof EntityPlayerMP)) return;
        if (event.getCommand().getName().equalsIgnoreCase("hatch")){
            if (event.getParameters().length == 1 && isNumeric(event.getParameters()[0])){
                int slot = Integer.parseInt(event.getParameters()[0]) - 1;
                EntityPlayerMP player = (EntityPlayerMP) event.getSender();
                PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player);
                if (storage.get(slot) != null && storage.get(slot).hasSpecFlag("unhatchable")){
                    event.setCanceled(true);
                    player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("unhatchablemsg").getString().replace("%pokemonname%",storage.get(slot).getSpecies().getPokemonName()))));
                }
            }
        }
    }


    //Timer spec
    @SubscribeEvent
    public void onSpawnerBlock(PixelmonSpawnerEvent event){
        List<String> spec = Lists.newArrayList(event.spec.args);
        if (spec.contains("tday") || spec.contains("tnight")  || spec.contains("tdusk")  || spec.contains("tdawn")){
            long age = event.spawner.getWorld().getWorldTime() % 24000;
            if (!canSpawnMCWeather(spec,age)){
                event.setCanceled(true);
            }
        }
        if (spec.contains("rain") || spec.contains("thunder")  || spec.contains("clear")){
            if (!canSpawnWeather(spec,event.spawner.getWorld().getWorldInfo())){
                event.setCanceled(true);
            }
        }
        spec.forEach(s -> {
            if (s.startsWith("realtime:")){
               String cut = s.replaceFirst("realtime:","");
                LocalTime time = LocalTime.now();
                ArrayList<String> listspawn = Lists.newArrayList(cut.split("/"));
                if (!canSpawnRealtime(listspawn,time)){
                    event.setCanceled(true);
                }
            }
        });
    }

    private boolean canSpawnMCWeather(List<String> spec,long agemodulo){
        if (spec.contains("tday") && agemodulo < 12000){
            return true;
        }else if (spec.contains("tnight") && agemodulo >= 13000 && agemodulo <=23000){
            return true;
        }else if (spec.contains("tdusk") && agemodulo >= 12000 && agemodulo < 13000 ){
            return true;
        }else return spec.contains("tdawn") && agemodulo > 23000;
    }


    private boolean canSpawnWeather(List<String> spec, WorldInfo worldInfo){
        if (spec.contains("rain") && worldInfo.isRaining()){
            return true;
        }else if (spec.contains("thunder") && worldInfo.isThundering()){
            return true;
        }else return spec.contains("clear") && !worldInfo.isThundering() && !worldInfo.isRaining();
    }

    private boolean canSpawnRealtime(ArrayList<String> listspawn, LocalTime time){
        //System.out.println("[FlashSpec] " + time.getHour());
        for (String s1 : listspawn) {
            if (s1.contains("-")) {
                String[] timecut = s1.split("-");
                int heure1 = Integer.parseInt(timecut[0]);
                int heure2 = Integer.parseInt(timecut[1]);
                if (time.getHour() >= heure1 && time.getHour() <= heure2) {
                    return true;
                }
            } else {
                if (time.getHour() == Integer.parseInt(s1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
           Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    //TranformCatch
    @SubscribeEvent
    public void onCapture1(CaptureEvent.SuccessfulCapture event){
        if (event.getPokemon().getPokemonData().getPersistentData().hasKey("onCatch")){
            String oldname = event.getPokemon().getPokemonName();
            NBTTagCompound data = event.getPokemon().getPokemonData().getPersistentData();
            String newname = data.getString("onCatch");
            if (newname.equalsIgnoreCase("random")) {
                newname = EnumSpecies.randomPoke().getPokemonName();
            }
            if (EnumSpecies.getFromName(newname).isPresent()){
                newname = EnumSpecies.getFromName(newname).get().getPokemonName();
            }else {
                if (getSpecforKey(newname).length()>0){
                    newname = getSpecforKey(newname);
                }else {
                    return;
                }
            }
            if (data.hasKey("transformChance")){
                Random r = new Random();
                int chancee = 1 + r.nextInt(100);
                //player.sendMessage(new TextComponentString("Test 3 " + chance));
                if (chancee <= Integer.parseInt(event.getPokemon().getPokemonData().getPersistentData().getString("transformChance"))){
                    changePokemon(event,oldname,newname);
                }
            }else {
                changePokemon(event,oldname,newname);
            }
        }
    }

    private void changePokemon(CaptureEvent.SuccessfulCapture event, String oldname, String newname){
        PokemonSpec spec = new PokemonSpec(newname);
        if (spec.name != null){
            event.setPokemon(Pixelmon.pokemonFactory.create(EnumSpecies.getFromName(spec.name).get()).getOrSpawnPixelmon(event.getPokemon()));
        }else {
            event.setPokemon(Pixelmon.pokemonFactory.create(EnumSpecies.randomPoke()).getOrSpawnPixelmon(event.getPokemon()));
        }
        spec.apply(event.getPokemon());
        event.player.sendMessage(new TextComponentString(translateAlternateColorCodes('&',lang.get("transformmessage").getString().replace("%previouspoke%",oldname).replace("%newpokemon%",event.getPokemon().getPokemonName()))));
        //event.player.sendMessage(new TextComponentString("§f[§dPixelmon§f]§7 Oh? "+oldname+" was actually a §a"+event.getPokemon().getPokemonName()+"§7 in disguise!!!"));
    }



    //Unpokedex
    @SubscribeEvent
    public void pokedexEvent(PokedexEvent event){
        if (event.pokemon.hasSpecFlag("unpokedexable")){
            event.setCanceled(true);
            Pixelmon.storageManager.getParty(event.uuid).pokedex.set(event.pokemon,EnumPokedexRegisterStatus.unknown);
        }
    }

    @SubscribeEvent
    public void onGainXP(ExperienceGainEvent event){
        if (noexp.contains(event.pokemon.getPlayerOwner().getUniqueID())){
            //System.out.println("CANCEL EVENT");
            noexp.remove(event.pokemon.getPlayerOwner().getUniqueID());
            //event.setCanceled(true);
            event.setExperience(0);
        }
    }


    @SubscribeEvent
    public void endBattle(BeatWildPixelmonEvent event){
        EntityPixelmon entity = (EntityPixelmon) event.wpp.getEntity();
        if (entity.getPokemonData().hasSpecFlag("unexpable")){
            noexp.add(event.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public void onSpawnEvent(SpawnEvent event){
       final Entity entity = event.action.getOrCreateEntity();
       if (entity instanceof EntityPixelmon){
           EntityPixelmon pix = (EntityPixelmon) entity;
           if (pix.getEntityData().hasKey("realtime")){
               String cut = pix.getEntityData().getString("realtime");
               LocalTime time = LocalTime.now();
               ArrayList<String> listspawn = Lists.newArrayList(cut.split("/"));
               if (!canSpawnRealtime(listspawn,time)){
                   event.setCanceled(true);
               }
           }
           /*if (pix.getPokemonData().hasSpecFlag("uncatchable")){
               if (!pix.getPokemonData().getBonusStats().preventsCapture()){
                   pix.getPokemonData().getBonusStats().setPreventsCapture(true);
               }
               if (pix.getPokemonData().getNickname() == null){
                   String nickname = pix.getNickname();
                   pix.getPokemonData().setNickname(stripColor(lang.get("uncatchabletag").getString().replace("%pokemonname%",pix.getLocalizedName()).replaceAll("%nickname%",nickname)));
               }
           }
           if (pix.getPokemonData().hasSpecFlag("unbattleable")){
               if (pix.getPokemonData().getNickname() == null){
                   String nickname = pix.getNickname();
                   pix.getPokemonData().setNickname(stripColor(lang.get("unbattleabletag").getString().replace("%pokemonname%",pix.getLocalizedName()).replaceAll("%nickname%",nickname)));
               }
           }*/
       }
    }

    //GiveEV event
    @SubscribeEvent
    public void onBeatWildPixelmon(BeatWildPixelmonEvent event){
        EntityPixelmon entpix = (EntityPixelmon) event.wpp.getEntity();
        Map<String, Integer> order = getOrder(entpix.getPokemonData().getPersistentData());
        order.forEach((key, gainhp) -> {
            if (key.equalsIgnoreCase("gevhp")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                //int evY = pix.getBaseStats().evYields.get(StatsType.HP);
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.HP);
                    attacker.getStats().evs.setStat(StatsType.HP, total);
                }
            }
            if (key.equalsIgnoreCase("gevatk")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.Attack);
                    attacker.getStats().evs.setStat(StatsType.Attack, total);
                }
            }
            if (key.equalsIgnoreCase("gevdef")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.Defence);
                    attacker.getStats().evs.setStat(StatsType.Defence, total);
                }
            }
            if (key.equalsIgnoreCase("gevspeatk")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.SpecialAttack);
                    attacker.getStats().evs.setStat(StatsType.SpecialAttack, total);
                }
            }
            if (key.equalsIgnoreCase("gevspedef")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.SpecialDefence);
                    attacker.getStats().evs.setStat(StatsType.SpecialDefence, total);
                }
            }
            if (key.equalsIgnoreCase("gevspeed")){
                //int gainhp = value;
                PixelmonWrapper pix = event.wpp.getFaintedPokemon();
                for (PixelmonWrapper attacker : pix.getAttackers()) {
                    int remain = getRemainingEVs(attacker.getStats().evs.getArray());
                    if (gainhp>remain){
                        gainhp = remain;
                    }
                    int total = gainhp + attacker.getStats().evs.getStat(StatsType.Speed);
                    attacker.getStats().evs.setStat(StatsType.Speed, total);
                }
            }
        });
    }

    //Fdate spec
    @SubscribeEvent
    public void onCaptureDate(CaptureEvent.SuccessfulCapture event){
        if (event.getPokemon().getPokemonData().getPersistentData().hasKey("fdate")){
            Calendar calendar = Calendar.getInstance();
            event.getPokemon().getPokemonData().getPersistentData().setLong("fdate",calendar.getTimeInMillis());
        }
    }

    //Despawnable spec
    @SubscribeEvent
    public void onBattleEndDespawn(BattleEndEvent event){
        for (BattleParticipant participant : event.bc.participants) {
            if (participant instanceof WildPixelmonParticipant){
                if (participant.getEntity() != null){
                    EntityPixelmon pixelmon = (EntityPixelmon) participant.getEntity();
                    if (pixelmon.getPokemonData().hasSpecFlag("despawnable")){
                        pixelmon.func_70106_y();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSpawnEvent(EntityJoinWorldEvent event){
        if (event.getEntity() instanceof EntityPixelmon){
            EntityPixelmon pixelmon = (EntityPixelmon) event.getEntity();
            if (pixelmon.hasOwner()) return;
            String nickname = pixelmon.getNickname();
            if (pixelmon.getPokemonData().hasSpecFlag("uncatchable")){
                if (!pixelmon.getPokemonData().getBonusStats().preventsCapture()){
                    pixelmon.getPokemonData().getBonusStats().setPreventsCapture(true);
                }
                Task.builder().execute(() -> pixelmon.getPokemonData().setNickname(translateAlternateColorCodes('&',lang.get("uncatchabletag").getString().replace("%pokemonname%",pixelmon.getLocalizedName()).replaceAll("%nickname%",nickname)))).delay(1).build();
            }
            if (pixelmon.getPokemonData().hasSpecFlag("unbattleable")){
                Task.builder().execute(() -> pixelmon.getPokemonData().setNickname(translateAlternateColorCodes('&',lang.get("unbattleabletag").getString().replace("%pokemonname%",pixelmon.getLocalizedName()).replaceAll("%nickname%",nickname)))).delay(1).build();
            }
        }
    }


}
