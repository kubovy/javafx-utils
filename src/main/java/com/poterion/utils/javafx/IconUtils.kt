@file:Suppress("unused")
package com.poterion.utils.javafx

import javafx.scene.image.Image
import javafx.scene.image.ImageView

/**
 * Reads [Icon] as an [Image].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param width Requested width (default 0, meaning original width)
 * @param height Requested height (default 0, meaning original height)
 * @return [Image]
 */
fun Icon.toImage(width: Int = 0, height: Int = 0): Image = inputStream.toImage(width, height)

/**
 * Reads icon to an [ImageView].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param width Requested width (default 16)
 * @param height Requested height (default 16)
 * @return [ImageView]
 */
fun Icon.toImageView(width: Int = 16, height: Int = 16): ImageView = inputStream
		.use { ImageView(it.toImage(width, height)) }