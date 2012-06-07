/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.compactsolars;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;

public class Version {
  private static String major;
  private static String minor;
  private static String rev;
  private static String build;
  private static String mcversion;
  private static boolean loaded;

  private static void init() {
    InputStream stream = Version.class.getClassLoader().getResourceAsStream("compactsolarsversion.properties");
    Properties properties = new Properties();
  
    if (stream != null) {
      try {
        properties.load(stream);
        major = properties.getProperty("compactsolars.build.major.number");
        minor = properties.getProperty("compactsolars.build.minor.number");
        rev = properties.getProperty("compactsolars.build.revision.number");
        build = properties.getProperty("compactsolars.build.build.number");
        mcversion = properties.getProperty("compactsolars.build.mcversion");
      } catch (IOException ex) {
        FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Could not get CompactSolars version information - corrupted installation detected!", ex);
        throw new RuntimeException(ex);
      }
    }
    loaded = true;
  }
  public static final String version() {
    if (!loaded) {
      init();
    }
    return major+"."+minor+"."+rev;
  }
}
