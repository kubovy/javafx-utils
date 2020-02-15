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