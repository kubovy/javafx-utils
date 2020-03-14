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
 *                                                                            *
 ******************************************************************************/
@file:Suppress("unused")
package com.poterion.utils.javafx

import javafx.beans.InvalidationListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import java.util.*
import java.util.function.Predicate
import java.util.function.UnaryOperator

/**
 * Read-only observable list wrapper.
 * @author Jan Kubovy [jan@kubovy.eu]
 */
class ReadOnlyObservableList<E>(private val backingList: ObservableList<E>) : ObservableList<E> {
	override val size: Int
		get() = backingList.size

	override fun get(index: Int): E = backingList[index]

	override fun isEmpty(): Boolean = backingList.isEmpty()

	override fun indexOf(element: E): Int = backingList.indexOf(element)

	override fun lastIndexOf(element: E): Int = backingList.lastIndexOf(element)

	override fun contains(element: E): Boolean = backingList.contains(element)

	override fun containsAll(elements: Collection<E>): Boolean = backingList.containsAll(elements)

	override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> = backingList.subList(fromIndex, toIndex)

	override fun iterator(): MutableIterator<E> = backingList.iterator()

	override fun listIterator(): MutableListIterator<E> = backingList.listIterator()

	override fun listIterator(index: Int): MutableListIterator<E> = backingList.listIterator(index)

	override fun add(element: E): Boolean = throw UnsupportedOperationException()

	override fun add(index: Int, element: E) = throw UnsupportedOperationException()

	override fun addAll(vararg elements: E): Boolean = throw UnsupportedOperationException()

	override fun addAll(elements: Collection<E>): Boolean = throw UnsupportedOperationException()

	override fun addAll(index: Int, elements: Collection<E>): Boolean = throw UnsupportedOperationException()

	override fun remove(element: E): Boolean = throw UnsupportedOperationException()

	override fun remove(fromIndex: Int, toIndex: Int) = throw UnsupportedOperationException()

	override fun removeAt(index: Int): E = throw UnsupportedOperationException()

	override fun removeIf(filter: Predicate<in E>): Boolean = throw UnsupportedOperationException()

	override fun removeAll(vararg elements: E): Boolean = throw UnsupportedOperationException()

	override fun removeAll(elements: Collection<E>): Boolean = throw UnsupportedOperationException()

	override fun set(index: Int, element: E): E = throw UnsupportedOperationException()

	override fun setAll(vararg elements: E): Boolean = throw UnsupportedOperationException()

	override fun setAll(col: MutableCollection<out E>?): Boolean = throw UnsupportedOperationException()

	override fun replaceAll(operator: UnaryOperator<E>) = throw UnsupportedOperationException()

	override fun retainAll(vararg elements: E): Boolean = throw UnsupportedOperationException()

	override fun retainAll(elements: Collection<E>): Boolean = throw UnsupportedOperationException()

	override fun clear()  = throw UnsupportedOperationException()

	override fun addListener(listener: InvalidationListener?) = backingList.addListener(listener)

	override fun removeListener(listener: InvalidationListener?) = backingList.removeListener(listener)

	override fun addListener(listener: ListChangeListener<in E>?) = backingList.addListener(listener)

	override fun removeListener(listener: ListChangeListener<in E>?) = backingList.removeListener(listener)

	fun filtered(predicate: (E) -> Boolean): FilteredList<E> = backingList.filtered(predicate)

	override fun filtered(predicate: Predicate<E>?): FilteredList<E> = backingList.filtered(predicate)

	override fun sorted(comparator: Comparator<E>): SortedList<E> = backingList.sorted(comparator)

	override fun sorted(): SortedList<E> = backingList.sorted()
}