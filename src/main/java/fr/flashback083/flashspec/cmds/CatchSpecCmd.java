package fr.flashback083.flashspec.cmds;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.ShopKeeperPacket;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.EnumBuySell;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import fr.flashback083.flashspec.PaginationApi.PaginationApi;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.flashback083.flashspec.config.GsonUtils.*;

public class CatchSpecCmd extends CommandBase implements ICommand {

    private final List<String> aliases;
    //public static HashMap<String,String> specs = new HashMap<>();


    public CatchSpecCmd(){
        aliases = Lists.newArrayList();
    }

    @Override
    public String getName() {
        return "catchspec";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "§a/catchspec register <key> <specs>\n"
                + "§a/catchspec unregister <key>\n"
                + "§a/catchspec info <key>\n"
                + "§a/catchspec list";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        //sender.sendMessage(new TextComponentString(getUsage(sender)));
            if (args.length > 2 && args[0].equalsIgnoreCase("register")) {
                List<String> arg = Lists.newArrayList(args);
                arg.remove(0);
                String key = arg.get(0);
                arg.remove(0);
                String spec = String.join(" ", arg);
                addSpec(key,spec);
                sender.sendMessage(new TextComponentString("§aSpec set for the key : §e" + args[1]  + " §aspec : §e" + spec));
            }else if (args.length == 2 && args[0].equalsIgnoreCase("unregister")){
                if(removeSpec(args[1])){
                    sender.sendMessage(new TextComponentString("§aUnregister with success!"));
                }else {
                    sender.sendMessage(new TextComponentString("§cError when unregister, does the key exist ?"));
                }
            }else if (args.length == 2 && args[0].equalsIgnoreCase("info")){
                String spec = getSpecforKey(args[1]);
                if (spec.length()>0){
                    sender.sendMessage(new TextComponentString("§aSpecs for the key §e" + args[1] + " §ais : §e" + spec));
                }else {
                    sender.sendMessage(new TextComponentString("§cError when unregister, does the key exist ?"));
                }
            }else if (args.length == 1 && args[0].equalsIgnoreCase("list")){
                HashMap<String, String> hashmap = getSpecList();
                List<ITextComponent> keyspeclist = new ArrayList<>();
                hashmap.forEach((key, value) -> {
                    keyspeclist.add(new TextComponentString("§aKey : §e" + key + " §a/ Spec : §e" + value));
                });
                PaginationApi speclist = new PaginationApi.PaginationBuilder(UUID.randomUUID())
                        .setTitle(new TextComponentString("List of Key & Spec"))
                        .setTexts(keyspeclist)
                        .setLinesPerPage(5).build();
                speclist.sendTo((EntityPlayerMP) sender);
            }
            else {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + getUsage(sender)));
            }
    }



    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "register","unregister","info","list");
        }
        else{
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
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
