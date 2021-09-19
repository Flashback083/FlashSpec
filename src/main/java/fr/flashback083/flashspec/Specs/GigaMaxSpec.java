package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class GigaMaxSpec extends SpecValue<Boolean> implements ISpecType
{
	public GigaMaxSpec(boolean value)
	{
		super("fgigamax", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("fgigamax");
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
			return new GigaMaxSpec(true);
		
		try
		{
			return new GigaMaxSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new GigaMaxSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new GigaMaxSpec(nbt.getBoolean(this.key));
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
		if (this.value){
            pokemon.setGigantamaxFactor(true);
		}
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new GigaMaxSpec(value);
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
        return false;
	}


}
