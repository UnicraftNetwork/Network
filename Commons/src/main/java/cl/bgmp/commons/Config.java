package cl.bgmp.commons;

import cl.bgmp.commons.Navigator.ServerButton;
import cl.bgmp.utilsbukkit.Channels;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Items;
import cl.bgmp.utilsbukkit.Server;
import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {
  private static Configuration getConfig() {
    Commons commons = Commons.get();
    if (commons != null) return commons.getConfig();
    else return new YamlConfiguration();
  }

  private static final String navigatorPath = "navigator";
  private static final String navigatorEnabledPath = navigatorPath + ".enabled";
  private static final String navigatorTitlePath = navigatorPath + ".title";
  private static final String navigatorSizePath = navigatorPath + ".size";
  private static final String navigatorServersPath = navigatorPath + ".servers";

  public static class Navigator {
    public static String getTitle() {
      return getConfig().getString(navigatorTitlePath);
    }

    public static int getSize() {
      return getConfig().getInt(navigatorSizePath);
    }

    public static boolean isEnabled() {
      return getConfig().getBoolean(navigatorEnabledPath);
    }

    public static Set<ServerButton> getServerButtons() {
      final Set<String> keys =
          Objects.requireNonNull(getConfig().getConfigurationSection(navigatorServersPath))
              .getKeys(false);
      Set<ServerButton> serverButtons = new HashSet<>();

      for (String key : keys) {
        String serverName = getConfig().getString(navigatorServersPath + "." + key + ".name");
        String serverIp = getConfig().getString(navigatorServersPath + "." + key + ".ip");
        int serverPort = getConfig().getInt(navigatorServersPath + "." + key + ".port");

        String materialString =
            getConfig().getString(navigatorServersPath + "." + key + ".item.material");
        String title = getConfig().getString(navigatorServersPath + "." + key + ".item.title");
        List<String> lore =
            getConfig().getStringList(navigatorServersPath + "." + key + ".item.lore");

        int slot = Integer.parseInt(key);

        ItemStack item =
            Items.titledItemStackWithLore(
                Material.valueOf(materialString),
                Chat.colourify(title),
                lore.stream()
                    .map(
                        line ->
                            Chat.colourify(
                                line.replaceAll(
                                    "%player_count%",
                                    Channels.getPrettyServerPlayerCount(serverIp, serverPort))))
                    .collect(Collectors.toList()));

        serverButtons.add(
            new ServerButton(new Server(serverName, serverIp, serverPort), item, slot));
      }

      return ImmutableSet.copyOf(serverButtons);
    }
  }
}
