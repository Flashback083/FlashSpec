package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class RealTimeSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public RealTimeSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public RealTimeSpec parse(@Nullable String s) {
        return new RealTimeSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue specValue) {
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
    public boolean matches(EntityPixelmon pixelmon) {
        return this.matches(pixelmon.getPokemonData());
    }



    public boolean matches(Pokemon pokemon) {
        return pokemon.getPersistentData().hasKey("realtime") && pokemon.getPersistentData().getString("realtime").equals(this.value);
    }


    public void apply(EntityPixelmon pixelmon) {
        pixelmon.getPokemonData().getPersistentData().setString("realtime", this.value);
    }

    public void apply(Pokemon pokemon) {
        pokemon.getPersistentData().setString("realtime", this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new RealTimeSpec(this.keys,this.value);
    }
}
