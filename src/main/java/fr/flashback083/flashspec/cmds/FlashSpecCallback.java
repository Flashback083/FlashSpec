package fr.flashback083.flashspec.cmds;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.flashback083.flashspec.PaginationApi.PaginationApi.*;
import static fr.flashback083.flashspec.config.GsonUtils.*;

public class FlashSpecCallback extends CommandBase implements ICommand {

    private final List<String> aliases;

    public FlashSpecCallback(){
        aliases = Lists.newArrayList();
    }

    @Override
    public String getName() {
        return "flashspeccallback";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "§a/flashspeccallback <uuid> <player> <next/previous>";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        //boolean isPlayer = sender instanceof EntityPlayerMP;
        if (args.length == 3){
            if (args[2].equalsIgnoreCase("previous")){
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[1]);
                sendPreviousPage(player,getPaginationFromUUID(UUID.fromString(args[0])));
            }else if (args[2].equalsIgnoreCase("next")){
                EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(args[1]);
                sendNextPage(player,getPaginationFromUUID(UUID.fromString(args[0])));
            }
        }else {
            sender.sendMessage((new TextComponentString(getUsage(sender))));
        }
    }



    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
