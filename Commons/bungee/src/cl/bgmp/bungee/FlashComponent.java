package cl.bgmp.bungee;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * A class to wrap up the main functionality of BungeeCord's {@link TextComponent}s You can build
 * and stylise components, while also being able to add Click & Hover events with no weird
 * side-effects.
 *
 * @author https://gist.github.com/BGMP/8e7e9af9654e0d7119290e3fb5869d46
 */
public class FlashComponent {
  private TextComponent init;
  private List<BaseComponent> queue = new ArrayList<>();

  public FlashComponent() {
    this.init = new TextComponent();
    queue.add(init);
  }

  public FlashComponent(String initialText) {
    this.init = new TextComponent(initialText);
    queue.add(init);
  }

  public FlashComponent(TextComponent initialText) {
    this.init = initialText;
    queue.add(init);
  }

  public ImmutableList<BaseComponent> getQueue() {
    return ImmutableList.copyOf(queue);
  }

  public FlashComponent color(ChatColor color) {
    getHeadFlash().setColor(color);
    return this;
  }

  public FlashComponent bold(boolean bold) {
    getHeadFlash().setBold(bold);
    return this;
  }

  public FlashComponent italic(boolean italic) {
    getHeadFlash().setItalic(italic);
    return this;
  }

  public FlashComponent strike(boolean strike) {
    getHeadFlash().setStrikethrough(strike);
    return this;
  }

  public FlashComponent magic(boolean magic) {
    getHeadFlash().setObfuscated(magic);
    return this;
  }

  public FlashComponent underline(boolean underline) {
    getHeadFlash().setUnderlined(underline);
    return this;
  }

  public FlashComponent hoverText(String text) {
    getHeadFlash()
        .setHoverEvent(
            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
    return this;
  }

  public FlashComponent hoverText(BaseComponent... component) {
    getHeadFlash().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component));
    return this;
  }

  public FlashComponent clickCommand(String command) {
    getHeadFlash().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
    return this;
  }

  public FlashComponent append(FlashComponent flashComponent) {
    queue.addAll(flashComponent.getQueue());
    return this;
  }

  public FlashComponent append(BaseComponent... baseComponents) {
    queue.addAll(Arrays.asList(baseComponents));
    return this;
  }

  public FlashComponent append(String string) {
    queue.add(new TextComponent(string));
    return this;
  }

  public FlashComponent append(TextComponent textComponent) {
    queue.add(textComponent);
    return this;
  }

  public BaseComponent[] build() {
    ComponentBuilder componentBuilder = new ComponentBuilder();
    for (BaseComponent baseComponent : queue)
      componentBuilder.append(baseComponent, ComponentBuilder.FormatRetention.NONE);
    return componentBuilder.create();
  }

  private BaseComponent getHeadFlash() {
    return queue.get(queue.size() - 1);
  }
}
