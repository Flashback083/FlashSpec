package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class LevelSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public LevelSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public LevelSpec parse(@Nullable String s) {
        return new LevelSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString("lvlc"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString("lvlc", this.value);
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
        if (this.value.startsWith("+")){
           int lvl = Integer.parseInt(this.value.replace("+",""));
           int maxvalue = PixelmonConfig.maxLevel;
           if (entityPixelmon.getPokemonData().getLevel() + lvl >= maxvalue){
               entityPixelmon.getPokemonData().setLevel(maxvalue);
           }else {
               entityPixelmon.getPokemonData().setLevel(entityPixelmon.getPokemonData().getLevel() + lvl);
           }
        }else if (this.value.startsWith("-")){
            int lvl = Integer.parseInt(this.value.replace("-",""));
            if (entityPixelmon.getPokemonData().getLevel() - lvl <= 1){
                entityPixelmon.getPokemonData().setLevel(1);
            }else {
                entityPixelmon.getPokemonData().setLevel(entityPixelmon.getPokemonData().getLevel() - lvl);
            }
        }
    }

    @Override
    public void apply(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void apply(Pokemon pokemon) {
        if (this.value.startsWith("+")){
            int lvl = Integer.parseInt(this.value.replace("+",""));
            int maxvalue = PixelmonConfig.maxLevel;
            if (pokemon.getLevel() + lvl >= maxvalue){
                pokemon.setLevel(maxvalue);
            }else {
                pokemon.setLevel(pokemon.getLevel() + lvl);
            }
        }else if (this.value.startsWith("-")){
            int lvl = Integer.parseInt(this.value.replace("-",""));
            if (pokemon.getLevel() - lvl <= 1){
                pokemon.setLevel(1);
            }else {
                pokemon.setLevel(pokemon.getLevel() - lvl);
            }
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return false;
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new LevelSpec(this.keys,this.value);
    }
}
