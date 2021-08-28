package fr.flashback083.flashspec.Specs.MatchSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class isWildSpec extends SpecValue<Boolean> implements ISpecType
{
	public isWildSpec(boolean value)
	{
		super("isWild", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("isWild");
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
			return new isWildSpec(true);
		
		try
		{
			return new isWildSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new isWildSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new isWildSpec(nbt.getBoolean(this.key));
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
		//
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new isWildSpec(value);
	}

	@Override
	public Class<Boolean> getValueClass()
	{
		return Boolean.class;
	}

	@Override
	public boolean matches(EntityPixelmon pixelmon)
	{
		if (pixelmon.hasOwner()) {
			return false;
		}
		if (pixelmon.isBossPokemon()){
			return false;
		}
		return true;
	}


	@Override
	public boolean matches(Pokemon pokemon)
	{
		return false;
	}
}
