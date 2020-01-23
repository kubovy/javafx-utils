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