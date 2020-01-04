package fr.flashback083.flashspec.Specs;

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NicknameSpec extends SpecValue implements ISpecType {

    public NicknameSpec(String value) {
        super("nickname", value);
    }

    public List getKeys() {
        return Arrays.asList("nickname");
    }

    public Class getSpecClass() {
        return this.getClass();
    }

    public Class getValueClass() {
        return String.class;
    }

    /*public NicknameSpec parse(String arg) {
        return new NicknameSpec(arg.replace("_", " ").replace("&", "§"));
    }*/

    public NicknameSpec parse(String arg) {
        return new NicknameSpec(arg.replace("_", " ").replace("&", "§"));
    }

    public String toParameterForm(SpecValue value) {
        return value.key + ":" + value.value.toString();
    }

    public SpecValue clone() {
        return new NicknameSpec((String)this.value);
    }

    public NicknameSpec readFromNBT(NBTTagCompound nbt) {
        return this.parse(nbt.getString("Nickname"));
    }

    public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
        nbt.setString("Nickname", value.value.toString());
    }

    public boolean matches(EntityPixelmon pokemon) {
        return pokemon.getNickname().equals(this.value);
    }

    public boolean matches(Pokemon pokemon) {
        return Objects.equals(pokemon.getNickname(), this.value);
    }

    public void apply(EntityPixelmon pokemon) {
        String nick = ((String)this.value).replace("%pokemon%", pokemon.getSpecies().name);
        pokemon.getPokemonData().setNickname(nick);
    }

    public void apply(Pokemon pokemon) {
        String nick = ((String)this.value).replace("%pokemon%", pokemon.getSpecies().name);
        pokemon.setNickname(nick);
    }
}

