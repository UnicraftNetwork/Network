package cl.bgmp.lobby.Portals;

import cl.bgmp.lobby.Lobby;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class PortalsXMLManager {
  private String fileName = "portals.xml";
  private File fileInDataFolder = new File(Lobby.get().getDataFolder(), fileName);
  private Document portalsXML;
  private List<Element> portalModules;

  public PortalsXMLManager() {
    if (!fileInDataFolder.exists()) {
      try {
        FileUtils.copyInputStreamToFile(
            Objects.requireNonNull(Lobby.get().getResource(fileName)), fileInDataFolder);
      } catch (IOException | NullPointerException e) {
        e.printStackTrace();
      }
    }

    try {
      this.portalsXML = new SAXBuilder().build(fileInDataFolder);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }

    final Element root = Objects.requireNonNull(portalsXML).getRootElement();
    this.portalModules = root.getChildren();
  }

  public List<Element> getAllPortalModules() {
    return portalModules;
  }
}
