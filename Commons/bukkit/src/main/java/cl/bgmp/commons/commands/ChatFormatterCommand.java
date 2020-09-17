package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import cl.bgmp.minecraft.util.commands.annotations.CommandPermissions;
import cl.bgmp.minecraft.util.commands.annotations.NestedCommand;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatFormatterCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads the chat formatting.",
      max = 1)
  @CommandPermissions("commons.chat.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    final Optional<Module> module =
        Commons.get().getModuleManager().getModule(ModuleId.CHAT_FORMAT);
    if (!module.isPresent()) {
      sender.sendMessage(
          ChatColor.RED
              + Commons.get().getTranslations().get("module.chatformat.no.formatting", sender));
      return;
    }

    final ChatFormatModule chatFormatModule = (ChatFormatModule) module.get();
    chatFormatModule.refreshVault();
    chatFormatModule.reloadConfigValues();

    sender.sendMessage(
        ChatColor.GREEN
            + Commons.get().getTranslations().get("module.chatformat.reload.success", sender));
  }

  public static class ChatFormatterParentCommand {
    @Command(
        aliases = {"chat", "chatformat"},
        desc = "Chat format node command.",
        max = 1)
    @CommandPermissions("commons.chat")
    @NestedCommand(value = ChatFormatterCommand.class, executeBody = false)
    public static void chat(final CommandContext args, final CommandSender sender) {}
  }
}
