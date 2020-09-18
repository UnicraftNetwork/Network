package cl.bgmp.lobbyx.commands;

import cl.bgmp.lobbyx.LobbyX;
import cl.bgmp.lobbyx.translations.AllTranslations;
import cl.bgmp.lobbyx.util.ChatUtil;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import cl.bgmp.minecraft.util.commands.annotations.TabCompletion;
import com.google.inject.Inject;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class LobbyXCommands {

  private LobbyX lobbyX;
  private AllTranslations translations;

  @Inject
  public LobbyXCommands(LobbyX lobbyX, AllTranslations translations) {
    this.lobbyX = lobbyX;
    this.translations = translations;
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

  @Command(
      aliases = {"reload"},
      desc = "Reload LobbyX's configuration.",
      max = 0)
  @CommandPermissions("lobby.reload")
  public void reload(final CommandContext args, final CommandSender sender) {
    lobbyX.reloadConfig();
    ChatUtil.log(sender, ChatColor.GREEN + translations.get("misc.configuration.reloaded", sender));
  }

  @TabCompletion
  public static class LobbyXCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      if (args.length == 1) return Collections.singletonList("reload");
      else return Collections.emptyList();
    }
  }
}
