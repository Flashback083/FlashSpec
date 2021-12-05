package fr.flashback083.flashspec.Specs.GiveSpec.evospec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.EnumInitializeCategory;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class FirstEvoSpec extends SpecValue<Boolean> implements ISpecType
{
	public FirstEvoSpec(boolean value)
	{
		super("firstevo", value);
	}

	@Override
	public List<String> getKeys()
	{
		return Lists.newArrayList("firstevo","isfirstevo");
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
			return new FirstEvoSpec(true);
		
		try
		{
			return new FirstEvoSpec(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			return new FirstEvoSpec(true);
		}
	}

	@Override
	public SpecValue<?> readFromNBT(NBTTagCompound nbt)
	{
		return new FirstEvoSpec(nbt.getBoolean(this.key));
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
			pokemon.setSpecies(getEvo(),true);
			pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL);
			//spec = PokemonSpec.from(CollectionHelper.getRandomElement(species).getPokemonName());
			//Pokemon newpokemon = spec.create();
			//pokemon.readFromNBT(newpokemon.writeToNBT(new NBTTagCompound()));
		}else {
            pokemon.setSpecies(getEvo(),true);
			pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
			//spec = PokemonSpec.from(EnumSpecies.randomPoke(false).getPokemonName());
		}
		//spec.apply(pokemon);
		//pokemon.initialize(EnumInitializeCategory.INTRINSIC_FORCEFUL,EnumInitializeCategory.SPECIES);
	}

	@Override
	public SpecValue<Boolean> clone()
	{
		return new FirstEvoSpec(value);
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
        return pokemon.getBaseStats().preEvolutions.length == 0 == this.value;
	}

    public EnumSpecies getEvo(){
        EnumSpecies poke = EnumSpecies.randomPoke();
        if (this.value){
            while (!isLastEvo(poke)){
                poke = EnumSpecies.randomPoke();
            }
        }else{
            while (isLastEvo(poke)){
                poke = EnumSpecies.randomPoke();
            }
        }
        return poke;
    }

    public boolean isLastEvo(EnumSpecies enumSpecies){
        return enumSpecies.getBaseStats().preEvolutions.length == 0;
    }
}
