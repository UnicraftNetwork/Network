package cl.bgmp.lobby.portals;

import cl.bgmp.lobby.Lobby;
import cl.bgmp.lobby.portals.NPCPortal.NPCPortal;
import cl.bgmp.lobby.portals.SignPortal.SignPortal;
import cl.bgmp.utilsbukkit.Locations;
import cl.bgmp.utilsbukkit.Server;
import com.google.common.collect.ImmutableList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class PortalFactory {
  private Set<BungeePortal> portalRegistry = new HashSet<>();
  private PortalsXMLManager portalsXMLManager = new PortalsXMLManager();

  public PortalFactory() {}

  public Set<BungeePortal> getPortalRegistry() {
    return portalRegistry;
  }

  public void loadPortals() {
    if (portalsXMLManager.getAllPortalModules() == null) return;
    if (portalsXMLManager.getAllPortalModules().isEmpty()) return;

    for (Element element : portalsXMLManager.getAllPortalModules()) {
      try {
        final String id = element.getAttributeValue("id");
        final String locationString = element.getAttributeValue("location");
        final String serverName = element.getAttributeValue("bungee-server");
        final String ip = element.getAttributeValue("ip");
        final int port = element.getAttribute("port").getIntValue();

        if (element.getName().equals(PortalType.SIGN.getId())) {
          final Location location =
              Locations.parseLocationFromSplit(locationString.split(","), false);
          final ImmutableList<String> lines =
              ImmutableList.copyOf(
                  element.getChildren().stream()
                      .map(line -> ChatColor.translateAlternateColorCodes('$', line.getText()))
                      .collect(Collectors.toList()));

          portalRegistry.add(new SignPortal(id, new Server(serverName, ip, port), location, lines));
        } else if (element.getName().equals(PortalType.NPC.getId())) {
          final Location location =
              Locations.parseLocationFromSplit(locationString.split(","), true);

          final Element titleModule = element.getChild("title");
          final List<Element> titleLineModules = titleModule.getChildren();
          final Element mineskinModule = element.getChild("mineskin");

          // TODO: Create a new utils method to specify colour characters
          final List<String> title =
              titleLineModules.stream()
                  .map(line -> ChatColor.translateAlternateColorCodes('$', line.getText()))
                  .collect(Collectors.toList());
          final String skinId = mineskinModule.getText();

          final NPC npc = Lobby.get().getNPCLib().createNPC(title);
          MineSkinFetcher.fetchSkinFromIdAsync(
              Integer.parseInt(skinId),
              skin -> {
                npc.setSkin(skin);
                npc.setLocation(location);
                npc.create();
              });

          portalRegistry.add(new NPCPortal(id, new Server(serverName, ip, port), location, npc));
        }
      } catch (NullPointerException | DataConversionException | NumberFormatException e) {
        e.printStackTrace();
      }
    }
  }
}
