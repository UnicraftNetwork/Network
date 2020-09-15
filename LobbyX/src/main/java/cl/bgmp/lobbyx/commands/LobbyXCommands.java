package cl.bgmp.lobbyx.commands;

import cl.bgmp.lobbyx.LobbyX;
import cl.bgmp.lobbyx.translations.AllTranslations;
import cl.bgmp.lobbyx.util.ChatUtil;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LobbyXCommands {

  private static LobbyX lobbyX = LobbyX.get();
  private static AllTranslations translations = (AllTranslations) lobbyX.getTranslations();

  @Command(
      aliases = {"reload"},
      desc = "Reload LobbyX's configuration.",
      help =
          "Reloads all the configuration present in config.yml, bringing it live over to the server.",
      max = 0)
  @CommandPermissions("lobby.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    lobbyX.reloadConfig();
    sender.sendMessage(ChatColor.GREEN + translations.get("misc.configuration.reloaded", sender));
  }

  public static class LobbyXParentCommand {
    @Command(
        aliases = {"lobbyx"},
        usage = "<reload>",
        desc = "LobbyX's node command.",
        max = 1)
    @CommandPermissions("lobby.node")
    @NestedCommand(LobbyXCommands.class)
    public static void lobby(final CommandContext args, final CommandSender sender) {
      sender.sendMessage(ChatUtil.lobbyXHeader());
      sender.sendMessage(ChatColor.GOLD + " » " + ChatColor.GREEN + "reload");
      sender.sendMessage(ChatUtil.lobbyXFooter());
    }
  }

  @TabCompletion
  public static class LobbyXCommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull CommandSender sender,
        org.bukkit.command.@NotNull Command command,
        @NotNull String label,
        @NotNull String[] args) {
      switch (args.length) {
        case 1:
          return Collections.singletonList("reload");
      }

      return Collections.emptyList();
    }
  }
}
