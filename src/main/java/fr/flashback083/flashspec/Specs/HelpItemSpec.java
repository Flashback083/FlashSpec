package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class HelpItemSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public HelpItemSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public HelpItemSpec parse(@Nullable String s) {
        return new HelpItemSpec(this.keys, s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString("helditem"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString("helditem", this.value);
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
        String item = "pixelmon:" + this.value;
        ItemStack itemStack = new ItemStack(Item.getByNameOrId(item));
        entityPixelmon.getPokemonData().setHeldItem(itemStack);
    }

    @Override
    public void apply(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void apply(Pokemon pokemon) {
        String item = "pixelmon:" + this.value;
        ItemStack itemStack = new ItemStack(Item.getByNameOrId(item));
        pokemon.setHeldItem(itemStack);
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        String item = "pixelmon:" + this.value;
        ItemStack itemStack = new ItemStack(Item.getByNameOrId(item));
        return  entityPixelmon.getPokemonData().getHeldItem().isItemEqualIgnoreDurability(itemStack);
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
        // return nbtTagCompound.getTagList("Moveset",10).get(0).toString().equalsIgnoreCase("MoveID");
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        String item = "pixelmon:" + this.value;
        ItemStack itemStack = new ItemStack(Item.getByNameOrId(item));
        return pokemon.getHeldItem().isItemEqualIgnoreDurability(itemStack);
    }

    @Override
    public SpecValue<String> clone() {
        return new HelpItemSpec(this.keys,this.value);
    }
}
