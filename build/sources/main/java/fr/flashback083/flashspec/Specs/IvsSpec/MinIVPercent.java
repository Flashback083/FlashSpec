package fr.flashback083.flashspec.Specs.IvsSpec;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;

public class MinIVPercent extends SpecValue implements ISpecType {
    public MinIVPercent(Integer value) {
        super("minimumiv%", value);
    }

    public List getKeys() {
        return Arrays.asList("minimumiv%", "miniv%", "minimumivs%", "minivs%");
    }

    public SpecValue parse(String arg) {
        try {
            int percentage = Integer.parseInt(arg);
            return percentage >= 0 && percentage <= 100 ? new MinIVPercent(percentage) : null;
        } catch (NumberFormatException var3) {
            return null;
        }
    }

    public SpecValue readFromNBT(NBTTagCompound nbt) {
        return new MinIVPercent(nbt.getInteger(this.key));
    }

    public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
        nbt.setByte(this.key, Byte.parseByte(value.value.toString()));
    }

    public Class getSpecClass() {
        return this.getClass();
    }

    public String toParameterForm(SpecValue value) {
        return "minimumiv%:" + value.value.toString();
    }

    public Class getValueClass() {
        return Integer.class;
    }

    public void apply(EntityPixelmon pixelmon) {
        this.apply(pixelmon.getPokemonData());
    }

    public void apply(Pokemon pokemon) {
        byte[] ivs = this.getIVsAbovePercentage((Integer)this.value);
        pokemon.getIVs().set(StatsType.HP, ivs[0]);
        pokemon.getIVs().set(StatsType.Attack, ivs[1]);
        pokemon.getIVs().set(StatsType.Defence, ivs[2]);
        pokemon.getIVs().set(StatsType.SpecialAttack, ivs[3]);
        pokemon.getIVs().set(StatsType.SpecialDefence, ivs[4]);
        pokemon.getIVs().set(StatsType.Speed, ivs[5]);
    }

    public void apply(NBTTagCompound nbt) {
        byte[] ivs = this.getIVsAbovePercentage((Integer)this.value);
        nbt.setByte("IVHP", ivs[0]);
        nbt.setByte("IVAttack", ivs[1]);
        nbt.setByte("IVDefence", ivs[2]);
        nbt.setByte("IVSpAtt", ivs[3]);
        nbt.setByte("IVSpDef", ivs[4]);
        nbt.setByte("IVSpeed", ivs[5]);
    }

    public boolean matches(EntityPixelmon pixelmon) {
        return this.matches(pixelmon.getPokemonData());
    }

    public boolean matches(Pokemon pokemon) {
        return this.areIVsAbovePercentage(new int[]{pokemon.getIVs().hp, pokemon.getIVs().attack, pokemon.getIVs().defence, pokemon.getIVs().specialAttack, pokemon.getIVs().specialDefence, pokemon.getIVs().speed}, (Integer)this.value);
    }

    public boolean matches(NBTTagCompound nbt) {
        return this.areIVsAbovePercentage(new int[]{nbt.getByte("IVHP"), nbt.getByte("IVAttack"), nbt.getByte("IVDefence"), nbt.getByte("IVSpAtt"), nbt.getByte("IVSpDef"), nbt.getByte("IVSpeed")}, (Integer)this.value);
    }

    public SpecValue clone() {
        return new MinIVPercent((Integer)this.value);
    }

    private byte[] getIVsAbovePercentage(int percentage) {
        byte[] ivs = new byte[]{0, 0, 0, 0, 0, 0};
        if (percentage == 100) {
            ivs = new byte[]{31, 31, 31, 31, 31, 31};
            return ivs;
        } else {
            int minTotal = Math.round(186.0F * ((float)percentage / 100.0F));
            int chosenTotal = RandomHelper.getRandomNumberBetween(minTotal, 186);

            int addable;
            for(int total = 0; total < chosenTotal; total += addable) {
                int jump = RandomHelper.getRandomNumberBetween(1, 4);

                int slot;
                while(ivs[slot = this.randomSlot()] >= 31) {
                }

                addable = Math.min(31 - ivs[slot], jump);
                ivs[slot] = (byte)(ivs[slot] + addable);
            }

            return ivs;
        }
    }

    private int randomSlot() {
        return RandomHelper.getRandomNumberBetween(0, 5);
    }

    private boolean areIVsAbovePercentage(int[] ivs, int percentage) {
        return Math.round((float)(ivs[0] + ivs[1] + ivs[2] + ivs[3] + ivs[4] + ivs[5]) / 186.0F * 100.0F) >= percentage;
    }
}

