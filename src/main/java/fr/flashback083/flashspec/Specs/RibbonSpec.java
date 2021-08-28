package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RibbonSpec extends SpecValue implements ISpecType {

    public RibbonSpec(String value) {
        super("fribbon", value);
    }

    public List getKeys() {
        return Arrays.asList("fribbon");
    }

    public Class getSpecClass() {
        return this.getClass();
    }

    public Class getValueClass() {
        return String.class;
    }

    public RibbonSpec parse(String arg) {
        return new RibbonSpec(arg);
    }

    public String toParameterForm(SpecValue value) {
        return value.key + ":" + value.value.toString();
    }

    public SpecValue clone() {
        return new RibbonSpec((String)this.value);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue specValue) {
        nbtTagCompound.setString(this.key, (String) specValue.value);
    }

    public boolean matches(EntityPixelmon pokemon) {
        return matches(pokemon.getPokemonData());
    }

    public boolean matches(Pokemon pokemon) {
        return Objects.equals(pokemon.getDisplayedRibbon().name(), this.value);
    }

    public void apply(EntityPixelmon pokemon) {
        apply(pokemon.getPokemonData());
    }

    public void apply(Pokemon pokemon) {
        if (((String) this.value).equalsIgnoreCase("none")){
            pokemon.getRibbons().clear();
        }else {
            try{
                if (((String) this.value).startsWith("!")){
                    if (pokemon.getRibbons().contains(EnumRibbonType.valueOf(((String)this.value).replaceFirst("!","").toUpperCase()))){
                        pokemon.getRibbons().remove(EnumRibbonType.valueOf(((String)this.value).toUpperCase()));
                    }
                }
                if (!pokemon.getRibbons().contains(EnumRibbonType.valueOf(((String)this.value).toUpperCase()))){
                    pokemon.addRibbon(EnumRibbonType.valueOf(((String)this.value).toUpperCase()));
                }
            }catch (IllegalArgumentException e1){
                System.out.println("No ribbon with that name");
            }
        }

    }
}

