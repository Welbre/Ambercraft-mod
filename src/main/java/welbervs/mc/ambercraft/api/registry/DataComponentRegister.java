package welbervs.mc.ambercraft.api.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.core.Ambercraft;

/// Registra os DataComponent usado em itens
public class DataComponentRegister
{
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,Ambercraft.MODID);

    /// Used to flag that an item can spawn a part in the world, when try to use to item in a block.
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PartDataComponent>> PART_DATA_COMPONENT = REGISTER.registerComponentType(
            "part_data",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(PartDataComponent.DISK_CODEC)
                    // The codec to read/write the data across the network
                    .networkSynchronized(PartDataComponent.NETWORK_CODEC)
    );
}
