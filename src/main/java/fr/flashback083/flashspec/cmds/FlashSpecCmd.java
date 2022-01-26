package fr.flashback083.flashspec.cmds;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import fr.flashback083.flashspec.FlashSpec;
import fr.flashback083.flashspec.utils.PokeSpecie;
import fr.flashback083.flashspec.utils.PosInfo;
import fr.flashback083.flashspec.config.Config;
import fr.flashback083.flashspec.config.Lang;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static fr.flashback083.flashspec.FlashSpec.getPokeSpecGroup;
import static fr.flashback083.flashspec.FlashSpec.getPos;
import static fr.flashback083.flashspec.config.GsonUtilsSpecGroup.getSpecGroupList;

public class FlashSpecCmd extends CommandBase implements ICommand
{
	private final List<String> aliases;

	public FlashSpecCmd(){
        aliases = Lists.newArrayList("flashspec");
    }
	
	
	@Override
	@Nonnull
	public String getName() {
		return "flashspec";
	}

	@Override
	@Nonnull
	public String getUsage(@Nonnull ICommandSender sender) {
		return "§c/flashspec reload\n"
                +"§c/flashspec debug\n"
                +"§c/flashspec give <player> <groupname> [specs]\n"
                +"§c/flashspec spawn <groupname> [specs] [-player:name -world:name -x:XX -y:YY -z:ZZ]";
				
	}


	@Override
	@Nonnull
	public List<String> getAliases()
	{
		return aliases;
	}

	@Override
	public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
			Configuration cfglang = FlashSpec.lang;
			Lang.readConfig();
			if (cfglang.hasChanged()) {
				cfglang.save();
			}
			cfglang.load();
			Configuration cfgnormal = FlashSpec.config;
			Config.readConfig();
			if (cfgnormal.hasChanged()) {
				cfgnormal.save();
			}
			cfgnormal.load();
            boolean allowRegionalRegion = cfgnormal.getCategory("General").get("allowRegionalFormSpec").getBoolean();
            getPokeSpecGroup.clear();
            getSpecGroupList().forEach((name, spec) -> {
                List<PokeSpecie> pokemonList = Lists.newArrayList();
                for (EnumSpecies value : EnumSpecies.values()) {
                    if (allowRegionalRegion){
                        for (IEnumForm possibleForm : value.getPossibleForms(false)) {
                            if(possibleForm.isRegionalForm()){
                                Pokemon pokemon = Pixelmon.pokemonFactory.create(value);
                                pokemon.setForm(possibleForm);
                                if (new PokemonSpec(spec).matches(pokemon)){
                                    pokemonList.add(new PokeSpecie(value,pokemon.getForm()));
                                }
                            }
                        }
                    }
                    if (new PokemonSpec(spec).matches(Pixelmon.pokemonFactory.create(value))){
                        pokemonList.add(new PokeSpecie(value,-1));
                    }
                }
                getPokeSpecGroup.put(name,pokemonList);
            });
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"FlashSpec's lang and config reloaded!"));
		}else if (args.length ==1 && args[0].equalsIgnoreCase("debug")){
			PokemonSpec.extraSpecTypes.forEach(iSpecType -> {
				FlashSpec.logger.info("[FlashSpec] List spec " + iSpecType.getKeys().get(0));
				FlashSpec.logger.info("[FlashSpec] List spec class " + iSpecType.getSpecClass().getCanonicalName());
			});
		}else if (args.length >= 3 && args[0].equalsIgnoreCase("give")){
            List<String> arg = Lists.newArrayList(args);
            arg.remove(0);
            String player = arg.get(0);
            arg.remove(0);
            if (!Lists.newArrayList(server.getPlayerList().getOnlinePlayerNames()).contains(player)){
                sender.sendMessage(new TextComponentString("§cPlayer not found"));
                return;
            }
            if (!getPokeSpecGroup.containsKey(arg.get(0))){
                sender.sendMessage(new TextComponentString("§cNo group found with this name!"));
                return;
            }
            List<PokeSpecie> list = getPokeSpecGroup.get(arg.get(0));
            if (list.size()<1){
                sender.sendMessage(new TextComponentString("§cNo pokemon available in this group"));
                return;
            }
            PokeSpecie randomSpecie = CollectionHelper.getRandomElement(list);
            Pokemon randompoke = Pixelmon.pokemonFactory.create(randomSpecie.getSpecies());
            randompoke.setForm(randomSpecie.getForm());
            arg.remove(0);
            new PokemonSpec(String.join(" ", arg)).apply(randompoke);
            /*if (randompoke == null){
                sender.sendMessage(new TextComponentString("§cCan't create the pokemon!"));
                return;
            }*/
            PlayerPartyStorage pps = Pixelmon.storageManager.getParty(server.getPlayerList().getPlayerByUsername(player));
            if (pps.add(randompoke)){
                sender.sendMessage(new TextComponentString("§aPokemon given!"));
            }else{
                sender.sendMessage(new TextComponentString("§cError when give the pokemon..."));
            }
        }else if (args.length >= 2 && args[0].equalsIgnoreCase("spawn")){
            //flashspec spawn <groupname> [specs] [-player:name -world:name -x:XX -y:YY -z:ZZ]";
            List<String> arg = Lists.newArrayList(args);
            arg.remove(0);
            if (!getPokeSpecGroup.containsKey(arg.get(0))){
                sender.sendMessage(new TextComponentString("§cNo group found with this name!"));
                return;
            }
            List<PokeSpecie> list = getPokeSpecGroup.get(arg.get(0));
            if (list.size()<1){
                sender.sendMessage(new TextComponentString("§cNo pokemon available in this group"));
                return;
            }
            PokeSpecie randomSpecie = CollectionHelper.getRandomElement(list);
            Pokemon randompoke = Pixelmon.pokemonFactory.create(randomSpecie.getSpecies());
            randompoke.setForm(randomSpecie.getForm());
            arg.remove(0);
            new PokemonSpec(String.join(" ", arg)).apply(randompoke);
            /*if (randompoke == null){
                sender.sendMessage(new TextComponentString("§cCan't create the pokemon!"));
                return;
            }*/
            PosInfo pos = getPos(sender,server,arg);
            randompoke.getOrSpawnPixelmon(pos.getWorld(),pos.getPosX(), pos.getPosY(), pos.getPosZ());
            sender.sendMessage(new TextComponentString("§aPokemon spawned at pos :" + pos.getWorld().getWorldInfo().getWorldName() + " x:" + pos.getPosX() + " y:"+pos.getPosY()+ " z:" + pos.getPosZ()));
        }
		else {
			sender.sendMessage(new TextComponentString(getUsage(sender)));
		}
	}
	



	@Override
	public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	@Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    	  return Collections.emptyList();
    }

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}
	
	



	
}