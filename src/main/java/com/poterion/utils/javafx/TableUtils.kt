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

import com.sun.javafx.scene.control.skin.TableViewSkin
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.util.Callback
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

private val LOGGER = LoggerFactory.getLogger("FXUtils")
private var columnToFitMethod: Method? = null

/**
 * Tries to auto-fit this [table][TableView] column widths based on their content.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun TableView<*>.autoFitTable() {
	if (columnToFitMethod == null) {
		try {
			columnToFitMethod = TableViewSkin::class.java.getDeclaredMethod(
					"resizeColumnToFitContent", TableColumn::class.java, Int::class.javaPrimitiveType)
			columnToFitMethod?.isAccessible = true
		} catch (e: NoSuchMethodException) {
			e.printStackTrace()
		}
	}

	skin?.also { tableViewSkin ->
		for (column in columns) {
			try {
				columnToFitMethod?.invoke(tableViewSkin, column, -1)
			} catch (e: IllegalAccessException) {
				LOGGER.error(e.message, e)
			} catch (e: InvocationTargetException) {
				LOGGER.error(e.message, e)
			}
		}
	}
}

private fun <S, T> TableColumn<S, T>.cellFactoryInternal(factory: (TableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
	if (factory != null) cellFactory = Callback<TableColumn<S, T>, TableCell<S, T>> {
		object : TableCell<S, T>() {
			override fun updateItem(item: T?, empty: Boolean) {
				super.updateItem(item, empty)
				factory(tableRow.item as? S, item, empty)
			}
		}
	}
}

/**
 * Sugar for [TableView]'s `rowFactory`
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <T> TableView<T>.row(factory: TableRow<T>.(T?) -> Unit) {
	rowFactory = Callback<TableView<T>, TableRow<T>> {
		object : TableRow<T>() {
			override fun updateItem(item: T, empty: Boolean) {
				super.updateItem(item, empty)
				factory(item)
			}
		}
	}
}

/**
 * Sugar for [TableColumn]'s `cellFactory` and `cellValueFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <S, T> TableColumn<S, T>.cell(getter: (S?) -> T,
								  factory: (TableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
	cellValueFactory = Callback<TableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
		SimpleObjectProperty(getter(it.value))
	}
	cellFactoryInternal(factory)
}

/**
 * Sugar for [TableColumn]'s `cellFactory` and `cellValueFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <S, T> TableColumn<S, T>.cell(property: String? = null,
								  factory: (TableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
	cellValueFactory = PropertyValueFactory(property)
	cellFactoryInternal(factory)
}

/**
 * Sugar for setting a item click handler.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param handler Handler.
 */
fun <T> TableView<T>.setOnItemClick(handler: TableRow<T>.(T?, MouseEvent) -> Unit) = row { item ->
	setOnMouseClicked { event -> handler(item, event) }
}
