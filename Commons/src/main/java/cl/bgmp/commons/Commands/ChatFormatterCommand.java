package cl.bgmp.commons.Commands;

import cl.bgmp.commons.Commons;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatFormatterCommand {
  @Command(
      aliases = {"reload"},
      desc = "Chat format node command.",
      max = 1)
  @CommandPermissions("commons.chat.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    Commons.get().getChatFormatter().refreshVault();
    Commons.get().getChatFormatter().reloadConfigValues();

    sender.sendMessage(ChatColor.GREEN + "Chat format successfully reloaded.");
  }

  public static class ChatFormatterParentCommand {
    @Command(
        aliases = {"chat", "chatformat"},
        desc = "Chat format node command.",
        max = 1)
    @CommandPermissions("commons.chat")
    @NestedCommand(ChatFormatterCommand.class)
    public static void chat(final CommandContext args, final CommandSender sender) {}
  }
}
