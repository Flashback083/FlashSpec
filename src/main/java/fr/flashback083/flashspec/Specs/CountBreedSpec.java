package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

public class CountBreedSpec extends SpecValue<String> implements ISpecType {

        public CountBreedSpec(String value) {
                super("countbreed", value);
        }

        public List<String> getKeys() {
                return Collections.singletonList("countbreed");
        }

        public Class getSpecClass() {
                return getClass();
        }

        public Class getValueClass() {
                return String.class;
        }


        public CountBreedSpec parse(String arg) {
                return new CountBreedSpec(arg);
                }

        public String toParameterForm(SpecValue value) {
                return value.key + ":" + value.value.toString();
        }

        public SpecValue clone() {
                return new CountBreedSpec(this.value);
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
                return pokemon.getPersistentData().hasKey("countbreed") && pokemon.getPersistentData().getInteger("countbreed") == Integer.parseInt(this.value);
        }


        public void apply(EntityPixelmon pixelmon) {
            apply(pixelmon.getPokemonData());
        }

        public void apply(Pokemon pokemon) {
            pokemon.getPersistentData().setInteger("countbreed", Integer.parseInt(this.value));
        }

}