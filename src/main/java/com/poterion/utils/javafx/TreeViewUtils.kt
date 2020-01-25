package com.poterion.utils.javafx

import javafx.scene.control.*
import javafx.util.Callback

/**
 * Sugar for [TreeView]'s `cellFactory`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 *
 */
fun <T> TreeView<T>.cell(factory: TreeCell<T>.(TreeItem<T>?, T?, Boolean) -> Unit) {
	cellFactory = Callback<TreeView<T>, TreeCell<T>> {
		object : TreeCell<T>() {
			override fun updateItem(item: T, empty: Boolean) {
				super.updateItem(item, empty)
				factory(treeItem, item, empty)
			}
		}
	}
}

