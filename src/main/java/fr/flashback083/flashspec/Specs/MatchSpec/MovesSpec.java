package fr.flashback083.flashspec.Specs.MatchSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class MovesSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public MovesSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public MovesSpec parse(@Nullable String s) {
        return new MovesSpec(this.keys, s.replaceAll("_"," "));
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        //nbtTagCompound.setString(this.key, this.value.replaceAll("_"," "));
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
        this.apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {

    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return this.matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        String atk = this.value.replaceAll("_", " ");
        boolean isattack = Attack.hasAttack(atk);
        if (isattack) {
            return pokemon.getMoveset().hasAttack(atk);
        }else {
            return false;
        }
    }

    @Override
    public SpecValue<String> clone() {
        return new MovesSpec(this.keys,this.value.replaceAll("_"," "));
    }
}
