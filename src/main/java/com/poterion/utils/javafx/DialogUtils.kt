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
				alert.buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
			}
			.showAndWait()
			.ifPresent { it.takeIf { it == ButtonType.YES }?.also { action() } }
}