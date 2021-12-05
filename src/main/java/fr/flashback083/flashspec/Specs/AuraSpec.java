package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;

public class AuraSpec extends SpecValue<String> implements ISpecType {


        public AuraSpec(String value) {
                super("aura", value);
        }

        public List<String> getKeys() {
                return Collections.singletonList("aura");
        }

        public Class getSpecClass() {
                return getClass();
        }

        public Class getValueClass() {
                return String.class;
        }


        public AuraSpec parse(String arg) {
                return new AuraSpec(arg);
                }

        public String toParameterForm(SpecValue value) {
                return value.key + ":" + value.value.toString();
        }

        public SpecValue clone() {
                return new AuraSpec(this.value);
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
                return pokemon.getPersistentData().hasKey("entity-particles:particle") && pokemon.getPersistentData().getString("entity-particles:particle").equals(this.value);
        }


        public void apply(EntityPixelmon pixelmon) {
                pixelmon.getPokemonData().getPersistentData().setString("entity-particles:particle", this.value);
        }

        public void apply(Pokemon pokemon) {
                pokemon.getPersistentData().setString("entity-particles:particle", this.value);
        }

}