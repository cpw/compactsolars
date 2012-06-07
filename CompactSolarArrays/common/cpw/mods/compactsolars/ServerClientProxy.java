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

import net.minecraft.src.ModLoader;

public enum ServerClientProxy {
	CLIENT("cpw.mods.compactsolars.client.ClientProxy"),
	SERVER("cpw.mods.compactsolars.server.ServerProxy");
	
	private String className;
	private ServerClientProxy(String proxyClassName) {
		className=proxyClassName;
	}
	
	private IProxy buildProxy() {
		try {
			return (IProxy) Class.forName(className).newInstance();
		} catch (Exception e) {
			ModLoader.getLogger().severe("A fatal error has occured initializing CompactSolars");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	public static IProxy getProxy() {
		try {
			ModLoader.class.getMethod("getMinecraftInstance");
		} catch (SecurityException e) {
			// UNPOSSIBLE
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			return SERVER.buildProxy();
		}
		return CLIENT.buildProxy();
	}

}
