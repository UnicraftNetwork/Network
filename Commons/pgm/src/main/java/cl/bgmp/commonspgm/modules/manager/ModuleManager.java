package cl.bgmp.commonspgm.modules.manager;

import cl.bgmp.commonspgm.modules.Module;
import cl.bgmp.commonspgm.modules.ModuleId;
import java.util.Optional;

public interface ModuleManager {

  void registerModules(Module... modules);

  void registerModule(Module module);

  void loadModules();

  void reloadModules();

  Optional<Module> getModule(ModuleId id);
}
