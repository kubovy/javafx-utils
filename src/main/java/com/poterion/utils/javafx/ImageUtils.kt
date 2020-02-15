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