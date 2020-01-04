package fr.flashback083.flashspec.Specs.TypeSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class TypeSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public TypeSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public TypeSpec parse(@Nullable String s) {
        return new TypeSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString("type"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
       // nbtTagCompound.setString("type", this.value);
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
        return entityPixelmon.getPokemonData().getBaseStats().getTypeList().contains(EnumType.parseType(this.value));
        //if (entityPixelmon.getPokemonData().getBaseStats().getType1().getName().equalsIgnoreCase(this.value) || entityPixelmon.getPokemonData().getBaseStats().getType2().getName().equalsIgnoreCase(this.value) ) return true;
        //return false; //entityPixelmon.getPokemonData().getBaseStats().getType2().getName().equalsIgnoreCase(this.value);
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getBaseStats().getTypeList().contains(EnumType.parseType(this.value));
    }

    @Override
    public SpecValue<String> clone() {
        return new TypeSpec(this.keys,this.value);
    }
}
