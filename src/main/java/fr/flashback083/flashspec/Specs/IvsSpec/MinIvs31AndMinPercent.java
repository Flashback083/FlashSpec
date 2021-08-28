package fr.flashback083.flashspec.Specs.IvsSpec;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static fr.flashback083.flashspec.FlashSpec.getServer;

public class MinIvs31AndMinPercent extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public MinIvs31AndMinPercent(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public MinIvs31AndMinPercent parse(@Nullable String s) {
        return new MinIvs31AndMinPercent(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString(this.key, (String) specValue.value);
    }

    @Override
    public Class<? extends SpecValue<?>> getSpecClass() {
        return getClass();
    }

    @Override
    public String toParameterForm(SpecValue<?> specValue) {
        return key + ":" + value;
    }

    @Override
    public Class<String> getValueClass() {
        return String.class;
    }

    @Override
    public void apply(EntityPixelmon entityPixelmon) {
        this.apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {
        int ivs = Integer.parseInt(this.value.split("-")[0]);
        int percent = Integer.parseInt(this.value.split("-")[1]);
        if ((ivs >= 0 && ivs <= 6) && (percent >= 0 && percent <= 100)){
            if ((ivs == 0 && percent <=95) || (ivs == 1 && percent >=16 && percent <=96) ||  (ivs == 2 && percent >=33 && percent <=96) || (ivs == 3 && percent >=50 && percent <=97) || (ivs == 4 && percent >=66 && percent <=97) || (ivs == 5 && percent >=83 && percent <=98)){
                Thread offthread = new Thread(() -> {
                    // Code doing a lookup, from files, online or wherever.
                    // Secondary thread to prevent stalling.
                    int[] iv = calculeIvs(ivs,percent);
                    getServer().addScheduledTask(() -> {
                        // Code modifying an object
                        // scheduled on main thread
                        pokemon.getIVs().set(StatsType.HP, iv[0]);
                        pokemon.getIVs().set(StatsType.Attack, iv[1]);
                        pokemon.getIVs().set(StatsType.Defence, iv[2]);
                        pokemon.getIVs().set(StatsType.SpecialAttack, iv[3]);
                        pokemon.getIVs().set(StatsType.SpecialDefence, iv[4]);
                        pokemon.getIVs().set(StatsType.Speed, iv[5]);

                    });
                });
                offthread.start();
            }
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return false;
    }


    @Override
    public boolean matches(Pokemon pokemon) {
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new MinIvs31AndMinPercent(this.keys,this.value);
    }


    private int[] calculeIvs(int ivs, int percent){
        int[] iv = new int[6];

        int count = 0;

        for(ArrayList<Integer> slots = Lists.newArrayList(0, 1, 2, 3, 4, 5); !slots.isEmpty();) {
            int slot = slots.get(RandomHelper.getRandomNumberBetween(0, slots.size() - 1));
            int slottoremove = slots.indexOf(slot);
            if (count < ivs){
                iv[slot] = 31;
                count++;
            }else {
                iv[slot] = RandomHelper.getRandomNumberBetween(0, 31);
            }
            slots.remove(slottoremove);
        }

        while (getPercent(iv) <= percent){
            for(ArrayList<Integer> slots = Lists.newArrayList(0, 1, 2, 3, 4, 5); !slots.isEmpty();) {
                int slot = slots.get(RandomHelper.getRandomNumberBetween(0, slots.size() - 1));
                int slottoremove = slots.indexOf(slot);
                if (iv[slot] != 31){
                    iv[slot] = RandomHelper.getRandomNumberBetween(0, 31);
                }
                slots.remove(slottoremove);
            }
        }

        return iv;
    }

    private int getPercent(int[] ivs){
        return Math.round((float)(ivs[0] + ivs[1] + ivs[2] + ivs[3] + ivs[4] + ivs[5]) / 186.0F * 100.0F);
    }


}
