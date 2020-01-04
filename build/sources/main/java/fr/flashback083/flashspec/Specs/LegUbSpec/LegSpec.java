package fr.flashback083.flashspec.Specs.LegUbSpec;

import com.google.common.collect.Lists;
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
		super("leg", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("leg","isleg");
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
		return new LegSpec(nbt.getBoolean("leg"));
	}

	@Override
	public String toParameterForm(SpecValue<?> spec)
	{
		return key + ":" + spec.value.toString();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, SpecValue<?> spec)
	{
		nbt.setBoolean("leg", spec.value == Boolean.TRUE);
	}

	@Override
	public void apply(EntityPixelmon pixelmon)
	{
		apply(pixelmon.getPokemonData());
	}

	@Override
	public void apply(NBTTagCompound arg)
	{
		// We don't care about this anymore. I'm not offering support to this as it's going to be deprecated and phased out.
	}

	@Override
	public void apply(Pokemon pokemon)
	{
		if (this.value){
			pokemon.setSpecies(EnumSpecies.getFromName(CollectionHelper.getRandomElement(EnumSpecies.legendaries)).get(), true);
		}else {
			pokemon.setSpecies(EnumSpecies.randomPoke(false), true);
		}
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
		return matches(pixelmon);
	}

	@Override
	public boolean matches(NBTTagCompound nbt)
	{
		// Don't offer support for this anymore.
		return false;
	}

	@Override
	public boolean matches(Pokemon pokemon)
	{
		return pokemon.isLegendary() == this.value;
		//return (EnumSpecies.legendaries.contains(pokemon.getSpecies().getPokemonName())  == this.value);
	}
}
