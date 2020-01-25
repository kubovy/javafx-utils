package com.poterion.utils.javafx

import javafx.beans.property.BooleanProperty
import javafx.scene.control.TreeItem

/**
 * Finds first [TreeItem] in this [TreeItem]'s tree including this [TreeItem] based on the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 * @return First found [TreeItem] or `null` of none was found.
 */
fun <T> TreeItem<T>.find(predicate: TreeItem<T>.(T?) -> Boolean): TreeItem<T>? {
	if (predicate(value)) return this

	for (child in children) {
		if (child.predicate(child.value)) return child
	}

	for (child in children) {
		val found = child.find(predicate)
		if (found != null) return found
	}
	return null
}

/**
 * Finds all [TreeItems][TreeItem] in this [TreeItem]'s tree including this [TreeItem] based on the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 * @return First found [TreeItem] or `null` of none was found.
 */
fun <T> TreeItem<T>.findAll(predicate: TreeItem<T>.(T) -> Boolean): Collection<TreeItem<T>> {
	val result = mutableListOf<TreeItem<T>>()
	if (!isLeaf) {
		for (child in children) result.addAll(child.findAll(predicate))
	}
	if (predicate(value)) result.add(this)
	return result
}

/**
 * Expands all [TreeItems][TreeItem] in this tree which resolve the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 */
fun <T> TreeItem<T>.expandTree(predicate: TreeItem<T>.(T) -> Boolean = { true }) {
	if (!isLeaf) {
		if (predicate(value)) isExpanded = true
		for (child in children) child.expandTree(predicate)
	}
}

/**
 * Monitors expansion of this [TreeItem].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param expandedGetter Expanded state getter
 * @param expandedSetter Expanded state setter
 */
fun <T> TreeItem<T>.monitorExpansion(expandedGetter: (T?) -> Boolean, expandedSetter: (T?, Boolean) -> Unit) = apply {
	isExpanded = expandedGetter(value)

	expandedProperty().addListener { observable, _, expanded ->
		expandedSetter(((observable as? BooleanProperty)?.bean as? TreeItem<T>)?.value, expanded)
	}
}