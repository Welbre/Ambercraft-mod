package welbervs.mc.ambercraft.core.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.api.part.PartDataComponent;
import welbervs.mc.ambercraft.core.Ambercraft;

public class DataComponentRegister
{
    public static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,Ambercraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PartDataComponent>> PART_DATA_COMPONENT = REGISTER.registerComponentType(
            "part_data",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(PartDataComponent.DISK_CODEC)
                    // The codec to read/write the data across the network
                    .networkSynchronized(PartDataComponent.NETWORK_CODEC)
    );
}
