package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.data.beans.Problem
import com.sakurawald.silicon.data.beans.request.ProblemDetailRequest
import com.sakurawald.silicon.data.beans.response.ProblemDetailResponse
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

class ProblemDetailController : WebViewController() {

    companion object {
        @JvmStatic
        fun showProblemDetailWindow(problem: Problem) {
            /** Support: ProblemDetailAction ?  */
            if (!currentActionSet.supportThisAction(currentActionSet.problemDetailAction)) {
                DialogTools.unsupportOperationDialog()
                return
            }
            /** Open Window: ProblemDetail.  */
            val loader = FXMLLoader(App::class.java.getResource("ProblemDetail.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Problem Detail: " + problem.problemID
                stage.scene = scene
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }


            // Move Window.
            (loader.getController<Any>() as WebViewController).followAppWindow()

            // Show Window.
            stage.show()

            // Request ProblemDetail.
            Thread {
                var problemDetailResponse: ProblemDetailResponse? = null
                try {
                    problemDetailResponse =
                        currentActionSet.problemDetailAction!!.execute(ProblemDetailRequest(problem))
                } catch (e: Exception) {
                    LoggerManager.reportException(e)
                }

                /** Update Problem Detail.  */
                Platform.runLater {
                    // Update WebView.
                    (loader.getController<Any>() as WebViewController).webview_core!!.engine.loadContent(
                        problemDetailResponse!!.HTML
                    )
                }
            }.start()

        }
    }


}