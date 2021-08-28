package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomTextureChanceSpec extends SpecValue<String> implements ISpecType {

    //private String value;
    //private List<String> keys;

    public CustomTextureChanceSpec(String value){
        /*super(keys.get(0), value);
        this.keys = keys;
        this.value = value;*/
        super("customtexturechance", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("ctchance","customtexturechance");
    }

    @Override
    public CustomTextureChanceSpec parse(@Nullable String s) {
        return new CustomTextureChanceSpec(s);
    }

    @Override
    public SpecValue<?> readFromNBT(NBTTagCompound nbtTagCompound) {
        return parse(nbtTagCompound.getString(this.key));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound, SpecValue<?> specValue) {
        nbtTagCompound.setString(this.key, specValue.value.toString());
    }

    public Class getSpecClass() {
        return this.getClass();
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
        int chance = Integer.parseInt(this.value.split(";")[1]);
        if (chance == 0){
            //System.out.println("[FlashSpec] Chance == 0");
            return;
        }
        int random =  new Random().nextInt(chance)+1;
        boolean choose = random == chance;
        if (choose){
            //System.out.println("[FlashSpec] Spawn with texture !");
            pokemon.setCustomTexture(this.value.split(";")[0]);
        }/*else{
            System.out.println("[FlashSpec] Spawn without the texture !");
        }*/
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        if (pokemon.getCustomTexture().length()>0){
            return pokemon.getCustomTexture().equalsIgnoreCase(this.value.split(";")[0]);
        }
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new CustomTextureChanceSpec(this.value);
    }
}
