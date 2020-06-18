package cl.bgmp.commons.modules;

public interface ModuleManager {

  void registerModules(Module... modules);

  void loadModules();

  void reloadModules();

  Module getModule(ModuleId id);
}
