package cl.bgmp.utilsbukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface Channels {
  String channel = "BungeeCord";
  String connect = "Connect";

  static void registerBungeeToPlugin(Plugin plugin) {
    plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
  }

  static void sendPlayerToServer(Plugin sender, Player player, String serverName) {
    try {
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

      dataOutputStream.writeUTF(connect);
      dataOutputStream.writeUTF(serverName);

      player.sendPluginMessage(sender, channel, byteArrayOutputStream.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static String getPrettyServerPlayerCount(final String ip, final int port) {
    try {
      final Socket socket = new Socket(ip, port);

      final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
      final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

      dataOutputStream.write(0xFE);

      int b;
      StringBuilder str = new StringBuilder();
      while ((b = dataInputStream.read()) != -1) {
        if (b > 16 && b != 255 && b != 23 && b != 24) {
          str.append((char) b);
        }
      }

      final String[] data = str.toString().split("§");
      final int onlinePlayers = Integer.parseInt(data[1]);
      final int maxPlayers = Integer.parseInt(data[2]);

      return ChatColor.GREEN.toString()
          + onlinePlayers
          + ChatColor.DARK_GRAY
          + "/"
          + ChatColor.GRAY
          + maxPlayers;

    } catch (IOException ignore) {
    }

    return ChatColor.RED + "Offline";
  }
}
