package cl.bgmp.lobby.Portals;

import cl.bgmp.lobby.Lobby;
import cl.bgmp.utilsbukkit.FileUtils;
import java.io.File;
import java.util.List;
import java.util.Objects;
import org.jdom2.Document;
import org.jdom2.Element;

public class PortalsXMLManager {
  private String fileName = "portals.xml";
  private List<Element> portals;

  public PortalsXMLManager() {
    try {
      final Document portalsXML =
          FileUtils.buildXMLDocumentFromFileAtResources(
              Lobby.get(), new File(Lobby.get().getDataFolder(), fileName), fileName);
      final Element root = Objects.requireNonNull(portalsXML).getRootElement();
      this.portals = root.getChildren();
    } catch (NullPointerException ignore) {
    }
  }

  public List<Element> getAllPortalModules() {
    return portals;
  }
}
