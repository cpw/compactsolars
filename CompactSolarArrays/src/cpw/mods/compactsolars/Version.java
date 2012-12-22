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

import java.util.Properties;

public class Version {
  private static String major;
  private static String minor;
  private static String rev;
  private static String build;
  private static String mcversion;

  static void init(Properties properties) {
    if (properties != null) {
	    major = properties.getProperty("CompactSolars.build.major.number");
		minor = properties.getProperty("CompactSolars.build.minor.number");
		rev = properties.getProperty("CompactSolars.build.revision.number");
		build = properties.getProperty("CompactSolars.build.number");
		mcversion = properties.getProperty("CompactSolars.build.mcversion");
    }
  }
  public static final String version() {
    return String.format("%s.%s.%s build %s", major, minor, rev, build);
  }
}
