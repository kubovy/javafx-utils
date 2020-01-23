@file:Suppress("unused")
package com.poterion.utils.javafx

import com.poterion.utils.kotlin.toUriOrNull
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
fun URI.openInExternalApplication() = desktop?.takeIf { it.isSupported(Desktop.Action.BROWSE) }
		?.also { it.browse(this) } != null

/**
 * Tries to open an {@link File} in its default application.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun File.openInExternalApplication() = desktop?.takeIf { it.isSupported(Desktop.Action.OPEN) }
		?.also { it.open(this) } != null

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
