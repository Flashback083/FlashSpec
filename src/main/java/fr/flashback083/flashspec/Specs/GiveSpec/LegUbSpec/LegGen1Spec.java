package fr.flashback083.flashspec.Specs.GiveSpec.LegUbSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.*;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import fr.flashback083.flashspec.FlashSpec;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

import static com.pixelmonmod.pixelmon.entities.pixelmon.specs.GenerationSpec.generationLists;

public class LegGen1Spec extends SpecValue<Boolean> implements ISpecType
{
	public LegGen1Spec(boolean value)
	{
		super("leg1", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("leg1");
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
			return new LegGen1Spec(true);
		
		try
		{
			return new LegGen1Spec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new LegGen1Spec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new LegGen1Spec(nbt.getBoolean(this.key));
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
			pokemon.setSpecies(EnumSpecies.getFromNameAnyCase(CollectionHelper.getRandomElement(FlashSpec.leg1)),true);
			pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
			//Pokemon newpokemon = spec.create();
			//pokemon.readFromNBT(newpokemon.writeToNBT(new NBTTagCompound()));
		}else {
			//List<EnumSpecies> gen1o = generationLists.get(1);
			//gen1o.remove(En umSpecies.getFromNameAnyCaseNoTranslate("random"));
			EnumSpecies gen1 = CollectionHelper.getRandomElement(generationLists.get(1));
			//System.out.println("raaa " + generationLists.get(1).size());
			while (gen1.isLegendary()){
				gen1 =  CollectionHelper.getRandomElement(generationLists.get(1));
			}
			pokemon.setSpecies(gen1,true);
			pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
		}
		//spec.apply(pokemon);
		//pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new LegGen1Spec(value);
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
