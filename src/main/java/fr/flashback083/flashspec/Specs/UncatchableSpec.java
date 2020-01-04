package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class UncatchableSpec extends SpecValue<Boolean> implements ISpecType
{
	public UncatchableSpec(boolean value)
	{
		super("uncatchable", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("uncatchable");
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
			return new UncatchableSpec(true);
		
		try
		{
			return new UncatchableSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new UncatchableSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new UncatchableSpec(nbt.getBoolean("uncatchable"));
	}

	@Override
	public String toParameterForm(SpecValue<?> spec)
	{
		return key + ":" + spec.value.toString();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, SpecValue<?> spec)
	{
		nbt.setBoolean("uncatchable", spec.value == Boolean.TRUE);
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
		pokemon.getBonusStats().setPreventsCapture(this.value);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new UncatchableSpec(value);
	}

	@Override
	public Class<Boolean> getValueClass()
	{
		return Boolean.class;
	}

	@Override
	public boolean matches(EntityPixelmon pixelmon)
	{
		return pixelmon.getPokemonData().getBonusStats().preventsCapture() == this.value;
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
		return pokemon.getBonusStats().preventsCapture() == this.value;
		//return (EnumSpecies.legendaries.contains(pokemon.getSpecies().getPokemonName())  == this.value);
	}
}
