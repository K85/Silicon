package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.data.beans.*
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

abstract class TraceAction : Action<TraceRequest?, TraceResponse?>() {
    override fun execute(requestBean: TraceRequest?): TraceResponse? {
        /** Trace This SubmitRequest.  */
        val traceSubmitRequest = requestBean!!.submitRequest
        /** Control ProgressBar.  */
        App.Companion.appInstance.controller!!.showSubmitProgressBar()
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
            val recentSubmitResponses = statusResponse!!.submitResponses
            val relevantSubmitResponse = currentActionSet.statusAction!!.getFirstSatisfiedSubmitResponse(
                recentSubmitResponses!!,
                traceSubmitRequest.submitAccount.userID!!,
                traceSubmitRequest.problemID
            )
            logDebug("Relevant SubmitResponse: $relevantSubmitResponse")
            if (relevantSubmitResponse == null) {
                Platform.runLater {
                    App.Companion.appInstance.controller!!.hideSubmitProgressBar()
                    DialogTools.errorDialog("TraceAction: Relevant SubmitResponse is Empty.")
                }
                return null
            }
            /** Handle SubmitError.  */
            if (currentActionSet.handleSubmitError(relevantSubmitResponse)) {
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
                App.Companion.appInstance.controller!!.hideSubmitProgressBar()
            }
            return TraceResponse(relevantSubmitResponse)
        }
    }

    @AUTO
    fun handleRelevantSubmitResponse(relevantSubmitResponse: SubmitResponse) {
        val submitResult = relevantSubmitResponse.submitResult
        if (submitResult == SubmitResult.ACCEPTED
            || submitResult == SubmitResult.WAITING
        ) {
            DialogTools.informationDialog(relevantSubmitResponse.formatedString)
            return
        }
        if (submitResult == SubmitResult.TIME_LIMIT_EXCEED || submitResult == SubmitResult.MEMORY_LIMIT_EXCEED || submitResult == SubmitResult.OUTPUT_LIMIT_EXCEED || submitResult == SubmitResult.RUNTIME_ERROR || submitResult == SubmitResult.PRESENTATION_ERROR || submitResult == SubmitResult.WRONG_ANSWER || submitResult == SubmitResult.UNKNOWN) {
            DialogTools.errorDialog(relevantSubmitResponse.formatedString)
            return
        }
        if (submitResult == SubmitResult.COMPILE_ERROR) {
            CompileDetailController.showCompileDetailWindow(relevantSubmitResponse)
            return
        }
    }
}