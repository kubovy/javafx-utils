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
import javafx.scene.control.TableColumn
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableCell
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView
import javafx.scene.control.cell.TreeItemPropertyValueFactory
import javafx.scene.input.MouseEvent
import javafx.util.Callback
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

private val LOGGER = LoggerFactory.getLogger("com.poterion.utils.javafx.TreeTableUtils")
private var columnToFitMethod: Method? = null

/**
 * Tries to auto-fit this [table][TreeTableView] column widths based on their content.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun TreeTableView<*>.autoFitTable() {
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

/**
 * Sugar for [TreeTableView]'s `rowFactory`
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <T> TreeTableView<T>.row(factory: TreeTableRow<T>.(T?) -> Unit) {
	rowFactory = Callback<TreeTableView<T>, TreeTableRow<T>> {
		object : TreeTableRow<T>() {
			override fun updateItem(item: T, empty: Boolean) {
				super.updateItem(item, empty)
				factory(item)
			}
		}
	}
}

private fun <S, T> TreeTableColumn<S, T>.cellFactoryInternal(
		factory: (TreeTableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
	if (factory != null) cellFactory = Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>> {
		object : TreeTableCell<S, T>() {
			override fun updateItem(item: T?, empty: Boolean) {
				super.updateItem(item, empty)
				factory(treeTableRow.item, item, empty)
			}
		}
	}
}

/**
 * Sugar for [TableColumn]'s `cellValueFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <S, T> TreeTableColumn<S, T>.cell(getter: TreeTableColumn<S, T>.(TreeTableColumn.CellDataFeatures<S, T>?) -> T?) {
	cellValueFactory = Callback<TreeTableColumn.CellDataFeatures<S, T>, ObservableValue<T>> { param ->
		this.getter(param)?.let { SimpleObjectProperty(it) }
	}
}

/**
 * Sugar for [TableColumn]'s `cellFactory` and `cellValueFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <S, T> TreeTableColumn<S, T>.cell(property: String? = null,
									  factory: (TreeTableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
	cellValueFactory = TreeItemPropertyValueFactory(property)
	cellFactoryInternal(factory)
}

/**
 * Sugar for setting a item click handler.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param handler Handler.
 */
fun <T> TreeTableView<T>.setOnItemClick(handler: TreeTableRow<T>.(T?, MouseEvent) -> Unit) = row { item ->
	setOnMouseClicked { event -> handler(item, event) }
}

/**
 * Finds first [TreeItem] in this [TreeTableView] based on the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 * @return First found [TreeItem] or `null` of none was found.
 */
fun <T> TreeTableView<T>.find(predicate: TreeItem<T>.(T?) -> Boolean) = root.find(predicate)