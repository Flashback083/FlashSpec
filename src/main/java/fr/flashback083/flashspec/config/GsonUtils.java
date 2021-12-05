package fr.flashback083.flashspec.config;

import com.google.gson.JsonObject;
import fr.flashback083.flashspec.FlashSpec;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GsonUtils {

    public static void writeJson(JsonObject players){
        try (PrintWriter writer = new PrintWriter(FlashSpec.speclist,"UTF-8")) {
            FlashSpec.gson.toJson(players, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static JsonObject readJson(){
        try (Reader reader = new InputStreamReader(new FileInputStream(FlashSpec.speclist), StandardCharsets.UTF_8)) {
            JsonObject data = FlashSpec.gson.fromJson(reader, JsonObject.class);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addSpec(String name, String spec){
        JsonObject specfile = readJson();
        if (specfile.has(name)){
            specfile.remove(name);
        }
        specfile.addProperty(name,spec);
        writeJson(specfile);
    }

    public static boolean removeSpec(String name){
        JsonObject specfile = readJson();
        if (specfile.has(name)){
            specfile.remove(name);
            writeJson(specfile);
            return true;
        }
        return false;
    }

    public static HashMap<String,String> getSpecList(){
        JsonObject specfile = readJson();
        HashMap<String,String> speclist = new HashMap<>();
        if (specfile.size()==0) return speclist;
        specfile.entrySet().forEach(stringJsonElementEntry -> {
            speclist.put(stringJsonElementEntry.getKey(),stringJsonElementEntry.getValue().getAsString());
        });
        return speclist;
    }

    public static String getSpecforKey(String key){
        JsonObject specfile = readJson();
        if (specfile.has(key)){
            return specfile.get(key).getAsString();
        }
        return "";
    }




}
