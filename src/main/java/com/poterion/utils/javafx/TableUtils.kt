@file:Suppress("unused")
package com.poterion.utils.javafx

import com.sun.javafx.scene.control.skin.TableViewSkin
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TreeItemPropertyValueFactory
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
			columnToFitMethod = TableViewSkin::class.java.getDeclaredMethod("resizeColumnToFitContent", TableColumn::class.java, Int::class.javaPrimitiveType)
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
 * Tries to auto-fit this [table][TreeTableView] column widths based on their content.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun TreeTableView<*>.autoFitTable() {
	if (columnToFitMethod == null) {
		try {
			columnToFitMethod = TableViewSkin::class.java.getDeclaredMethod("resizeColumnToFitContent", TableColumn::class.java, Int::class.javaPrimitiveType)
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
 * Sugar for [TreeView]'s `cellFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 *
 */
fun <T> TreeView<T>.factory(factory: TreeCell<T>.(T?, Boolean) -> Unit) {
	cellFactory = Callback<TreeView<T>, TreeCell<T>> {
		object : TreeCell<T>() {
			override fun updateItem(item: T, empty: Boolean) {
				super.updateItem(item, empty)
				factory(item, empty)
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

private fun <S, T> TreeTableColumn<S, T>.cellFactoryInternal(factory: (TreeTableCell<S, T>.(S?, T?, Boolean) -> Unit)? = null) {
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

fun <S, T> TreeTableColumn<S, T>.cell(getter: (S?) -> T) {
	cellValueFactory = Callback<TreeTableColumn.CellDataFeatures<S, T>, ObservableValue<T>> {
		SimpleObjectProperty(getter(it.value.value))
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
 * Finds first [TreeItem] in this [TreeTableView] based on the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 * @return First found [TreeItem] or `null` of none was found.
 */
fun <T> TreeTableView<T>.find(predicate: (T?) -> Boolean) = root.find(predicate)

/**
 * Finds first [TreeItem] in this [TreeItem]'s children including this [TreeItem] based on the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 * @return First found [TreeItem] or `null` of none was found.
 */
fun <T> TreeItem<T>.find(predicate: (T?) -> Boolean): TreeItem<T>? {
	if (predicate(value)) return this

	for (child in children) {
		if (predicate(child.value)) return child
	}

	for (child in children) {
		val found = child.find(predicate)
		if (found != null) return found
	}
	return null
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