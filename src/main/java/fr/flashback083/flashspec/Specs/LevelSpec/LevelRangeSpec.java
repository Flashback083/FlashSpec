package fr.flashback083.flashspec.Specs.LevelSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LevelRangeSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public LevelRangeSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public LevelRangeSpec parse(@Nullable String s) {
        return new LevelRangeSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString(this.key, (String) specValue.value);
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
        int maxvalue = PixelmonConfig.maxLevel;
        int value1 = Integer.parseInt(this.value.split("-")[0]);
        int value2 = Integer.parseInt(this.value.split("-")[1]);
        if (value1 >= value2){
            return;
        }
        if (value1 > 0 && value1 < maxvalue && value2 <= maxvalue){
            Random r = new Random();
            int level =  r.nextInt((value2 - value1) + 1) + value1;
            entityPixelmon.getPokemonData().setLevel(level);
            entityPixelmon.getPokemonData().getMoveset().attacks = entityPixelmon.getPokemonData().getBaseStats().loadMoveset(level).attacks;
        }
    }


    @Override
    public void apply(Pokemon pokemon) {
        int maxvalue = PixelmonConfig.maxLevel;
        int value1 = Integer.parseInt(this.value.split("-")[0]);
        int value2 = Integer.parseInt(this.value.split("-")[1]);
        if (value1 >= value2){
            return;
        }
        if (value1 > 0 && value1 < maxvalue && value2 <= maxvalue){
            Random r = new Random();
            int level =  r.nextInt((value2 - value1) + 1) + value1;
            pokemon.setLevel(level);
            pokemon.getMoveset().attacks = pokemon.getBaseStats().loadMoveset(level).attacks;
        }

    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return matches(entityPixelmon.getPokemonData());
    }


    @Override
    public boolean matches(Pokemon pokemon) {
        int level = pokemon.getLevel();
        int x = Integer.parseInt(this.value.split("-")[0]);
        int y = Integer.parseInt(this.value.split("-")[1]);
        //lvlr:x-y
        //lvlr:10-20
        //poke level = 15
        //donc il doit être >= x et <= y
        return level >= x && level <= y;
    }

    @Override
    public SpecValue<String> clone() {
        return new LevelRangeSpec(this.keys,this.value);
    }
}
