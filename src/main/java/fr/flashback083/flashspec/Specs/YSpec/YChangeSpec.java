package fr.flashback083.flashspec.Specs.YSpec;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.List;

public class YChangeSpec extends SpecValue<String> implements ISpecType {

    private String value;
    private List<String> keys;

    public YChangeSpec(List<String> keys, String value){
        super(keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }


    @Override
    public List<String> getKeys() {
        return this.keys;
    }

    @Override
    public YChangeSpec parse(@Nullable String s) {
        return new YChangeSpec(this.keys, s);
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
        if (this.value.startsWith("+")){
           int y = Integer.parseInt(this.value.replace("+",""));
            entityPixelmon.setPositionAndUpdate(entityPixelmon.getPosition().getX(),entityPixelmon.getPosition().getY()+y,entityPixelmon.getPosition().getZ());
        }else if (this.value.startsWith("-")){
            int y = Integer.parseInt(this.value.replace("-",""));
            if (entityPixelmon.getPosition().getY() - y <= 1){
                entityPixelmon.setPositionAndUpdate(entityPixelmon.getPosition().getX(),2,entityPixelmon.getPosition().getZ());
            }else {
                entityPixelmon.setPositionAndUpdate(entityPixelmon.getPosition().getX(),entityPixelmon.getPosition().getY()+y,entityPixelmon.getPosition().getZ());
            }
        }
    }

    @Override
    public void apply(Pokemon pokemon) {

    }

    @Override
    public boolean matches(EntityPixelmon entityPixelmon) {
       return false;
    }

    @Override
    public boolean matches(NBTTagCompound nbtTagCompound) {
        return false;
    }

    @Override
    public boolean matches(Pokemon pokemon) {
        return false;
    }

    @Override
    public SpecValue<String> clone() {
        return new YChangeSpec(this.keys,this.value);
    }
}
