package fr.flashback083.flashspec.Specs.MatchSpec.EggGroupSpec;

import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class EggGroup1Spec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public EggGroup1Spec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public EggGroup1Spec parse(@Nullable String s) {
        return new EggGroup1Spec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString(this.key, this.value);
    }

    @Override
    public Class<? extends SpecValue<?>> getSpecClass() {
        return getClass();
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
    public void apply(Pokemon pokemon) {}

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return this.matches(entityPixelmon.getPokemonData());
    }


    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getBaseStats().eggGroups[0].name().equalsIgnoreCase(this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new EggGroup1Spec(this.keys,this.value);
    }


}
