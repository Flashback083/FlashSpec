package fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class UbSpec extends SpecValue<Boolean> implements ISpecType
{
	public UbSpec(boolean value)
	{
		super("ub", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("ub","isub");
	}

	@Override
	public Class<? extends SpecValue<?>> getSpecClass()
	{
		return getClass();
	}

	@Override
	public SpecValue<?> parse(String s)
	{
		if (s == null)
			return new UbSpec(true);
		
		try
		{
			return new UbSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new UbSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new UbSpec(nbt.getBoolean(this.key));
	}

	@Override
	public String toParameterForm(SpecValue<?> spec)
	{
		return key + ":" + spec.value.toString();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, SpecValue<?> spec)
	{
		nbt.setBoolean(this.key, spec.value == Boolean.TRUE);
	}

	@Override
	public void apply(EntityPixelmon pixelmon)
	{
		apply(pixelmon.getPokemonData());
	}

	@Override
	public void apply(Pokemon pokemon)
	{
		//PokemonSpec spec;
		if (this.value){
			//spec = PokemonSpec.from(CollectionHelper.getRandomElement(EnumSpecies.ultrabeasts));
			pokemon.setSpecies(CollectionHelper.getRandomElement(EnumSpecies.ultrabeasts), true);
			pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
		}else {
			//spec = PokemonSpec.from(randomPokeNoUb().getPokemonName());
			EnumSpecies specie = randomPokeNoUb();
			if (specie.isLegendary()){
				pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
			}else {
				pokemon.setSpecies(specie,true);
				pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
			}
		}
		//spec.apply(pokemon);
		//pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new UbSpec(value);
	}

	@Override
	public Class<Boolean> getValueClass()
	{
		return Boolean.class;
	}

	@Override
	public boolean matches(EntityPixelmon pixelmon)
	{
		return matches(pixelmon.getPokemonData());
	}

	@Override
	public boolean matches(Pokemon pokemon)
	{
		return (EnumSpecies.ultrabeasts.contains(pokemon.getSpecies())  == this.value);
	}


	private EnumSpecies randomPokeNoUb(){

		ArrayList<EnumSpecies> list = Lists.newArrayList(EnumSpecies.values());
		EnumSpecies.ultrabeasts.forEach(list::remove);
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
