package welbervs.mc.ambercraft.core.registry;

import net.neoforged.neoforge.registries.DeferredHolder;
import welbervs.mc.ambercraft.api.part.PartType;
import welbervs.mc.ambercraft.api.registry.DeferredPartTypeRegister;
import welbervs.mc.ambercraft.content.part_type.ResistorPartType;
import welbervs.mc.ambercraft.content.part_type.VoltageSourcePartType;
import welbervs.mc.ambercraft.core.Ambercraft;

/// Register all PartType used in the AmberCraft mod.
public class PartTypesRegister
{
    public static final DeferredPartTypeRegister REGISTER = new DeferredPartTypeRegister(Ambercraft.MODID);

    public static final DeferredHolder<PartType, ResistorPartType> RESISTOR_PART_TYPE = REGISTER.register("resistor_part_type", ResistorPartType::new);
    public static final DeferredHolder<PartType, VoltageSourcePartType> VOLTAGE_SOURCE_PART_TYPE = REGISTER.register("voltage_source_part_type", VoltageSourcePartType::new);
}
