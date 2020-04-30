package cl.bgmp.commons.Commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.Modules.ChatFormatModule;
import cl.bgmp.commons.Modules.Module;
import cl.bgmp.commons.Modules.ModuleId;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.Translations.Translations;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatFormatterCommand {
  @Command(
      aliases = {"reload"},
      desc = "Reloads the chat formatting.",
      max = 1)
  @CommandPermissions("commons.chat.reload")
  public static void reload(final CommandContext args, final CommandSender sender) {
    final Module module = Commons.get().getModule(ModuleId.CHAT_FORMAT);
    if (module == null) {
      sender.sendMessage(
          Chat.getStringAsException(Translations.get("module.chatformat.no.formatting", sender)));
      return;
    }

    final ChatFormatModule chatFormatModule = (ChatFormatModule) module;
    chatFormatModule.refreshVault();
    chatFormatModule.reloadConfigValues();

    sender.sendMessage(
        ChatColor.GREEN + Translations.get("module.chatformat.reload.success", sender));
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
