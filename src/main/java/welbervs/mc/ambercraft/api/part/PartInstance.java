package welbervs.mc.ambercraft.api.part;

import welbervs.mc.ambercraft.api.component.Component;
import welbervs.mc.ambercraft.api.component.ComponentType;

import java.util.List;

public final class PartInstance
{
    private final Component[] component;
    private final Part part;

    public PartInstance(Part part)
    {
        this.part = part;
        this.component = new Component[part.getComponentsType().size()];

        initializeComponents(part);
    }

    private void initializeComponents(Part part)
    {
        List<ComponentType<?>> componentsType = part.getComponentsType();
        for (int i = 0; i < componentsType.size(); i++)
            this.component[i] = componentsType.get(i).getDefaultInstance();
    }
}
