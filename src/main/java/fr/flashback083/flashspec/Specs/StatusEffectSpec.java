package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.status.*;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class StatusEffectSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public StatusEffectSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public StatusEffectSpec parse(@Nullable String s) {
        return new StatusEffectSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue specValue) {
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
        apply(entityPixelmon.getPokemonData());
    }


    @Override
    public void apply(Pokemon pokemon) {
        if (!StatusType.isStatusEffect(this.value)){
            return;
        }
        pokemon.setStatus(getStatusFromType(this.value));
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        if (!StatusType.isStatusEffect(this.value)){
            return false;
        }
        return pokemon.getStatus().type.isStatus(StatusType.getStatusEffect(this.value));
    }

    @Override
    public SpecValue<String> clone() {
        return new StatusEffectSpec(this.keys,this.value);
    }

    public StatusPersist getStatusFromType(String statusType){
        switch (StatusType.getStatusEffect(statusType)){
            case Poison:
                return new Poison();
            case Burn:
                return new Burn();
            case Freeze:
                return new Freeze();
            case Paralysis:
                return new Paralysis();
            case PoisonBadly:
                return new PoisonBadly();
            case Sleep:
                return new Sleep();
            default: return new NoStatus();
        }
    }


}
