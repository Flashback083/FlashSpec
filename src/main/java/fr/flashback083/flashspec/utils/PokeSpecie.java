package fr.flashback083.flashspec.utils;

import com.pixelmonmod.pixelmon.enums.EnumSpecies;

public class PokeSpecie {

    private final EnumSpecies species;
    private final int form;

    public PokeSpecie(EnumSpecies species, int form){
        this.species = species;
        this.form = form;
    }

    public EnumSpecies getSpecies() {
        return species;
    }

    public int getForm() {
        return form;
    }
}
