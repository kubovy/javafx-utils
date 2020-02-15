/******************************************************************************
 * Copyright (C) 2020 Jan Kubovy (jan@kubovy.eu)                              *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU Lesser General Public License as published      *
 * by the Free Software Foundation, either version 3 of the License, or (at   *
 * your option) any later version.                                            *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU Lesser General Public License for more details.                        *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this program.  If not, see                              *
 * <http://www.gnu.org/licenses/>.                                            *
 ******************************************************************************/
@file:Suppress("unused")
package com.poterion.utils.javafx

import com.poterion.utils.kotlin.toUriOrNull
import com.sun.javafx.PlatformUtil
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.io.File
import java.net.URI

private val LOGGER = LoggerFactory.getLogger("com.poterion.utils.javafx.CommonUtils")
private val desktop
	get() = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null

/**
 * Tries to open an {@link URI} in default browser.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun URI.openInExternalApplication() {
	when {
		PlatformUtil.isLinux() && Runtime.getRuntime().exec(arrayOf("which", "xdg-open")).inputStream.read() != -1 -> {
			Runtime.getRuntime().exec(arrayOf("xdg-open", this.toString()))
			return
		}
	}
	desktop?.takeIf { it.isSupported(Desktop.Action.BROWSE) }
			?.also { it.browse(this) } != null
}

/**
 * Tries to open an {@link File} in its default application.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun File.openInExternalApplication() {
	when {
		PlatformUtil.isLinux() -> {
			if (Runtime.getRuntime().exec(arrayOf("which", "xdg-open")).inputStream.read() != -1) {
				Runtime.getRuntime().exec(arrayOf("xdg-open", absolutePath))
				return
			}
			if (File("/usr/bin/open").let { it.exists() && it.canExecute() }) {
				Runtime.getRuntime().exec(arrayOf("/usr/bin/open", absolutePath))
				return
			}
		}
		PlatformUtil.isWindows() -> {
			Runtime.getRuntime().exec(arrayOf("rundll32", "url.dll,FileProtocolHandler", absolutePath))
			return
		}
	}
	desktop?.takeIf { it.isSupported(Desktop.Action.OPEN) }
			?.also { it.open(this) } != null
}

/**
 * Tries to open given {@code uri} in default browser or application.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param uri URI to open.
 */
fun openInExternalApplication(uri: String) = uri.toUriOrNull()
		?.takeUnless { it.scheme.startsWith("file") }
		?.openInExternalApplication()
		?: File(uri).takeIf { it.exists() }?.openInExternalApplication()
