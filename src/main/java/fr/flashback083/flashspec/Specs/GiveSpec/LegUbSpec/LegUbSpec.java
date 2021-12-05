package fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LegUbSpec extends SpecValue<Boolean> implements ISpecType {

   public LegUbSpec(boolean value) {
        super("isuborleg", value);
    }

    public List getKeys() {
        return Lists.newArrayList("uborleg", "isuborleg");
    }

    @Override
    public SpecValue<?> parse(String s)
    {
        if (s == null)
            return new LegUbSpec(true);

        try
        {
            return new LegUbSpec(Boolean.parseBoolean(s));
        }
        catch (Exception e)
        {
            return new LegUbSpec(true);
        }
    }

    public SpecValue readFromNBT(NBTTagCompound nbt) {
        return new LegUbSpec(nbt.getBoolean(this.key));
    }

    public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
        nbt.setBoolean(this.key, (Boolean) value.value);
    }

    public Class getSpecClass() {
        return LegUbSpec.class;
    }

    public String toParameterForm(SpecValue<?> value) {
        return key  + ":" + value.value.toString();
    }

    public Class getValueClass() {
        return Boolean.class;
    }

    public void apply(EntityPixelmon pixelmon) {
        this.apply(pixelmon.getPokemonData());
    }

    public void apply(Pokemon pokemon) {
        if (this.value) {
            ArrayList<EnumSpecies> list = Lists.newArrayList();
            list.addAll(EnumSpecies.ultrabeasts);
            list.addAll(EnumSpecies.legendaries);
            //list.addAll(Arrays.asList(EnumSpecies.LEGENDARY_ENUMS));
            EnumSpecies p = CollectionHelper.getRandomElement(list);
            pokemon.setSpecies(p,true);
            pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
            //PokemonSpec spec = PokemonSpec.from(p);
            //spec.apply(pokemon);

            //pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.getFromName(p).get());
            //pokemon.initialize(EnumInitializeCategory.SPECIES);
            //pokemon.setSpecies(EnumSpecies.getFromName(p).get());
        } else {
            pokemon.setSpecies(randomPokeNonLegNonUB(),true);
            //pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
            //PokemonSpec spec = PokemonSpec.from(randomPokeNonLegNonUB().getPokemonName());
            //spec.apply(pokemon);

            //pokemon.setSpecies(randomPokeNonLegNonUB(), true);
        }
        //pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
    }

    public boolean matches(EntityPixelmon pixelmon) {
        return this.matches(pixelmon.getPokemonData());
    }

    public boolean matches(Pokemon pokemon) {
       return (EnumSpecies.ultrabeasts.contains(pokemon.getSpecies()) == this.value) ||  (EnumSpecies.legendaries.contains(pokemon.getSpecies()) == this.value);
    }

    public SpecValue clone() {
        return new LegUbSpec(this.value);
    }

    private EnumSpecies randomPokeNonLegNonUB(){

        ArrayList<EnumSpecies> list = Lists.newArrayList(EnumSpecies.values());
        EnumSpecies.ultrabeasts.forEach(list::remove);
        EnumSpecies.legendaries.forEach(list::remove);
        EnumSpecies s = CollectionHelper.getRandomElement(list);
        boolean isValid = false;
        while(!isValid) {
            if (!PixelmonConfig.isGenerationEnabled(s.getGeneration())) {
                s = CollectionHelper.getRandomElement(list);
            }else {
                isValid = true;
            }
        }
        return s;
    }

}
