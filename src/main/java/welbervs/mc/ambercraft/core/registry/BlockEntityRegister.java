package welbervs.mc.ambercraft.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.core.part.PartContainerBlockEntity;

import java.util.Arrays;
import java.util.function.Supplier;


public class BlockEntityRegister
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Ambercraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PartContainerBlockEntity>> PART_CONTAINER_BLOCK_ENTITY
            = register("part_container", PartContainerBlockEntity::new, BlocksRegister.PART_CONTAINER_BLOCK);



    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String key, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block>... blocks )
    {
        return REGISTER.register(key, () -> BlockEntityType.Builder.of(
                factory,
                Arrays.stream(blocks).map(Supplier::get).toArray(Block[]::new)
        ).build(null));
    }
}
