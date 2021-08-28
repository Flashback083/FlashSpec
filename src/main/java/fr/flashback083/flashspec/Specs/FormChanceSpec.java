package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class FormChanceSpec extends SpecValue<String> implements ISpecType {

    //private String value;
    //private List<String> keys;

    public FormChanceSpec(String value){
        /*super(keys.get(0), value);
        this.keys = keys;
        this.value = value;*/
        super("formchance", value);
    }


    @Override
    public List<String> getKeys() {
        return Lists.newArrayList("formchance");
    }

    @Override
    public FormChanceSpec parse(@Nullable String s) {
        return new FormChanceSpec(s);
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
            if (!setForm(pokemon,this.value.split(";")[0])){
                System.out.println("no form found");
            }
        }
    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
        return  matches(entityPixelmon.getPokemonData());
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return  pokemon.getFormEnum().getUnlocalizedName().equalsIgnoreCase(this.value.split(";")[0]);
    }

    @Override
    public SpecValue<String> clone() {
        return new FormChanceSpec(this.value);
    }

    /*private IEnumForm getForm(Pokemon pokemon,String formname){
        final IEnumForm[] form = new IEnumForm[1];
        pokemon.getSpecies().getPossibleForms(true).forEach(iEnumForm -> {
            if (iEnumForm.getLocalizedName().equalsIgnoreCase(formname)){
                form[0] = iEnumForm;
            }
        });
        return form[0];
    }*/

    private boolean setForm(Pokemon pokemon,String formname){
        AtomicBoolean work = new AtomicBoolean(false);
        pokemon.getSpecies().getPossibleForms(true).forEach(iEnumForm -> {
            //System.out.println("Form found2 : " + iEnumForm.getLocalizedName());
            if (iEnumForm.getLocalizedName().equalsIgnoreCase(formname)){
                pokemon.setForm(iEnumForm);
                work.set(true);
            }
        });
        return work.get();
    }
}
