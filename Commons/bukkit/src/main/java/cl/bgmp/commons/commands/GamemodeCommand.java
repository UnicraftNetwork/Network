package cl.bgmp.commons.commands;

import cl.bgmp.utilsbukkit.translations.Translations;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand {

  private static GameMode stringToGamemode(String string) {
    switch (string) {
      case "0":
      case "SURVIVAL":
        return GameMode.SURVIVAL;
      case "1":
      case "CREATIVE":
        return GameMode.CREATIVE;
      case "2":
      case "ADVENTURE":
        return GameMode.ADVENTURE;
      case "3":
      case "SPECTATOR":
        return GameMode.SPECTATOR;
    }
    return null;
  }

  @Command(
      aliases = {"gamemode", "gm"},
      desc = "Custom gamemode command.",
      min = 1,
      max = 2,
      usage = "<gamemode | player> <gamemode>")
  @CommandPermissions("commons.gamemode")
  public static void gamemode(final CommandContext args, final CommandSender sender) {
    String gamemodeString = args.getString(0).toUpperCase();
    GameMode gamemode = stringToGamemode(gamemodeString);

    if (gamemode == null) {
      sender.sendMessage(
          ChatColor.RED + Translations.get("commons.invalid.gamemode", sender, gamemodeString));
      return;
    }

    if (args.argsLength() == 1) {
      if (sender instanceof Player) {
        Player player = (Player) sender;
        player.setGameMode(gamemode);
        player.sendMessage(
            ChatColor.YELLOW
                + Translations.get("commons.gamemode.set.own", sender, gamemode.toString()));
      } else {
        sender.sendMessage(ChatColor.RED + Translations.get("commands.no.console", sender));
      }
    } else {
      Player thirdParty = Bukkit.getPlayer(args.getString(1));
      if (thirdParty != null) {
        thirdParty.setGameMode(gamemode);
        sender.sendMessage(
            ChatColor.YELLOW
                + Translations.get(
                    "commons.gamemode.set.other",
                    sender,
                    thirdParty.getDisplayName(),
                    gamemode.toString()));
        thirdParty.sendMessage(
            ChatColor.YELLOW
                + Translations.get("commons.gamemode.set.own", sender, gamemode.toString()));
      } else {
        sender.sendMessage(ChatColor.RED + Translations.get("misc.invalid.player", sender));
      }
    }
  }
}
