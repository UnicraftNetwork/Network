package cl.bgmp.commons.modules.manager;

import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import java.util.Optional;

public interface ModuleManager {

  void registerModules(Module... modules);

  void registerModule(Module module);

  void loadModules();

  void reloadModules();

  Optional<Module> getModule(ModuleId id);
}
