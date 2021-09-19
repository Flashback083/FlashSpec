package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HeldItemSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public HeldItemSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public HeldItemSpec parse(@Nullable String s) {
        return new HeldItemSpec(this.keys, s);
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
        ItemStack itemStack;
        if (this.value.equalsIgnoreCase("random")){
            itemStack = new ItemStack(CollectionHelper.getRandomElement(PixelmonItemsHeld.getHeldItemList()));
        }else{
            String item = "pixelmon:" + this.value;
            itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));
        }
        pokemon.setHeldItem(itemStack);
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        String item = "pixelmon:" + this.value;
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Item.getByNameOrId(item)));
        return pokemon.getHeldItem().isItemEqualIgnoreDurability(itemStack);
    }

    @Override
    public SpecValue<String> clone() {
        return new HeldItemSpec(this.keys,this.value);
    }
}
