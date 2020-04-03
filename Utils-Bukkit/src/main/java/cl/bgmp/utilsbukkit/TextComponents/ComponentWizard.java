package cl.bgmp.utilsbukkit.TextComponents;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentWizard {
  private TextComponent textComponent;

  public ComponentWizard(final String text) {
    textComponent = new TextComponent(text);
  }

  public TextComponent getTextComponent() {
    return textComponent;
  }

  public ComponentWizard setHoverable(final String text) {
    textComponent.setHoverEvent(
        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
    return this;
  }

  public ComponentWizard setClickable(final String command) {
    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    return this;
  }

  public BaseComponent asBaseComponent() {
    return new ComponentBuilder().append(textComponent).create()[0];
  }

  public BaseComponent[] asBaseComponentArray() {
    return new ComponentBuilder().append(textComponent).create();
  }
}
