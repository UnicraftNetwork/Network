package cl.bgmp.utilsbukkit.textcomponents;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ComponentSender {

  public static void sendComponent(final TextComponent textComponent, final Player to) {
    to.spigot().sendMessage(textComponent);
  }

  public static void sendComponent(final BaseComponent baseComponent, final Player to) {
    to.spigot().sendMessage(baseComponent);
  }

  public static void sendComponent(final BaseComponent[] baseComponents, final Player to) {
    to.spigot().sendMessage(baseComponents);
  }
}
