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

import com.sun.javafx.collections.NonIterableChange.GenericAddRemoveChange
import com.sun.javafx.collections.SortHelper
import javafx.beans.Observable
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ObjectPropertyBase
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.TransformationList
import java.util.*
import java.util.function.Predicate

/**
 * Constructs a new FilteredList wrapper around the source list.
 * The provided predicate will match the elements in the source list that will be visible.
 * If the predicate is null, all elements will be matched and the list is equal to the source list.
 * @param source the source list
 * @param predicate the predicate to match the elements or null to match all elements.
 */
class BindFilteredList<E>(source: ObservableList<E>, observable: Observable, var predicate: (E) -> Boolean) :
		TransformationList<E, E>(source) {

	private var filtered: IntArray

	/**
	 * Returns the number of elements in this list.
	 *
	 * @return the number of elements in this list
	 */
	private var helper: SortHelper? = null

	/**
	 * The predicate that will match the elements that will be in this FilteredList.
	 * Elements not matching the predicate will be filtered-out.
	 * Null predicate means "always true" predicate, all elements will be matched.
	 */
	val predicateProperty: ObjectProperty<Predicate<E>> = object : ObjectPropertyBase<Predicate<E>>() {
		override fun invalidated() {
			refilter()
		}

		override fun getBean(): Any {
			return this@BindFilteredList
		}

		override fun getName(): String {
			return "predicate"
		}
	}

	override var size: Int = 0

	init {
		observable.addListener { refilter() }
		filtered = IntArray(source.size * 3 / 2 + 1)
		refilter()
	}

	override fun sourceChanged(c: ListChangeListener.Change<out E>) {
		beginChange()
		while (c.next()) {
			if (c.wasPermutated()) {
				permutate(c)
			} else if (c.wasUpdated()) {
				update(c)
			} else {
				addRemove(c)
			}
		}
		endChange()
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param  index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	override fun get(index: Int): E {
		if (index >= size) {
			throw IndexOutOfBoundsException()
		}
		return source[filtered[index]]
	}

	override fun getSourceIndex(index: Int): Int {
		if (index >= size) {
			throw IndexOutOfBoundsException()
		}
		return filtered[index]
	}

	private fun getSortHelper(): SortHelper {
		if (helper == null) {
			helper = SortHelper()
		}
		return helper!!
	}

	private fun findPosition(p: Int): Int {
		if (filtered.size == 0) {
			return 0
		}
		if (p == 0) {
			return 0
		}
		var pos = Arrays.binarySearch(filtered, 0, size, p)
		if (pos < 0) {
			pos = pos.inv()
		}
		return pos
	}


	private fun ensureSize(size: Int) {
		if (filtered.size < size) {
			val replacement = IntArray(size * 3 / 2 + 1)
			System.arraycopy(filtered, 0, replacement, 0, this.size)
			filtered = replacement
		}
	}

	private fun updateIndexes(from: Int, delta: Int) {
		for (i in from until size) {
			filtered[i] += delta
		}
	}

	private fun permutate(c: ListChangeListener.Change<out E>) {
		val from = findPosition(c.from)
		val to = findPosition(c.to)
		if (to > from) {
			for (i in from until to) {
				filtered[i] = c.getPermutation(filtered[i])
			}
			val perm = getSortHelper().sort(filtered, from, to)
			nextPermutation(from, to, perm)
		}
	}

	private fun addRemove(c: ListChangeListener.Change<out E>) {
		ensureSize(source.size)
		val from = findPosition(c.from)
		val to = findPosition(c.from + c.removedSize)
		// Mark the nodes that are going to be removed
		for (i in from until to) {
			nextRemove(from, c.removed[filtered[i] - c.from])
		}
		// Update indexes of the sublist following the last element that was removed
		updateIndexes(to, c.addedSize - c.removedSize)
		// Replace as many removed elements as possible
		var fpos = from
		var pos = c.from
		val it: ListIterator<E> = source.listIterator(pos)
		while (fpos < to && it.nextIndex() < c.to) {
			if (predicate(it.next())) {
				filtered[fpos] = it.previousIndex()
				nextAdd(fpos, fpos + 1)
				++fpos
			}
		}
		if (fpos < to) { // If there were more removed elements than added
			System.arraycopy(filtered, to, filtered, fpos, size - to)
			size -= to - fpos
		} else { // Add the remaining elements
			while (it.nextIndex() < c.to) {
				if (predicate(it.next())) {
					System.arraycopy(filtered, fpos, filtered, fpos + 1, size - fpos)
					filtered[fpos] = it.previousIndex()
					nextAdd(fpos, fpos + 1)
					++fpos
					++size
				}
				++pos
			}
		}
	}

	private fun update(c: ListChangeListener.Change<out E>) {
		val pred = predicate
		ensureSize(source.size)
		var sourceFrom = c.from
		val sourceTo = c.to
		val filterFrom = findPosition(sourceFrom)
		var filterTo = findPosition(sourceTo)
		val it: ListIterator<E> = source.listIterator(sourceFrom)
		var pos = filterFrom
		while (pos < filterTo || sourceFrom < sourceTo) {
			val el = it.next()
			if (pos < size && filtered[pos] == sourceFrom) {
				if (!pred(el)) {
					nextRemove(pos, el)
					System.arraycopy(filtered, pos + 1, filtered, pos, size - pos - 1)
					--size
					--filterTo
				} else {
					nextUpdate(pos)
					++pos
				}
			} else {
				if (pred(el)) {
					nextAdd(pos, pos + 1)
					System.arraycopy(filtered, pos, filtered, pos + 1, size - pos)
					filtered[pos] = sourceFrom
					++size
					++pos
					++filterTo
				}
			}
			sourceFrom++
		}
	}

	private fun refilter() {
		ensureSize(source.size)
		var removed: List<E>? = null
		if (hasListeners()) {
			removed = ArrayList(this)
		}
		size = 0
		var i = 0
		val it: Iterator<E> = source.iterator()
		while (it.hasNext()) {
			val next = it.next()
			if (predicate(next)) {
				filtered[size++] = i
			}
			++i
		}
		if (hasListeners()) {
			fireChange(GenericAddRemoveChange(0, size, removed, this))
		}
	}
}