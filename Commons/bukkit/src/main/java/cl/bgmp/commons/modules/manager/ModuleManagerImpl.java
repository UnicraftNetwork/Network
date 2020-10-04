package cl.bgmp.commons.modules.manager;

import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ModuleManagerImpl implements ModuleManager {
  private Set<Module> modules = new HashSet<>();

  @Override
  public void registerModules(Module... modules) {
    this.modules.addAll(Arrays.asList(modules));
  }

  @Override
  public void registerModule(Module module) {
    this.modules.add(module);
  }

  @Override
  public void loadModules() {
    for (Module module : this.modules) {
      if (module.isEnabled()) {
        module.configure();
        module.load();
      }
    }
  }

  @Override
  public void reloadModules() {
    for (Module module : this.modules) {
      module.reload();
    }
  }

  @Override
  public Optional<Module> getModule(ModuleId id) {
    return this.modules.stream().filter(module -> module.getId().equals(id)).findFirst();
  }
}
