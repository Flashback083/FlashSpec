package fr.flashback083.flashspec.config;

import fr.flashback083.flashspec.FlashSpec;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GsonUtilsSpecGroup {

    public static void writeJson(SpecGroup players){
        try (PrintWriter writer = new PrintWriter(FlashSpec.customspecgroup,"UTF-8")) {
            FlashSpec.gson.toJson(players, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SpecGroup readJson(){
        try (Reader reader = new InputStreamReader(new FileInputStream(FlashSpec.customspecgroup), StandardCharsets.UTF_8)) {
            SpecGroup data = FlashSpec.gson.fromJson(reader, SpecGroup.class);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static HashMap<String,String> getSpecGroupList(){
        SpecGroup specfile = readJson();
        return specfile.getSpecList();
    }




}
