package welbervs.mc.ambercraft.api.part;

import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.List;

public final class Part
{
    private final Component[] component;
    private final PartType partType;

    public Part(PartType partType)
    {
        this.partType = partType;
        this.component = new Component[partType.getComponentsType().size()];

        initializeComponents(partType);
    }

    private void initializeComponents(PartType partType)
    {
        List<ComponentType<?>> componentsType = partType.getComponentsType();
        for (int i = 0; i < componentsType.size(); i++)
            this.component[i] = componentsType.get(i).getDefaultInstance();
    }
}
