package cl.bgmp.commonspgm.modules;

public enum ModuleId {
  NAVIGATOR("navigator-module"),
  TIPS("tips-module");

  private String toString;

  ModuleId(String toString) {
    this.toString = toString;
  }

  @Override
  public String toString() {
    return toString;
  }
}
