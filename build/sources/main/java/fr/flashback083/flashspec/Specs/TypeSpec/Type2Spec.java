package fr.flashback083.flashspec.Specs.TypeSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class Type2Spec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public Type2Spec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public Type2Spec parse(@Nullable String s) {
        return new Type2Spec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString("type2"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString("type2", this.value);
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
            /*EnumType type = EnumType.valueOf(this.value);
            entityPixelmon.getPokemonData().getBaseStats().types.set(1,type);*/
    }

    @Override
    public void apply(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void apply(Pokemon pokemon) {
        /*EnumType type = EnumType.valueOf(this.value);
        pokemon.getBaseStats().types.set(1,type);*/
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  entityPixelmon.getPokemonData().getBaseStats().getType2().getName().equalsIgnoreCase(this.value);
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getBaseStats().getType2().getName().equalsIgnoreCase(this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new Type2Spec(this.keys,this.value);
    }
}
