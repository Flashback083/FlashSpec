package fr.flashback083.flashspec.Specs.GiveSpec.LegUbLevelSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class LegSpec extends SpecValue<Boolean> implements ISpecType
{
	public LegSpec(boolean value)
	{
		super("legl", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("legl","islegl");
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
			return new LegSpec(true);
		
		try
		{
			return new LegSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new LegSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new LegSpec(nbt.getBoolean(this.key));
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
			List<EnumSpecies> species = Lists.newArrayList(EnumSpecies.LEGENDARY_ENUMS);
			pokemon.setSpecies(CollectionHelper.getRandomElement(species),true);
			//spec = PokemonSpec.from(CollectionHelper.getRandomElement(species).getPokemonName());
			//Pokemon newpokemon = spec.create();
			//pokemon.readFromNBT(newpokemon.writeToNBT(new NBTTagCompound()));
		}else {
			pokemon.setSpecies(EnumSpecies.randomPoke(false),true);
			//spec = PokemonSpec.from(EnumSpecies.randomPoke(false).getPokemonName());
		}
		//spec.apply(pokemon);
		//pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new LegSpec(value);
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
		return pokemon.isLegendary() == this.value;
		//return (EnumSpecies.legendaries.contains(pokemon.getSpecies().getPokemonName())  == this.value);
	}
}
