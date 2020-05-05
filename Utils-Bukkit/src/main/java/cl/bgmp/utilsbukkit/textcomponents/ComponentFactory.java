package cl.bgmp.utilsbukkit.textcomponents;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentFactory {

  public static BaseComponent[] squashComponents(final TextComponent... textComponents) {
    final ComponentBuilder componentBuilder = new ComponentBuilder();
    for (final TextComponent textComponent : textComponents) componentBuilder.append(textComponent);
    return componentBuilder.create();
  }
}
