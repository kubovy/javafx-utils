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
package com.poterion.utils.javafx

import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.IntegerBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * Observable matcher binding
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <T> ObservableObjectValue<T>.matches(predicate: (T?) -> Boolean): BooleanBinding {
	return object : BooleanBinding() {
		init {
			super.bind(this@matches)
		}

		override fun dispose() {
			super.unbind(this@matches)
		}

		override fun computeValue(): Boolean {
			return predicate(this@matches.get())
		}

		override fun getDependencies(): ObservableList<*>? {
			return FXCollections.singletonObservableList(this@matches)
		}
	}
}

/**
 * Observable mapping binding
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <T, M> ObservableObjectValue<T>.mapped(transformation: (T?) -> M): ObjectBinding<M> {
	return object : ObjectBinding<M>() {
		init {
			super.bind(this@mapped)
		}

		override fun dispose() {
			super.unbind(this@mapped)
		}

		override fun computeValue(): M {
			return transformation(this@mapped.get())
		}

		override fun getDependencies(): ObservableList<*>? {
			return FXCollections.singletonObservableList(this@mapped)
		}
	}
}

/**
 * Observable [Int] mapping binding
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <T> ObservableObjectValue<T>.mappedInt(transformation: (T?) -> Int): IntegerBinding {
	return object : IntegerBinding() {
		init {
			super.bind(this@mappedInt)
		}

		override fun dispose() {
			super.unbind(this@mappedInt)
		}

		override fun computeValue(): Int {
			return transformation(this@mappedInt.get())
		}

		override fun getDependencies(): ObservableList<*>? {
			return FXCollections.singletonObservableList(this@mappedInt)
		}
	}
}