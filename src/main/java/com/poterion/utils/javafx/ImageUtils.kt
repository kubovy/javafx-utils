package com.poterion.utils.javafx

import javafx.scene.image.Image
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Reads [InputStream] as an [Image].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param width Requested width (default 0, meaning original width)
 * @param height Requested height (default 0, meaning original height)
 * @return [Image]
 */
fun InputStream.toImage(width: Int = 0, height: Int = 0) = use {
	Image(it, width.toDouble(), height.toDouble(), true, true)
}

/**
 * Reads [File] as an [Image].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param width Requested width (default 0, meaning original width)
 * @param height Requested height (default 0, meaning original height)
 * @return [Image]
 */
fun File.toImage(width: Int = 0, height: Int = 0) = FileInputStream(this).toImage(width, height)