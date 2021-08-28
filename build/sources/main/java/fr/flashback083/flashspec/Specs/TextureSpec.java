package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class TextureSpec extends SpecValue<String> implements ISpecType {

        public String value;
        public List<String> keys;


        public TextureSpec(List<String> keys, String value) {
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


        public TextureSpec parse(String arg) {
                return new TextureSpec(this.keys, arg);
                }

        public String toParameterForm(SpecValue<?> specValue) {
                return key + ":" + value;
        }

        public SpecValue clone() {
                return new TextureSpec(this.keys, this.value);
                }

        public SpecValue<?> readFromNBT(NBTTagCompound nbt) {
                return this.parse(nbt.getString(this.key));
         }

        public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
                nbt.setString(this.key, (String) value.value);
        }

        public boolean matches(EntityPixelmon pixelmon) {
                return this.matches(pixelmon.getPokemonData());
        }

        public boolean matches(Pokemon pokemon) {
                if (this.value.equalsIgnoreCase("*")){
                        return !pokemon.getCustomTexture().isEmpty();
                }else {
                        return pokemon.getCustomTexture().equalsIgnoreCase(this.value);
                }
        }


        public void apply(EntityPixelmon pixelmon) {
                pixelmon.getPokemonData().setCustomTexture(this.value);
        }

        public void apply(Pokemon pokemon) {
                pokemon.setCustomTexture(this.value);
                //pokemon.getPersistentData().setString("CustomTexture",this.value);
        }

}