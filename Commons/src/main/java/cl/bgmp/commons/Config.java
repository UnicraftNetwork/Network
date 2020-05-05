package cl.bgmp.commons;

import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.navigator.ServerButton;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Server;
import cl.bgmp.utilsbukkit.Validate;
import cl.bgmp.utilsbukkit.items.Items;
import cl.bgmp.utilsbukkit.timeutils.Time;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

// TODO: Reduce boilerplate validation methods
public class Config {
  private static Configuration getConfig() {
    Commons commons = Commons.get();
    if (commons != null) return commons.getConfig();
    else return new YamlConfiguration();
  }

  public static void reload() {
    Commons.get().reloadConfig();
  }

  public static class Tools {
    private static final String onJoinToolsPath = "onjoin-tools";

    private static final boolean defaultOnJoinToolsState = false;

    public static boolean areEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(onJoinToolsPath)))
        return defaultOnJoinToolsState;
      else return getConfig().getBoolean(onJoinToolsPath);
    }
  }

  public static class Navigator {
    private static final String navigatorPath = "navigator";
    private static final String navigatorEnabledPath = navigatorPath + ".enabled";
    private static final String navigatorTitlePath = navigatorPath + ".title";
    private static final String navigatorSizePath = navigatorPath + ".size";
    private static final String navigatorServersPath = navigatorPath + ".servers";

    private static final String defaultTitle =
        ChatColor.BLUE.toString() + ChatColor.BOLD + "Navigator";
    private static final int defaultSize = 27;
    private static final boolean defaultEnabledState = false;

    public static String getTitle() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(navigatorTitlePath)))
        return defaultTitle;
      else return getConfig().getString(navigatorTitlePath);
    }

    public static int getSize() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(navigatorSizePath)))
        return defaultSize;
      else return getConfig().getInt(navigatorSizePath);
    }

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(navigatorEnabledPath)))
        return defaultEnabledState;
      else return getConfig().getBoolean(navigatorEnabledPath);
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
                lore.stream().map(Chat::colourify).collect(Collectors.toList()));

        serverButtons.add(
            new ServerButton(new Server(serverName, serverIp, serverPort), item, slot));
      }

      return ImmutableSet.copyOf(serverButtons);
    }
  }

  public static class ChatFormat {
    private static final String chatPath = "chat";
    private static final String vaultFormattingPath = chatPath + ".vault-formatting";
    private static final String vaultFormattingEnabledPath = vaultFormattingPath + ".enabled";
    private static final String vaultFormattingFormatPath = vaultFormattingPath + ".format";

    private static final boolean defaultEnabledState = false;
    private static final String defaultFormat = ChatFormatModule.DEFAULT_FORMAT;

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(vaultFormattingEnabledPath)))
        return defaultEnabledState;
      else return getConfig().getBoolean(vaultFormattingEnabledPath);
    }

    public static String getFormat() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(vaultFormattingFormatPath)))
        return defaultFormat;
      else return getConfig().getString(vaultFormattingFormatPath);
    }
  }

  public static class ForceGamemode {
    private static final String forceGamemodePath = "force-gamemode";
    private static final String forceGamemodeEnabledPath = forceGamemodePath + ".enabled";
    private static final String forceGamemodeExemptPermissionPath =
        forceGamemodePath + ".exempt-permission";
    private static final String forcedGamemodePath = forceGamemodePath + ".gamemode";

    private static final boolean defaultForceState = false;
    private static final String defaultPermission = "commons.defaultgamemode.exempt";
    private static final GameMode defaultGamemode = GameMode.SURVIVAL;

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(forceGamemodeEnabledPath)))
        return defaultForceState;
      else return getConfig().getBoolean(forceGamemodeEnabledPath);
    }

    public static String getGamemodeForceExemptPermission() {
      if (!Validate.pathsAreValid(
          getConfig().getConfigurationSection(forceGamemodeExemptPermissionPath)))
        return defaultPermission;
      else return getConfig().getString(forceGamemodeExemptPermissionPath);
    }

    public static GameMode getGamemode() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(forcedGamemodePath)))
        return defaultGamemode;
      else {
        final String gamemodeString = getConfig().getString(forcedGamemodePath);
        assert gamemodeString != null;

        if (gamemodeString.equals("0") || gamemodeString.equalsIgnoreCase("survival"))
          return GameMode.SURVIVAL;
        if (gamemodeString.equals("1") || gamemodeString.equalsIgnoreCase("creative"))
          return GameMode.CREATIVE;
        if (gamemodeString.equals("2") || gamemodeString.equalsIgnoreCase("adventure"))
          return GameMode.ADVENTURE;
        if (gamemodeString.equals("3") || gamemodeString.equalsIgnoreCase("spectator"))
          return GameMode.SPECTATOR;

        Commons.get()
            .getLogger()
            .warning("Unable to parse default gamemode. Check your config.yml.");
        Commons.get().getLogger().warning("Default gamemode was default to SURVIVAL.");
        return GameMode.SURVIVAL;
      }
    }
  }

  public static class Weather {
    private static final String weatherPath = "weather";
    private static final String weatherEnabledPath = weatherPath + ".disabled";

    private static final boolean defaultWeatherState = false;

    public static boolean isDisabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(weatherEnabledPath)))
        return defaultWeatherState;
      else return getConfig().getBoolean(weatherEnabledPath);
    }
  }

  public static class JoinQuitMessages {
    private static final String joinQuitMessagesPath = "joinquit-messages";
    private static final String joinQuitMessagesEnabledPath = joinQuitMessagesPath + ".enabled";
    private static final String joinQuitMessageJoinPath = joinQuitMessagesPath + ".join";
    private static final String joinQuitMessageQuitPath = joinQuitMessagesPath + ".quit";

    private static final boolean joinQuitMessagesDefaultState = false;
    private static final String joinQuitMessagesDefaultJoin = "<%player%> joined the game";
    private static final String joinQuitMessagesDefaultQuit = "<%player%> left the game";

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(joinQuitMessagesEnabledPath)))
        return joinQuitMessagesDefaultState;
      else return getConfig().getBoolean(joinQuitMessagesEnabledPath);
    }

    public static String getJoinMessage() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(joinQuitMessageJoinPath)))
        return joinQuitMessagesDefaultJoin;
      else return getConfig().getString(joinQuitMessageJoinPath);
    }

    public static String getQuitMessage() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(joinQuitMessageQuitPath)))
        return joinQuitMessagesDefaultQuit;
      else return getConfig().getString(joinQuitMessageQuitPath);
    }
  }

  public static class Lobby {
    private static final String lobbyPath = "lobby";

    private static final String defaultLobby = "Lobby";

    public static String getLobbyServerName() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(lobbyPath)))
        return defaultLobby;
      else return getConfig().getString(lobbyPath);
    }
  }

  public static class Restart {
    private static final String restartPath = "restart";
    private static final String enabledPath = restartPath + ".enabled";
    private static final String intervalPath = restartPath + ".interval";

    private static final boolean defaultEnabled = true;
    private static final String defaultInterval = "24h";

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(restartPath)))
        return defaultEnabled;
      else return getConfig().getBoolean(enabledPath);
    }

    public static Time getInterval() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(intervalPath)))
        return Time.fromString(defaultInterval);
      else return Time.fromString(Objects.requireNonNull(getConfig().getString(intervalPath)));
    }
  }

  public static class Tips {
    private static final String tipsPath = "tips";
    private static final String enabledPath = tipsPath + ".enabled";
    private static final String intervalPath = tipsPath + ".interval";
    private static final String prefixPath = tipsPath + ".prefix";
    private static final String messagesPath = tipsPath + ".messages";

    private static final boolean defaultEnabled = true;
    private static final String defaultInterval = "7m";
    private static final String defaultPrefix = "[Tip] ";
    private static final List<String> defaultMessages =
        new ArrayList<String>() {
          {
            add("Check your configuration! something might've gone wrong!");
          }
        };

    public static boolean isEnabled() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(enabledPath))) {
        return defaultEnabled;
      } else return getConfig().getBoolean(enabledPath);
    }

    public static Time getInterval() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(intervalPath))) {
        return Time.fromString(defaultInterval);
      } else return Time.fromString(Objects.requireNonNull(getConfig().getString(intervalPath)));
    }

    public static String getPrefix() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(prefixPath))) {
        return defaultPrefix;
      } else return Chat.colourify(getConfig().getString(prefixPath));
    }

    public static ImmutableList<String> getMessages() {
      if (!Validate.pathsAreValid(getConfig().getConfigurationSection(messagesPath))) {
        return ImmutableList.copyOf(defaultMessages);
      } else return ImmutableList.copyOf(getConfig().getStringList(messagesPath));
    }
  }
}
