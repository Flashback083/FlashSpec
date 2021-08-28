package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HAChanceSpec extends SpecValue implements ISpecType {

    //private String value;
    //private List<String> keys;

    public HAChanceSpec(String value){
        super("hachance", value);
        /*super(keys.get(0), value);
        this.keys = keys;
        this.value = value;*/
    }


    public List getKeys() {
        return Arrays.asList("hachance");
    }

    @Override
    public HAChanceSpec parse(@Nullable String s) {
        return new HAChanceSpec(s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString("hachance", specValue.value.toString());
    }

    public Class getSpecClass() {
        return this.getClass();
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
        int chance = Integer.parseInt((String)this.value);
        if (chance == 0){
            pokemon.setAbilitySlot(0);
            return;
        }
        int random =  new Random().nextInt(chance)+1;
        boolean choose = random == chance;
        if (choose){
            pokemon.setAbilitySlot(2);
        }else {
            pokemon.setAbilitySlot(0);
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return true;
    }

    @Override
    public SpecValue<String> clone() {
        return new HAChanceSpec((String)this.value);
    }
}
