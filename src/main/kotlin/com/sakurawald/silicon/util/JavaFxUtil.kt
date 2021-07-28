package com.sakurawald.silicon.util

import com.sakurawald.silicon.Silicon
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.web.HTMLEditor
import javafx.stage.Stage

class JavaFxUtil {
    object ImageTools {
        fun centerImage(imgView: ImageView) {
            val img = imgView.image
            if (img != null) {
                var w = 0.0
                var h = 0.0
                val ratioX = imgView.fitWidth / img.width
                val ratioY = imgView.fitHeight / img.height
                var reducCoeff = 0.0
                reducCoeff = Math.min(ratioX, ratioY)
                w = img.width * reducCoeff
                h = img.height * reducCoeff
                imgView.x = (imgView.fitWidth - w) / 2
                imgView.y = (imgView.fitHeight - h) / 2
            }
        }
    }

    object WindowTools {
        fun setWindowIcon(stage: Stage) {
            stage.icons.add(Image(Silicon::class.java.getResourceAsStream("icon.png")))
        }
    }

    object HtmlEditorTools {
        fun clearToolKits(htmlEditor: HTMLEditor) {
            val nodes = htmlEditor.lookupAll(".tool-bar").toTypedArray()
            for (node in nodes) {
                node.isVisible = false
                node.isManaged = false
            }
        }
    }

    object DialogTools {
        fun warnDialog(message: String?) {
            alert(AlertType.WARNING, message, ButtonType.OK).show()
        }

        fun errorDialog(message: String?) {
            alert(AlertType.ERROR, message, ButtonType.OK).show()
        }

        fun informationDialog(message: String?) {
            alert(AlertType.INFORMATION, message, ButtonType.OK).show()
        }

        fun noneDialog(message: String?) {
            alert(AlertType.NONE, message, ButtonType.OK).show()
        }

        fun unsupportOperationDialog() {
            errorDialog("Unsupported Operation.")
        }

        fun setIcon(dialog: Dialog<*>) {
            // Set Icon
            (dialog.dialogPane.scene.window as Stage).icons.add(
                Image(
                    Silicon::class.java.getResourceAsStream("icon.png")
                )
            )
        }

        fun alert(alertType: AlertType?, contentText: String?, vararg buttonTypes: ButtonType?): Alert {
            val a = Alert(alertType, contentText, *buttonTypes)
            setIcon(a)
            return a
        }
    }
}