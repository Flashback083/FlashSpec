package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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
		return new UncatchableSpec(nbt.getBoolean(this.key));
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
		pokemon.getBonusStats().setPreventsCapture(this.value);
		if(!pokemon.getPersistentData().hasKey("flashspec")){
			pokemon.getPersistentData().setTag("flashspec",new NBTTagList());
		}
		pokemon.getPersistentData().getTagList("flashspec",8).appendTag(new NBTTagString(this.key+":"+this.value));

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
