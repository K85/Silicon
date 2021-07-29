package com.sakurawald.silicon.util

import com.sakurawald.silicon.Silicon
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.web.HTMLEditor
import javafx.stage.Stage
import kotlin.math.min

@Suppress("unused")
class JavaFxUtil {
    object ImageTools {
        @JvmStatic
        fun centerImage(imgView: ImageView) {
            val img = imgView.image
            if (img != null) {
                val w: Double
                val h: Double
                val ratioX = imgView.fitWidth / img.width
                val ratioY = imgView.fitHeight / img.height
                val reducCoeff = min(ratioX, ratioY)
                w = img.width * reducCoeff
                h = img.height * reducCoeff
                imgView.x = (imgView.fitWidth - w) / 2
                imgView.y = (imgView.fitHeight - h) / 2
            }
        }
    }

    object WindowTools {
        @JvmStatic
        fun setWindowIcon(stage: Stage) {
            stage.icons.add(Image(Silicon::class.java.getResourceAsStream("icon.png")))
        }
    }

    object HtmlEditorTools {
        @JvmStatic
        fun clearToolKits(htmlEditor: HTMLEditor) {
            val nodes = htmlEditor.lookupAll(".tool-bar").toTypedArray()
            for (node in nodes) {
                node.isVisible = false
                node.isManaged = false
            }
        }
    }

    object DialogTools {
        @JvmStatic
        fun warnDialog(message: String?) {
            alert(AlertType.WARNING, message, ButtonType.OK).show()
        }

        @JvmStatic
        fun errorDialog(message: String?) {
            alert(AlertType.ERROR, message, ButtonType.OK).show()
        }

        @JvmStatic
        fun informationDialog(message: String?) {
            alert(AlertType.INFORMATION, message, ButtonType.OK).show()
        }

        @JvmStatic
        fun noneDialog(message: String?) {
            alert(AlertType.NONE, message, ButtonType.OK).show()
        }

        @JvmStatic
        fun confirmationDialog(message: String?) {
            alert(AlertType.CONFIRMATION, message, ButtonType.OK).show()
        }

        @JvmStatic
        fun unsupportOperationDialog() {
            errorDialog("Unsupported Operation.")
        }

        @JvmStatic
        fun setIcon(dialog: Dialog<*>) {
            // Set Icon
            (dialog.dialogPane.scene.window as Stage).icons.add(
                Image(
                    Silicon::class.java.getResourceAsStream("icon.png")
                )
            )
        }

        @JvmStatic
        fun alert(alertType: AlertType?, contentText: String?, vararg buttonTypes: ButtonType?): Alert {
            val a = Alert(alertType, contentText, *buttonTypes)
            setIcon(a)
            return a
        }
    }
}