package fr.flashback083.flashspec.Specs.IvsSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinNumIVs extends SpecValue implements ISpecType {
    public MinNumIVs(Integer value) {
        super("minnumivs", value);
    }

    public List getKeys() {
        return Arrays.asList("minnumivs");
    }

    public SpecValue parse(String arg) {
        try {
            int count = Integer.parseInt(arg);
            return count >= 0 && count <= 6 ? new MinNumIVs(count) : null;
        } catch (NumberFormatException var3) {
            return null;
        }
    }

    public SpecValue readFromNBT(NBTTagCompound nbt) {
        return new MinNumIVs(nbt.getInteger(this.key));
    }

    public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
        nbt.setByte(this.key, Byte.parseByte(value.value.toString()));
    }

    public Class getSpecClass() {
        return MinNumIVs.class;
    }

    public String toParameterForm(SpecValue value) {
        return "minnumivs:" + value.value.toString();
    }

    public Class getValueClass() {
        return Integer.class;
    }

    public void apply(EntityPixelmon pixelmon) {
        this.apply(pixelmon.getPokemonData());
    }

    public void apply(Pokemon pokemon) {
        int[] ivs = this.getIVsWithMax31((Integer)this.value);
        pokemon.getIVs().set(StatsType.HP, ivs[0]);
        pokemon.getIVs().set(StatsType.Attack, ivs[1]);
        pokemon.getIVs().set(StatsType.Defence, ivs[2]);
        pokemon.getIVs().set(StatsType.SpecialAttack, ivs[3]);
        pokemon.getIVs().set(StatsType.SpecialDefence, ivs[4]);
        pokemon.getIVs().set(StatsType.Speed, ivs[5]);
        if(!pokemon.getPersistentData().hasKey("flashspec")){
            pokemon.getPersistentData().setTag("flashspec",new NBTTagList());
        }
        pokemon.getPersistentData().getTagList("flashspec",8).appendTag(new NBTTagString(this.key+":"+this.value));
    }

    public void apply(NBTTagCompound nbt) {
        int[] ivs = this.getIVsWithMax31((Integer)this.value);
        nbt.setByte("IVHP", (byte)ivs[0]);
        nbt.setByte("IVAttack", (byte)ivs[1]);
        nbt.setByte("IVDefence", (byte)ivs[2]);
        nbt.setByte("IVSpAtt", (byte)ivs[3]);
        nbt.setByte("IVSpDef", (byte)ivs[4]);
        nbt.setByte("IVSpeed", (byte)ivs[5]);
    }

    public boolean matches(EntityPixelmon pixelmon) {
        return this.matches(pixelmon.getPokemonData());
    }

    public boolean matches(Pokemon pokemon) {
        return this.count31s(new int[]{pokemon.getIVs().hp, pokemon.getIVs().attack, pokemon.getIVs().defence, pokemon.getIVs().specialAttack, pokemon.getIVs().specialDefence, pokemon.getIVs().speed}) == (Integer)this.value;
    }

    public boolean matches(NBTTagCompound nbt) {
        return this.count31s(new int[]{nbt.getByte("IVHP"), nbt.getByte("IVAttack"), nbt.getByte("IVDefence"), nbt.getByte("IVSpAtt"), nbt.getByte("IVSpDef"), nbt.getByte("IVSpeed")}) == (Integer)this.value;
    }

    public SpecValue clone() {
        return new MinNumIVs((Integer)this.value);
    }

    private int[] getIVsWithMax31(int num) {

        int[] ivs = new int[6];
        int count = 0;

        for(ArrayList slots = Lists.newArrayList(0, 1, 2, 3, 4, 5); !slots.isEmpty();) {
            int slot = (Integer)slots.get(RandomHelper.getRandomNumberBetween(0, slots.size() - 1));
            int slottoremove = slots.indexOf(slot);
            if (count < num){
                ivs[slot] = 31;
                count++;
            }else {
                ivs[slot] = RandomHelper.getRandomNumberBetween(0, 31);
            }
            slots.remove(slottoremove);
        }

        return ivs;
    }

    private int count31s(int[] ivs) {
        int count = 0;
        int[] var3 = ivs;
        int var4 = ivs.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int iv = var3[var5];
            if (iv == 31) {
                ++count;
            }
        }

        return count;
    }
}
