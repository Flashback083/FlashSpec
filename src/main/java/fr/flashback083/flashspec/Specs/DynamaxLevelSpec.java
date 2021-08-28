package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class DynamaxLevelSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public DynamaxLevelSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public DynamaxLevelSpec parse(@Nullable String s) {
        return new DynamaxLevelSpec(this.keys, s);
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
    public void apply(Pokemon pokemon) {
        if (this.value.startsWith("+")){
            int lvl = Integer.parseInt(this.value.replace("+",""));
            int maxvalue = 10;
            if (pokemon.getDynamaxLevel() + lvl >= maxvalue){
                pokemon.setDynamaxLevel(maxvalue);
            }else {
                pokemon.setDynamaxLevel(pokemon.getDynamaxLevel()+lvl);
            }
        }else if (this.value.startsWith("-")){
            int lvl = Integer.parseInt(this.value.replace("-",""));
            if (pokemon.getDynamaxLevel() - lvl <= 0){
                pokemon.setDynamaxLevel(0);
            }else {
                pokemon.setDynamaxLevel(pokemon.getDynamaxLevel()-lvl);
            }
        }else{
            pokemon.setDynamaxLevel(Math.min(10,Integer.parseInt(this.value)));
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
      return matches(entityPixelmon.getPokemonData());
    }


    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getFriendship() == Integer.parseInt(this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new DynamaxLevelSpec(this.keys,this.value);
    }
}
