package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.data.beans.request.SourceDetailRequest
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException

class SourceDetailController : WebViewController() {

    companion object {
        fun showSourceDetailWindow(submitResponse: SubmitResponse) {
            /** Support: SourceDetailAction ?  */
            if (!currentActionSet.supportThisAction(currentActionSet.sourceDetailAction)) {
                DialogTools.unsupportOperationDialog()
                return
            }
            /** Open Window: SourceDetail.  */
            val loader = FXMLLoader(App::class.java.getResource("SourceDetail.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Source Detail: " + submitResponse.runID
                stage.scene = scene
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }

            // Move Window.
            (loader.getController<Any>() as WebViewController).followAppWindow()

            // Show Window.
            stage.show()
            /** Update Source Detail.  */
            Platform.runLater {
                // Request.
                val sourceDetailRequest = SourceDetailRequest(mainAccount, submitResponse.runID)
                try {
                    val sourceDetailResponse = currentActionSet.sourceDetailAction!!.execute(sourceDetailRequest)
                    // Update Webview.
                    (loader.getController<Any>() as WebViewController).webview_core!!.engine.loadContent(
                        sourceDetailResponse.HTML
                    )
                } catch (e: Exception) {
                    LoggerManager.reportException(e)
                }

            }
        }
    }

}