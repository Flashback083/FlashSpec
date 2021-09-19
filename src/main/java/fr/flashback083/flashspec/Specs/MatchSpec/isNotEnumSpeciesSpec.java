package fr.flashback083.flashspec.Specs.MatchSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class isNotEnumSpeciesSpec extends SpecValue<String> implements ISpecType {

    public isNotEnumSpeciesSpec(String value){
        super("isNotEnumSpecies", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("isnotenumspecies");
    }

    @Override
    public isNotEnumSpeciesSpec parse(@Nullable String s) {
        return new isNotEnumSpeciesSpec(s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString(this.key, specValue.value.toString());
    }

    public Class getSpecClass() {
        return this.getClass();
    }


    @Override
    public String toParameterForm(SpecValue<?> specValue) {
        return key + ":" + value;
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public void apply(EntityPixelmon entityPixelmon) {
        apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {
        List<String> species = Lists.newArrayList(this.value.split("/"));
        List<String> pokeList = Lists.newArrayList(EnumSpecies.getNameList());
        pokeList.removeAll(species);
        pokemon.setSpecies(EnumSpecies.getFromNameAnyCase(CollectionHelper.getRandomElement(pokeList)),true);
        pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        String name = pokemon.getSpecies().getPokemonName();
        ArrayList<String> list = Lists.newArrayList(this.value.split("/"));
        boolean match = true;
        for (String species : list) {
            if (species.replaceAll("_"," ").equalsIgnoreCase(name)) {
                match = false;
                break;
            }
        }
        return match;
    }

    @Override
    public SpecValue<String> clone() {
        return new isNotEnumSpeciesSpec(this.value);
    }
}
