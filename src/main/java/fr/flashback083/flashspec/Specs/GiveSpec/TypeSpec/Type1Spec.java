package fr.flashback083.flashspec.Specs.GiveSpec.TypeSpec;

import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class Type1Spec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public Type1Spec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public Type1Spec parse(@Nullable String s) {
        return new Type1Spec(this.keys, s);
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
    public void apply(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void apply(Pokemon pokemon) {
        PokemonSpec spec = PokemonSpec.from(randomPokeType(this.value).getPokemonName());
        /*Pokemon newpokemon = spec.create();
        pokemon.readFromNBT(newpokemon.writeToNBT(new NBTTagCompound()));*/
        spec.apply(pokemon);
        pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return entityPixelmon.getPokemonData().getBaseStats().getType1().getName().equalsIgnoreCase(this.value);
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getBaseStats().getType1().getName().equalsIgnoreCase(this.value);
    }

    @Override
    public SpecValue<String> clone() {
        return new Type1Spec(this.keys,this.value);
    }

    private EnumSpecies randomPokeType(String value){
        EnumSpecies s = EnumSpecies.randomPoke();
        boolean isValid = false;
        while(!isValid) {
            if (!s.getBaseStats().getType1().getName().equalsIgnoreCase(value)) {
                s = EnumSpecies.randomPoke();
            }else {
                isValid = true;
            }
        }
        return s;
    }

    private EnumSpecies randomPokeTypeLeg(String value){
        EnumSpecies s = EnumSpecies.randomPoke();
        boolean isValid = false;
        while(!isValid) {
            if (!s.getBaseStats().getType1().getName().equalsIgnoreCase(value) && !EnumSpecies.legendaries.contains(s.getPokemonName())) {
                s = EnumSpecies.randomPoke();
            }else {
                isValid = true;
            }
        }
        return s;
    }

}
