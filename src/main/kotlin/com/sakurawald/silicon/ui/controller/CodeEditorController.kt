package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException

class CodeEditorController : WebViewController() {
    @FXML
    override fun initialize() {
        super.webview_core!!.engine.load(currentActionSet.codeEditorURL)
        super.webview_core!!.isContextMenuEnabled = false
    }

    companion object {
        fun showCodeEditor() {
            /** Open Window: CodeEditor.  */
            val loader = FXMLLoader(App::class.java.getResource("CodeEditor.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Code Editor"
                stage.scene = scene
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }

            // Show Window.
            stage.show()
        }
    }
}