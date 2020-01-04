package fr.flashback083.flashspec.Specs.MoveSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class Move1Spec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public Move1Spec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public Move1Spec parse(@Nullable String s) {
        return new Move1Spec(this.keys, s.replaceAll("_"," "));
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString("move1"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString("move1", this.value.replaceAll("_"," "));
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
        String atk = this.value.replaceAll("_", " ");
        boolean isattack = Attack.hasAttack(atk);
        if (isattack){
            entityPixelmon.getPokemonData().getMoveset().set(0, new Attack(atk));
        }
    }

    @Override
    public void apply(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public void apply(Pokemon pokemon) {
        String atk = this.value.replaceAll("_", " ");
        boolean isattack = Attack.hasAttack(atk);
        if (isattack){
            pokemon.getMoveset().set(0, new Attack(atk));
        }


    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  entityPixelmon.getPokemonData().getMoveset().get(0).toString().equalsIgnoreCase(this.value.replaceAll("_"," "));
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
        // return nbtTagCompound.getTagList("Moveset",10).get(0).toString().equalsIgnoreCase("MoveID");
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return pokemon.getMoveset().get(0).toString().equalsIgnoreCase(this.value.replaceAll("_"," "));
    }

    @Override
    public SpecValue<String> clone() {
        return new Move1Spec(this.keys,this.value.replaceAll("_"," "));
    }
}
