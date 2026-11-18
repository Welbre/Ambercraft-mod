package welbervs.mc.ambercraft.api.network;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import welbervs.mc.ambercraft.core.Ambercraft;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The physical domain of a {@link NetworkComponent} (electrical, fluid, thermal...).
 * <p>
 * Domains are interned: {@link #of(ResourceLocation)} always returns the same instance for the same id, so they can be
 * compared with {@code ==}. Other mods can create their own domains just by calling {@link #of(ResourceLocation)}.
 */
public final class NetworkDomain
{
    private static final Map<ResourceLocation, NetworkDomain> DOMAINS = new ConcurrentHashMap<>();

    public static final NetworkDomain ELECTRICAL = of(Ambercraft.MODID, "electrical");
    public static final NetworkDomain FLUID = of(Ambercraft.MODID, "fluid");
    public static final NetworkDomain THERMAL = of(Ambercraft.MODID, "thermal");
    public static final NetworkDomain MECHANICAL = of(Ambercraft.MODID, "mechanical");

    private final ResourceLocation id;

    private NetworkDomain(ResourceLocation id)
    {
        this.id = id;
    }

    /// Returns the (unique) domain with this id, creating it if needed.
    public static NetworkDomain of(@NotNull ResourceLocation id)
    {
        return DOMAINS.computeIfAbsent(id, NetworkDomain::new);
    }

    public static NetworkDomain of(String namespace, String path)
    {
        return of(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return id.toString();
    }
}
