package cl.bgmp.commons.Modules;

import org.bukkit.event.Listener;

public abstract class Module implements Listener {
    protected String id;
    protected boolean enabled;

    public Module(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public abstract void load();

    public abstract void unload();
}
