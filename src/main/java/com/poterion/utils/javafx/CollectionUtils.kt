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

import javafx.beans.Observable
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet

/**
 * Bind an observable to this observable applying the provided filter.
 *
 * @param observable Observable to bind
 * @param predicate Filter predicate
 * @return Filtered binding
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> ObservableList<E>.bindFiltered(observable: Observable, predicate: (E) -> Boolean) =
		BindFilteredList(this, observable, predicate)

/**
 * Wrap an [ObservableList] with [MappedList] using the provided `transformation`.
 *
 * @param transformation Transformation to apply
 * @return [MappedList]
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E, F> ObservableList<E>.mapped(transformation: (E?) -> F?) = MappedList(this, transformation)

/**
 * Converts this [Set] to [ObservableSet].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> Set<E>.toObservableSet(): ObservableSet<E> = FXCollections.observableSet(toMutableSet())

/**
 * Converts this [Collection] to [ObservableList].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> Collection<E>.toObservableList(): ObservableList<E> = FXCollections.observableList(toMutableList())

/**
 * Converts this [Array] to [ObservableList].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> Array<E>.toObservableList(): ObservableList<E> = FXCollections.observableList(toMutableList())

/**
 * Wraps this [Collection] as a [ReadOnlyObservableList].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> Collection<E>.toReadOnlyObservableList(): ReadOnlyObservableList<E> = ReadOnlyObservableList(toObservableList())

/**
 * Wraps this [Array] as a [ReadOnlyObservableList].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <E> Array<E>.toReadOnlyObservableList(): ReadOnlyObservableList<E> = ReadOnlyObservableList(toObservableList())

/**
 * Converts this [Map] to [ObservableMap].
 * @author Jan Kubovy [jan@kubovy.eu]
 */
fun <K, V> Map<K, V>.toObservableMap(): ObservableMap<K, V> = FXCollections.observableMap(toMutableMap())