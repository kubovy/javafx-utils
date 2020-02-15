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
 * Find a parent [TreeItem] of this [TreeItem] which satisfies the given `predicate`.
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param predicate Predicate
 */
fun <T> TreeItem<T>.findParent(predicate: TreeItem<T>.(T?) -> Boolean): TreeItem<T>? {
	var current = this
	while (current.parent != null) {
		current = current.parent
		if (current.predicate(current.value)) return current
	}
	return null
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