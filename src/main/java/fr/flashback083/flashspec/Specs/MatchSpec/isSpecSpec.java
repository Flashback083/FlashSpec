package fr.flashback083.flashspec.Specs.MatchSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class isSpecSpec extends SpecValue<String> implements ISpecType {

    public isSpecSpec(String value){
        super("isSpec", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("isSpec");
    }

    @Override
    public isSpecSpec parse(@Nullable String s) {
        return new isSpecSpec(s);
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
        /*List<String> species = Lists.newArrayList(this.value.split("/"));
        pokemon.setSpecies(EnumSpecies.getFromNameAnyCase(CollectionHelper.getRandomElement(species)),true);
        pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);*/
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        ArrayList<String> list = Lists.newArrayList(this.value.split("/"));
        boolean match = false;
        for (String spec : list) {
            if (new PokemonSpec(spec.replaceAll("_"," ")).matches(pokemon)) {
                match = true;
                break;
            }
        }
        return match;
    }

    @Override
    public SpecValue<String> clone() {
        return new isSpecSpec(this.value);
    }
}
