package fr.flashback083.flashspec.Specs.MoveSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nullable;
import java.util.List;

public class Move2Spec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public Move2Spec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }



    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public Move2Spec parse(@Nullable String s) {
        return new Move2Spec(this.keys, s.replaceAll("_"," "));
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        String atk = ((String) specValue.value).replaceAll("_", " ");
        if (atk.equalsIgnoreCase("None")){
            nbtTagCompound.setString(this.key, ((String) specValue.value));
            return;
        }
        boolean isattack = Attack.hasAttack(atk);
        if (isattack){
            nbtTagCompound.setString(this.key, ((String) specValue.value).replaceAll("_"," "));
        }
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
        String atk = this.value.replaceAll("_", " ");
        if (atk.equalsIgnoreCase("None")){
            pokemon.getMoveset().remove(1);
            return;
        }
        boolean isattack = Attack.hasAttack(atk);
        if (isattack){
            pokemon.getMoveset().set(1, new Attack(atk));
            if(!pokemon.getPersistentData().hasKey("flashspec")){
                pokemon.getPersistentData().setTag("flashspec",new NBTTagList());
            }
            pokemon.getPersistentData().getTagList("flashspec",8).appendTag(new NBTTagString(this.key+":"+this.value));
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  entityPixelmon.getPokemonData().getMoveset().get(1).toString().equalsIgnoreCase(this.value.replaceAll("_"," "));
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getMoveset().get(1).toString().equalsIgnoreCase(this.value.replaceAll("_"," "));
    }

    @Override
    public SpecValue<String> clone() {
        return new Move2Spec(this.keys,this.value.replaceAll("_"," "));
    }
}
