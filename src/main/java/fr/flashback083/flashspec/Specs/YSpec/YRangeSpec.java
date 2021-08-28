package fr.flashback083.flashspec.Specs.YSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class YRangeSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public YRangeSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public YRangeSpec parse(@Nullable String s) {
        return new YRangeSpec(this.keys, s);
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
        int value1 = Integer.parseInt(this.value.split("-")[0]);
        int value2 = Integer.parseInt(this.value.split("-")[1]);
        if (value1 >= value2){
            return;
        }
        Random r = new Random();
        int y =  r.nextInt((value2 - value1) + 1) + value1;
        entityPixelmon.setPositionAndUpdate(entityPixelmon.getPosition().getX(),y,entityPixelmon.getPosition().getZ());
    }


    @Override
    public void apply(Pokemon pokemon) {
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return false;
    }


    @Override
    public boolean matches(Pokemon pokemon) {
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new YRangeSpec(this.keys,this.value);
    }
}
