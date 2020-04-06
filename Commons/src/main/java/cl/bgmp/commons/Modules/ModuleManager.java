package cl.bgmp.commons.Modules;

public interface ModuleManager {

    void registerModules(Module... modules);

    void loadModules();

    Module getModule(ModuleId id);
}
