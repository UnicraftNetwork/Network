package cl.bgmp.utilsbukkit;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.bukkit.plugin.Plugin;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public interface FileUtils {
  static Document buildXMLDocumentFromFileAtResources(
      Plugin resourcesOwnerPlugin, File file, String xmlFileName) {
    if (!file.exists()) {
      try {
        org.apache.commons.io.FileUtils.copyInputStreamToFile(
            Objects.requireNonNull(resourcesOwnerPlugin.getResource(xmlFileName)), file);
      } catch (IOException | NullPointerException e) {
        e.printStackTrace();
      }
    }

    try {
      return new SAXBuilder().build(file);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }

    return null;
  }
}
