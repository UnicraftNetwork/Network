package cl.bgmp.commons.commands;

import cl.bgmp.commons.Commons;
import cl.bgmp.commons.modules.ChatFormatModule;
import cl.bgmp.commons.modules.Module;
import cl.bgmp.commons.modules.ModuleId;
import cl.bgmp.utilsbukkit.Chat;
import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.annotations.Command;
import com.sk89q.minecraft.util.commands.annotations.CommandPermissions;
import com.sk89q.minecraft.util.commands.annotations.NestedCommand;
import com.sk89q.minecraft.util.commands.annotations.TabCompletion;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

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
    @NestedCommand(value = ChatFormatterCommand.class, executeBody = false)
    public static void chat(final CommandContext args, final CommandSender sender) {}
  }

  @TabCompletion
  public static class FUtilsTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(
        CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
      switch (args.length) {
        case 1:
          return Arrays.asList("reload");
        default:
          return Collections.emptyList();
      }
    }
  }
}
