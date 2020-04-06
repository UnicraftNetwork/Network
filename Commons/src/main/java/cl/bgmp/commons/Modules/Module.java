package cl.bgmp.commons.Modules;

import org.bukkit.event.Listener;

public abstract class Module implements Listener {
    protected ModuleId id;
    protected boolean enabled;

    public Module(ModuleId id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public ModuleId getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public abstract void load();

    public abstract void unload();
}
