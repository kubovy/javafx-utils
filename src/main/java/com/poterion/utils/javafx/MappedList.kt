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

import com.poterion.utils.kotlin.noop
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.TransformationList

/**
 * Mapped observable list.
 * @author Jan Kubovy [jan@kubovy.eu]
 */
class MappedList<F, E>(source: ObservableList<F>, transformation: (F?) -> E?) : TransformationList<E, F>(source) {

	/**
	 * Mapping transformation.
	 * @see transformationProperty
	 */
	var transformation: (F?) -> E?
		get() = transformationProperty.get()
		set(value) = transformationProperty.set(value)

	/**
	 * Mapping transformation property.
	 * @see transformation
	 */
	val transformationProperty: ObjectProperty<(F?) -> E?> = SimpleObjectProperty(transformation)

	override fun sourceChanged(c: ListChangeListener.Change<out F?>) = noop()

	override fun get(index: Int): E? = source.getOrNull(index)?.let(transformation)

	override fun getSourceIndex(index: Int): Int = index

	override val size: Int
		get() = source.size
}