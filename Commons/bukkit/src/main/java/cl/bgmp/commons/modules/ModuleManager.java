package cl.bgmp.commons.modules;

import java.util.Optional;

public interface ModuleManager {

  void registerModules(Module... modules);

  void loadModules();

  void reloadModules();

  Optional<Module> getModule(ModuleId id);
}
