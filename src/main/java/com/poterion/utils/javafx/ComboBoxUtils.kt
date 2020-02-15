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

import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback

/**
 * Sugar to define a common [ComboBox] factory for both `cellFactory` and `buttonFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param factory The factory
 */
fun <T> ComboBox<T>.factory(factory: ListCell<T>.(T?, Boolean) -> Unit) {
	cellFactory = Callback<ListView<T>, ListCell<T>> {
		object : ListCell<T>() {
			override fun updateItem(item: T, empty: Boolean) {
				super.updateItem(item, empty)
				factory(item, empty)
			}
		}
	}
	buttonCell = cellFactory.call(null)
}