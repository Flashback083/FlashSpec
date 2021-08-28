package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class AbsSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public AbsSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public AbsSpec parse(@Nullable String s) {
        return new AbsSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setInteger(this.key,Integer.parseInt(this.value));
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
        entityPixelmon.getPokemonData().setAbilitySlot(Integer.parseInt(this.value));
    }

    @Override
    public void apply(Pokemon pokemon) {
        pokemon.setAbilitySlot(Integer.parseInt(this.value));
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  entityPixelmon.getPokemonData().getAbilitySlot() == Integer.parseInt(this.value);
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getAbilitySlot() == Integer.parseInt(this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new AbsSpec(this.keys,this.value);
    }
}
