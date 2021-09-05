package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Page
import com.sakurawald.silicon.data.beans.SubmitResult
import com.sakurawald.silicon.data.beans.request.StatusRequest
import com.sakurawald.silicon.data.beans.request.TraceRequest
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.data.beans.response.TraceResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.ui.controller.CompileDetailController
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import javafx.application.Platform

open class TraceAction : Action<TraceRequest, TraceResponse>() {
    override fun execute(requestBean: TraceRequest): TraceResponse {
        /** Trace This SubmitRequest.  */
        val traceSubmitRequest = requestBean.submitRequest

        /** Control ProgressBar.  */
        App.appInstance.controller?.showSubmitProgressBar()
        while (true) {
            /** Wait Remote Server.  */
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                LoggerManager.reportException(e)
            }
            /** Get Status.  */
            val statusRequest = StatusRequest(
                traceSubmitRequest.submitAccount,
                traceSubmitRequest.problemID,
                traceSubmitRequest.submitAccount.userID,
                Page.HOME_PAGE
            )
            val statusResponse = currentActionSet.statusAction!!.execute(statusRequest)
            val recentSubmitResponses = statusResponse.submitResponses
            val relevantSubmitResponse = currentActionSet.statusAction!!.getLatestSatisfiedSubmitResponse(
                recentSubmitResponses!!,
                traceSubmitRequest.submitAccount.userID!!,
                traceSubmitRequest.problemID
            )
            logDebug("Relevant SubmitResponse: $relevantSubmitResponse")
            if (relevantSubmitResponse == null) {
                Platform.runLater {
                    App.appInstance.controller!!.hideSubmitProgressBar()
                    DialogTools.errorDialog("TraceAction: Relevant SubmitResponse is Empty.")
                }
                return TraceResponse(null)
            }

            /** Handle ActionError.  */
            if (currentActionSet.handleActionError(relevantSubmitResponse)) {
                return TraceResponse(relevantSubmitResponse)
            }

            /** SubmitResult == Waiting ?  */
            if (relevantSubmitResponse.submitResult == SubmitResult.WAITING) {
                continue
            }
            Platform.runLater {
                /** Handle SubmitResult.  */
                handleRelevantSubmitResponse(relevantSubmitResponse)
                /** Control ProgressBar.  */
                App.appInstance.controller!!.hideSubmitProgressBar()
            }
            return TraceResponse(relevantSubmitResponse)
        }
    }

    @NECESSARY
    fun handleRelevantSubmitResponse(relevantSubmitResponse: SubmitResponse) {
        when (relevantSubmitResponse.submitResult) {
            SubmitResult.ACCEPTED -> DialogTools.noneDialog(relevantSubmitResponse.formatedString)
            SubmitResult.TIME_LIMIT_EXCEED,
            SubmitResult.MEMORY_LIMIT_EXCEED,
            SubmitResult.OUTPUT_LIMIT_EXCEED,
            SubmitResult.RUNTIME_ERROR,
            SubmitResult.PRESENTATION_ERROR,
            SubmitResult.WRONG_ANSWER,
            SubmitResult.WAITING -> DialogTools.errorDialog(relevantSubmitResponse.formatedString)
            SubmitResult.COMPILE_ERROR -> CompileDetailController.showCompileDetailWindow(relevantSubmitResponse)
            SubmitResult.UNKNOWN -> DialogTools.confirmationDialog(relevantSubmitResponse.formatedString)
            else -> DialogTools.warnDialog("You may be the victim of a pirated CPU.")
        }
    }
}