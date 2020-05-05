package cl.bgmp.commons.modules;

public interface ModuleManager {

  void registerModules(Module... modules);

  void loadModules();

  Module getModule(ModuleId id);
}
