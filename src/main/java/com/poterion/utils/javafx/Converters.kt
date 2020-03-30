/******************************************************************************
 * Copyright (c) 2020 Jan Kubovy <jan@kubovy.eu>                              *
 *                                                                            *
 * This program is free software: you can redistribute it and/or modify it    *
 * under the terms of the GNU General Public License as published by the Free *
 * Software Foundation, version 3.                                            *
 *                                                                            *
 * This program is distributed in the hope that it will be useful, but        *
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY *
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License    *
 * for more details.                                                          *
 *                                                                            *
 * You should have received a copy of the GNU General Public License along    *
 * with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ******************************************************************************/
@file:Suppress("unused")

package com.poterion.utils.javafx

import javafx.scene.paint.Color
import kotlin.math.roundToInt

/**
 * Converts AWT [java.awt.Color] to [Color].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun java.awt.Color.toColor(): Color = Color.rgb(red, green, blue)

/**
 * Converts [Color] to AWT [java.awt.Color].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun Color.toAwtColor() = java.awt.Color(red.toFloat(), green.toFloat(), blue.toFloat())

/**
 * Coverts a hex-encoded color to [Color].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun String.toColor(): Color? = "^#?([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})".toRegex()
		.matchEntire(this)
		?.groupValues
		?.mapNotNull { it.toIntOrNull(16) }
		?.takeIf { it.size == 3 }
		?.let { Color.rgb(it[0], it[1], it[2]) }

/**
 * Converts a [Color] to hex-encoded color string.
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun Color.toHex(prefix: String = "#") = listOf(red, green, blue)
		.map { (it * 255.0).roundToInt() }
		.joinToString("", prefix) { "%02X".format(it) }

