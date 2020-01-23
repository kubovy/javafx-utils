@file:Suppress("unused")
package com.poterion.utils.javafx

import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * Common icon interface to be used for icon enums. E.g.:
 *
 *     package com.poterion.something
 *
 *     enum class SomeIcon : Icon {
 *       ICON_A,
 *       ICON_B;
 *     }
 *
 * The above example expects the icon files to be `PNG` files in `resources/com/poterion/something/icons` where
 * the file name matches lower-case [Enum] name. This introduces some limitation, e.g., an icon filename cannot contain
 * dashed.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
interface Icon {
	val inputStream: InputStream
		get() = this::class.java.getResourceAsStream("icons/${toString().toLowerCase()}.png")
				.use { it.readBytes() }
				.let { ByteArrayInputStream(it) }
}