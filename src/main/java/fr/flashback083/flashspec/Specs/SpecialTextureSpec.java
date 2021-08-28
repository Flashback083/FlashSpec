package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EnumSpecialTexture;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/*public class SpecialTextureSpec extends SpecValue<String> implements ISpecType {

        public String value;
        public List<String> keys;


        public SpecialTextureSpec(List<String> keys, String value) {
                super(keys.get(0), value);
                this.keys = keys;
                this.value = value;
                }


        public List<String> getKeys() {
                return this.keys;
                }

        public Class<? extends SpecValue<?>> getSpecClass() {
                return getClass();
        }

        public Class<String> getValueClass() {
                return String.class;
        }


        public SpecialTextureSpec parse(String arg) {
                return new SpecialTextureSpec(this.keys, arg);
                }

        public String toParameterForm(SpecValue<?> specValue) {
                return key + ":" + value;
        }

        public SpecValue clone() {
                return new SpecialTextureSpec(this.keys, this.value);
                }

        public SpecValue<?> readFromNBT(NBTTagCompound nbt) {
                return this.parse(String.valueOf(nbt.getInteger("specialTexture")));
         }

        public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
                nbt.setInteger("specialTexture", Integer.parseInt(this.value));
                }

        /*public boolean matches(EntityPixelmon pixelmon) {
                return pixelmon.getPokemonData().getSpecialTexture().id == Integer.parseInt(this.value);
        }

        public boolean matches(Pokemon pokemon) {
                return pokemon.getSpecialTexture().id == Integer.parseInt(this.value);
                }

        public boolean matches(NBTTagCompound nbt) {
                return nbt.getInteger("specialTexture") == Integer.parseInt(this.value);
                }

        public void apply(EntityPixelmon pixelmon) {
                pixelmon.getPokemonData().setSpecialTexture(EnumSpecialTexture.fromIndex(Integer.parseInt(this.value)));
        }

        public void apply(Pokemon pokemon) {
                pokemon.setSpecialTexture(EnumSpecialTexture.fromIndex(Integer.parseInt(this.value)));
                //pokemon.getPersistentData().setString("CustomTexture",this.value);
        }

        public void apply(NBTTagCompound nbt) {
                nbt.setInteger("specialTexture", Integer.parseInt(this.value));
                }
}*/