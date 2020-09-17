package cl.bgmp.commons.modules;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModuleManagerImpl implements ModuleManager {
  private Set<Module> modules = new HashSet<>();

  @Override
  public void registerModules(Module... modules) {
    this.modules.addAll(Arrays.asList(modules));
  }

  @Override
  public void loadModules() {
    this.modules.stream()
        .filter(Module::isEnabled)
        .collect(Collectors.toSet())
        .forEach(Module::load);
  }

  @Override
  public void reloadModules() {
    this.modules.forEach(Module::unload);
    this.loadModules();
  }

  @Override
  public Optional<Module> getModule(ModuleId id) {
    return this.modules.stream().filter(module -> module.getId().equals(id)).findFirst();
  }
}
