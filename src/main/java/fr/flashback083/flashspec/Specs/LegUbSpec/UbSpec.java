package fr.flashback083.flashspec.Specs.LegUbSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
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
	public void apply(NBTTagCompound arg)
	{
		// We don't care about this anymore. I'm not offering support to this as it's going to be deprecated and phased out.
	}

	@Override
	public void apply(Pokemon pokemon)
	{
		if (this.value){
			pokemon.setSpecies(EnumSpecies.getFromName(CollectionHelper.getRandomElement(EnumSpecies.ultrabeasts)).get(), true);
		}else {
			pokemon.setSpecies(randomPokeNoUb(),true);
		}
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
		return (EnumSpecies.ultrabeasts.contains(pokemon.getSpecies().getPokemonName())  == this.value);
	}


	private EnumSpecies randomPokeNoUb(){

		ArrayList<String> list = Lists.newArrayList(EnumSpecies.getNameList());
		EnumSpecies.ultrabeasts.forEach(list::remove);
		String pokemon = CollectionHelper.getRandomElement(list);
		EnumSpecies s = EnumSpecies.getFromName(pokemon).get();
		boolean isValid = false;
		while(!isValid) {
			if (!PixelmonConfig.isGenerationEnabled(s.getGeneration())) {
				isValid = false;
				pokemon = CollectionHelper.getRandomElement(list);
				s = EnumSpecies.getFromName(pokemon).get();
			}else {
				isValid = true;
			}
		}
		return s;
	}
}
