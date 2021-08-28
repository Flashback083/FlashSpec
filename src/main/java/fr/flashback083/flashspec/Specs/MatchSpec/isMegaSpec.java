package fr.flashback083.flashspec.Specs.MatchSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.forms.EnumMega;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class isMegaSpec extends SpecValue<Boolean> implements ISpecType
{
	public isMegaSpec(boolean value)
	{
		super("isMega", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("isMega");
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
			return new isMegaSpec(true);
		
		try
		{
			return new isMegaSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new isMegaSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new isMegaSpec(nbt.getBoolean(this.key));
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
		//pokemon.setForm(EnumMega.Mega);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new isMegaSpec(value);
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
		if (pokemon.getFormEnum().getLocalizedName().equalsIgnoreCase(EnumMega.Mega.getLocalizedName())){
			return true;
		}
		if (pokemon.getFormEnum().getLocalizedName().equalsIgnoreCase(EnumMega.MegaX.getLocalizedName())){
			return true;
		}
		return pokemon.getFormEnum().getLocalizedName().equalsIgnoreCase(EnumMega.MegaY.getLocalizedName());
	}
}
