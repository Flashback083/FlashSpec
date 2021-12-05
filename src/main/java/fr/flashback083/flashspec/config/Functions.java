package fr.flashback083.flashspec.config;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore.MAX_TOTAL_EVS;

public class Functions {

    private static final String regex = "&(?=[0-9a-ff-or])";
    private static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);


    /*public static String getStringColored(String msg){
        return msg.replaceAll("&4","§4")
                .replaceAll("&c","§c")
                .replaceAll("&6","§6")
                .replaceAll("&e","§e")
                .replaceAll("&2","§2")
                .replaceAll("&a","§a")
                .replaceAll("&b","§b")
                .replaceAll("&3","§3")
                .replaceAll("&1","§1")
                .replaceAll("&9","§9")
                .replaceAll("&d","§d")
                .replaceAll("&5","§5")
                .replaceAll("&f","§f")
                .replaceAll("&7","§7")
                .replaceAll("&8","§8")
                .replaceAll("&0","§0")
                .replaceAll("&r","§r")
                .replaceAll("&l","§l")
                .replaceAll("&o","§o")
                .replaceAll("&n","§n")
                .replaceAll("&m","§m")
                .replaceAll("&k","§k");
    }*/


    /*public static String getStringColored(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            line = line.replaceAll(regex, "§");
        }
        return line;
    }*/

    public static int getRemainingEVs(int[] ivs) {
        return Math.max(0, MAX_TOTAL_EVS - ivs[0] - ivs[1] - ivs[2] - ivs[3] - ivs[4] - ivs[5]);
    }

    public static Map<String, Integer> getOrder(NBTTagCompound data){
        HashMap<String,Integer> notsorted = new HashMap<>();
        if (data.hasKey("gevhp")){
            notsorted.put("gevhp",data.getInteger("gevhp"));
        }
        if (data.hasKey("gevatk")){
            notsorted.put("gevatk",data.getInteger("gevatk"));
        }
        if (data.hasKey("gevdef")){
            notsorted.put("gevdef",data.getInteger("gevdef"));
        }
        if (data.hasKey("gevspeatk")){
            notsorted.put("gevspeatk",data.getInteger("gevspeatk"));
        }
        if (data.hasKey("gevspedef")){
            notsorted.put("gevspedef",data.getInteger("gevspedef"));
        }
        if (data.hasKey("gevspeed")){
            notsorted.put("gevspeed",data.getInteger("gevspeed"));
        }
        return notsorted.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));
    }

}
