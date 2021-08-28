package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class EggStepSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public EggStepSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public EggStepSpec parse(@Nullable String s) {
        return new EggStepSpec(this.keys, s);
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
        int totalstep = PixelmonConfig.stepsPerEggCycle * 22;
        int stepneed = totalstep - Integer.parseInt(this.value);
        pokemon.setEggCycles(21);
        return pokemon.getEggSteps() == (stepneed);
    }


    public void apply(EntityPixelmon pixelmon) {
        int totalstep = PixelmonConfig.stepsPerEggCycle * 22;
        int stepneed = totalstep - Integer.parseInt(this.value);
        pixelmon.getPokemonData().setEggCycles(21);
        pixelmon.getPokemonData().setEggSteps(stepneed);
    }

    public void apply(Pokemon pokemon) {
        int totalstep = PixelmonConfig.stepsPerEggCycle * 22;
        int stepneed = totalstep - Integer.parseInt(this.value);
        pokemon.setEggCycles(21);
        pokemon.setEggSteps(stepneed);
    }

    @Override
    public SpecValue<String> clone() {
        return new EggStepSpec(this.keys,this.value);
    }
}
