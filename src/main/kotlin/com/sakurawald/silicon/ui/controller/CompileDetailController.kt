package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.data.beans.request.CompileDetailRequest
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

class CompileDetailController : WebViewController() {

    companion object {
        fun showCompileDetailWindow(submitResponse: SubmitResponse) {
            /** Support: CompileDetailAction ?  */
            val compileDetailAction = currentActionSet.compileDetailAction
            if (!currentActionSet.supportThisAction(compileDetailAction)) {
                DialogTools.unsupportOperationDialog()
                return
            }
            /** Open Window: CompileDetail.  */
            val loader = FXMLLoader(App::class.java.getResource("CompileDetail.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Compile Detail: " + submitResponse.runID
                stage.scene = scene
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }

            // Move Window.
            (loader.getController<Any>() as WebViewController).followAppWindow()

            // Show Window.
            stage.show()
            /** Update Source Detail  */
            Platform.runLater {

                // Request.
                val compileDetailRequest = CompileDetailRequest(mainAccount, submitResponse.runID)
                val compileDetailResponse = currentActionSet.compileDetailAction!!.execute(compileDetailRequest)

                // Update Webview.
                (loader.getController<Any>() as WebViewController).webview_core!!.engine.loadContent(compileDetailResponse!!.hTML)
            }
        }
    }


}