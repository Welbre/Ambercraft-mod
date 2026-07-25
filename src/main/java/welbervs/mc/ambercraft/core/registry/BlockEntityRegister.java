package welbervs.mc.ambercraft.core.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import welbervs.mc.ambercraft.core.Ambercraft;
import welbervs.mc.ambercraft.ipart.IPartContainerBlockEntity;


public class BlockEntityRegister
{
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Ambercraft.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IPartContainerBlockEntity>> IPART_CONTAINER_BLOCK_ENTITY
            = register("ipart_container", IPartContainerBlockEntity::new, BlocksRegister.IPART_CONTAINER_BLOCK.get());



    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String key, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks )
    {
        return REGISTER.register(key, () -> BlockEntityType.Builder.of(
                factory,
                blocks
        ).build(null));
    }
}
