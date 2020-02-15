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

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

/**
 * Opens a [confirmation][Alert.AlertType.CONFIRMATION] [dialog][Alert] and executes `action` if the user confirms that
 * [dialog][Alert].
 *
 * @author Jan Kubovy [jan@kubovy.eu]
 * @param title Dialog's window title
 * @param content Dialog's content text
 * @param header Optional header text
 * @param action Action to execute if user confirms the [dialog][Alert]
 */
fun confirmDialog(title: String,
				  content: String,
				  header: String? = null,
				  action: () -> Unit) {
	Alert(Alert.AlertType.CONFIRMATION)
			.also { alert ->
				alert.title = title
				alert.headerText = header ?: title
				alert.contentText = content
				alert.isResizable = true
				alert.buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
			}
			.showAndWait()
			.ifPresent { it.takeIf { it == ButtonType.YES }?.also { action() } }
}