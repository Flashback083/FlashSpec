package fr.flashback083.flashspec.cmds;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import fr.flashback083.flashspec.FlashSpec;
import fr.flashback083.flashspec.config.Config;
import fr.flashback083.flashspec.config.Lang;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
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
		return 	TextFormatting.LIGHT_PURPLE+"/flashspec <reload/debug>";
				
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
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"FlashSpec's lang and config reloaded!"));
		}else if (args.length ==1 && args[0].equalsIgnoreCase("debug")){
			PokemonSpec.extraSpecTypes.forEach(iSpecType -> {
				FlashSpec.logger.info("[FlashSpec] List spec " + iSpecType.getKeys().get(0));
				FlashSpec.logger.info("[FlashSpec] List spec class " + iSpecType.getSpecClass().getCanonicalName());
			});
		}
		else {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error in the command ! " + TextFormatting.AQUA + getUsage(sender)));
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