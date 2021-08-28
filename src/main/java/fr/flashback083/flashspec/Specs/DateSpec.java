package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.List;

public class DateSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public DateSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public DateSpec parse(@Nullable String s) {
        return new DateSpec(this.keys, s);
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
        apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {
        if (this.value.equalsIgnoreCase("now")){
            Calendar calendar = Calendar.getInstance();
            pokemon.getPersistentData().setLong("fdate",calendar.getTimeInMillis());
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        long milis = pokemon.getPersistentData().getLong("fdate");
        if (milis==0){
            return false;
        }
        if (this.value.startsWith("<")){
            long time = Long.parseLong(this.value.split("<")[1]);
            return milis < time;
        }else if (this.value.startsWith(">")){
            long time = Long.parseLong(this.value.split(">")[1]);
            return milis > time;
        }else{
            return false;
        }

    }

    @Override
    public SpecValue<String> clone() {
        return new DateSpec(this.keys,this.value);
    }
}
