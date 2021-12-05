package fr.flashback083.flashspec.config;

import java.util.HashMap;

public class SpecGroup {

    private final HashMap<String,String> specList;

    public SpecGroup(HashMap<String,String> specList){
        this.specList = specList;
    }

    public HashMap<String, String> getSpecList() {
        return specList;
    }
}
