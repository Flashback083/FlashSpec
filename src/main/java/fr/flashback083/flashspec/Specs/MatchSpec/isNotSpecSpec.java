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

public class isNotSpecSpec extends SpecValue<String> implements ISpecType {

    public isNotSpecSpec(String value){
        super("isNotSpec", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("isnotspec");
    }

    @Override
    public isNotSpecSpec parse(@Nullable String s) {
        return new isNotSpecSpec(s);
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
        List<String> pokeList = Lists.newArrayList(EnumSpecies.getNameList());
        pokeList.removeAll(species);

        pokemon.setSpecies(EnumSpecies.getFromNameAnyCase(CollectionHelper.getRandomElement(pokeList).split("_")[0]),true);
        pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);*/
        /*pokeList.forEach(spec -> {
            String specs = spec.split([])
        });*/
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        ArrayList<String> list = Lists.newArrayList(this.value.split("/"));
        boolean match = true;
        for (String spec : list) {
            if (new PokemonSpec(spec.replaceAll("_"," ")).matches(pokemon)) {
                match = false;
                break;
            }
        }
        return match;
    }

    @Override
    public SpecValue<String> clone() {
        return new isNotSpecSpec(this.value);
    }
}
