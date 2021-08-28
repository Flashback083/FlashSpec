package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ShinyChanceSpec extends SpecValue<String> implements ISpecType {

    //private String value;
    //private List<String> keys;

    public ShinyChanceSpec(String value){
        /*super(keys.get(0), value);
        this.keys = keys;
        this.value = value;*/
        super("shinychance", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("shinychance");
    }

    @Override
    public ShinyChanceSpec parse(@Nullable String s) {
        return new ShinyChanceSpec(s);
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
        int chance = Integer.parseInt(this.value);
        if (chance == 0){
            pokemon.setShiny(false);
            return;
        }
        int random =  new Random().nextInt(chance)+1;
        boolean choose = random == chance;
        pokemon.setShiny(choose);
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return true;
    }

    @Override
    public SpecValue<String> clone() {
        return new ShinyChanceSpec(this.value);
    }
}
