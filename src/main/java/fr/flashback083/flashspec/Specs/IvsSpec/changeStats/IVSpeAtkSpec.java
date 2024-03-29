package fr.flashback083.flashspec.Specs.IvsSpec.changeStats;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class IVSpeAtkSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public IVSpeAtkSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public IVSpeAtkSpec parse(@Nullable String s) {
        return new IVSpeAtkSpec(this.keys, s);
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
        if (this.value.startsWith("+")){
            int iv = Integer.parseInt(this.value.replace("+",""));
            int maxvalue = 31;
            if (pokemon.getIVs().getStat(StatsType.SpecialAttack) + iv >= maxvalue){
                pokemon.getIVs().setStat(StatsType.SpecialAttack,maxvalue);
            }else {
                pokemon.getIVs().setStat(StatsType.SpecialAttack,pokemon.getIVs().getStat(StatsType.SpecialAttack) + iv);
            }
        }else if (this.value.startsWith("-")){
            int iv = Integer.parseInt(this.value.replace("-",""));
            if (pokemon.getIVs().getStat(StatsType.SpecialAttack) - iv <= 0){
                pokemon.getIVs().setStat(StatsType.SpecialAttack,0);
            }else {
                pokemon.getIVs().setStat(StatsType.SpecialAttack,pokemon.getIVs().getStat(StatsType.SpecialAttack) - iv);
            }
        }else{
            int iv = Integer.parseInt(this.value);
            pokemon.getIVs().setStat(StatsType.SpecialAttack,iv);
        }
        pokemon.markDirty(EnumUpdateType.Stats);
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
        return new IVSpeAtkSpec(this.keys,this.value);
    }
}
