package fr.flashback083.flashspec.Specs;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.api.pokemon.ISpecType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.flashback083.flashspec.FlashSpec.getServer;

public class OTSpec extends SpecValue implements ISpecType {
    private String value;
    private List keys;

    public OTSpec(List keys, String value) {
        super((String)keys.get(0), value);
        this.keys = keys;
        this.value = value;
    }

    public List getKeys() {
        return this.keys;
    }

    public Class getSpecClass() {
        return this.getClass();
    }

    public Class getValueClass() {
        return String.class;
    }

    public OTSpec parse(String arg) {
        return new OTSpec(this.keys, arg);
    }

    public String toParameterForm(SpecValue value) {
        return value.key + ":" + value.value.toString();
    }

    public SpecValue clone() {
        return new TextureSpec(this.keys, this.value);
    }

    public OTSpec readFromNBT(NBTTagCompound nbt) {
        return this.parse(nbt.getString("originalTrainer"));
    }

    public void writeToNBT(NBTTagCompound nbt, SpecValue value) {
        nbt.setString("originalTrainer", value.value.toString());
    }

    public boolean matches(EntityPixelmon pixelmon) {
        return this.matches(pixelmon.getPokemonData());
    }

    public boolean matches(Pokemon pokemon) {
        return pokemon.getOriginalTrainer().equalsIgnoreCase(this.value);
    }

    public boolean matches(NBTTagCompound nbt) {
        return nbt.getString("originalTrainer").equals(this.value);
    }

    public void apply(EntityPixelmon pixelmon) {
        this.apply(pixelmon.getPokemonData());
    }

    public void apply(Pokemon pokemon) {
        ArrayList<String> players = Lists.newArrayList(getServer().getOnlinePlayerNames());
        if (players.contains(this.value)){
            pokemon.setOriginalTrainer(getServer().getPlayerList().getPlayerByUsername(this.value).getUniqueID(), this.value);
        }else {
            if (getServer().isServerInOnlineMode()){
                Thread offthread = new Thread(() -> {
                    GameProfile gp = getServer().getPlayerProfileCache().getGameProfileForUsername(this.value);
                    getServer().addScheduledTask(() -> {
                        pokemon.setOriginalTrainer(gp.getId(), this.value);
                    });
                });
                offthread.start();
            }else {
                UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.value).getBytes());
                pokemon.setOriginalTrainer(uuid, this.value);
            }
        }
    }

    public void apply(NBTTagCompound nbt) {
        nbt.setString("originalTrainer", this.value);
    }


}

