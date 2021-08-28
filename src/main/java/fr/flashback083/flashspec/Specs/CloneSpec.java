package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

import static com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.MewStats.MAX_CLONES;

public class CloneSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public CloneSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public CloneSpec parse(@Nullable String s) {
        return new CloneSpec(this.keys, s);
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
    public void apply(EntityPixelmon entityPixelmon) {
        this.apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {
        if (pokemon.getSpecies().equals(EnumSpecies.Mew)) {
            if (pokemon.getExtraStats() != null) {
                ((MewStats) pokemon.getExtraStats()).numCloned = Integer.parseInt(this.value);
            }
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return this.matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        if (pokemon.getSpecies().equals(EnumSpecies.Mew)) {
            if (pokemon.getExtraStats() != null) {
               return ((MewStats) pokemon.getExtraStats()).numCloned == Integer.parseInt(this.value);
            }
        }
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new CloneSpec(this.keys,this.value);
    }
}
