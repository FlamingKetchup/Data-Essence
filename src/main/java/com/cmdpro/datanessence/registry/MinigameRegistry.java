package com.cmdpro.datanessence.registry;

import com.cmdpro.datanessence.DataNEssence;
import com.cmdpro.datanessence.minigames.TestMinigameCreator;
import com.cmdpro.datanessence.screen.databank.MinigameSerializer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MinigameRegistry {
    public static final DeferredRegister<MinigameSerializer> MINIGAME_TYPES = DeferredRegister.create(new ResourceLocation(DataNEssence.MOD_ID, "minigames"), DataNEssence.MOD_ID);

    public static final Supplier<MinigameSerializer> TESTMINIGAME = register("test", () -> new TestMinigameCreator.TestMinigameSerializer());
    private static <T extends MinigameSerializer> Supplier<T> register(final String name, final Supplier<T> item) {
        return MINIGAME_TYPES.register(name, item);
    }
}
