package com.sakurawald.silicon.action.actionset.abstracts

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.Silicon.subAccount
import com.sakurawald.silicon.action.abstracts.*
import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.*
import com.sakurawald.silicon.data.beans.request.SourceDetailRequest
import com.sakurawald.silicon.data.beans.request.SubmitRequest
import com.sakurawald.silicon.data.beans.request.TraceRequest
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import javafx.application.Platform
import javafx.scene.control.ProgressBar
import java.util.*

/**
 * 描述一组动作集
 */
abstract class ActionSet {
    @get:NECESSARY
    abstract val submitAction: SubmitAction?

    @get:NECESSARY
    abstract val loginAction: LoginAction?
    abstract val statusAction: StatusAction?
    abstract val problemsAction: ProblemsAction?
    abstract val problemDetailAction: ProblemDetailAction?
    abstract val sourceDetailAction: SourceDetailAction?
    abstract val noticeAction: NoticeAction?
    abstract val compileDetailAction: CompileDetailAction?

    @get:NECESSARY
    abstract val traceAction: TraceAction?
    abstract val baseURL: String

    /**
     * 对获取的原始HTML代码进行适当的编码转换, 以支持中文文本, 防止乱码.
     */
    open abstract fun encodeHTML(rawHTML: String?): String?
    open  abstract fun decodeHTML(rawHTML: String?): String?

    /**
     * 对资源相对路径的转化.
     */
    open fun transferBaseURL(rawHTML: String): String {
        return rawHTML.replace("src=\"", "src=\"$baseURL")
            .replace("href=\"", "href=\"$baseURL")
    }

    abstract val actionSetName: String
    open override fun toString(): String {
        return actionSetName
    }

    open fun supportThisAction(action: Action<*, *>?): Boolean {
        return action != null
    }

    open fun cloneAccount() {
        App.Companion.settingsInstance.controller!!.progressbar_clone_account!!.setProgress(ProgressBar.INDETERMINATE_PROGRESS)
        App.Companion.settingsInstance.controller!!.button_clone_account!!.setDisable(true)
        Thread {
            /** Get SubAccount's All History Status.  */
            /** Get SubAccount's All History Status.  */
            val historySubmitResponse: ArrayList<SubmitResponse?>?
            historySubmitResponse = currentActionSet.statusAction!!.getAllHistoryStatus(subAccount!!)
            logDebug("Account Clone >> historySubmitResponse.size() = " + historySubmitResponse.size)
            /** Get SubAccount's All Accepted Status.  */
            /** Get SubAccount's All Accepted Status.  */
            val accpetedSubmitResponse = ArrayList<SubmitResponse?>()
            for (submitResponse in historySubmitResponse) {
                if (submitResponse!!.submitResult == SubmitResult.ACCEPTED) {
                    accpetedSubmitResponse.add(submitResponse)
                }
            }
            logDebug("Account Clone >> accpetedSubmitResponse.size() = " + accpetedSubmitResponse.size)
            /** Get SourceDetail & Submit Source.   */
            /** Get SourceDetail & Submit Source.   */
            val problemSubmit = HashMap<String?, Boolean>()
            for (submitResponse in accpetedSubmitResponse) {

                // Booked ?
                if (problemSubmit.getOrDefault(submitResponse!!.problemID, false)) {
                    continue
                }
                problemSubmit[submitResponse.problemID] = true
                logDebug("Account Clone >> current SubmitResponse -> ProblemID = " + submitResponse.problemID)

                // Get SourceDetail.
                val sourceDetailRequest = SourceDetailRequest(subAccount, submitResponse.runID)
                val sourceDetailResponse = currentActionSet.sourceDetailAction!!.execute(sourceDetailRequest)
                logDebug("Account Clone >> current SourceDetailResponse = $sourceDetailResponse")

                // Submit Source.
                val code = sourceDetailResponse!!.source
                val language_id = getLanguage(submitResponse.language)!!.language_id
                val submitRequest = SubmitRequest(mainAccount!!, submitResponse.problemID!!, language_id, code!!)
                currentActionSet.submitAction!!.execute(submitRequest)

                // Submit Interval.
                try {
                    Thread.sleep((1000 * 5).toLong())
                } catch (e: InterruptedException) {
                    LoggerManager.reportException(e)
                }
            }
            Platform.runLater {
                App.Companion.settingsInstance.controller!!.progressbar_clone_account!!.setProgress(0.0)
                App.Companion.settingsInstance.controller!!.button_clone_account!!.setDisable(false)
                DialogTools.informationDialog("Account Clone Done!")
            }
        }.start()
    }

    val codeEditorURL: String
        get() = "http://clioude.space/"

    /**
     * 将结果字符串转化为SubmitResult对象.
     */
    open fun getSubmitResult(submitResult: String): SubmitResult {
        if (submitResult.toLowerCase().contains("accept")) {
            return SubmitResult.ACCEPTED
        }
        if (submitResult.toLowerCase().contains("wrong")) {
            return SubmitResult.WRONG_ANSWER
        }
        if (submitResult.toLowerCase().contains("presentation")) {
            return SubmitResult.PRESENTATION_ERROR
        }
        if (submitResult.toLowerCase().contains("runtime")) {
            return SubmitResult.RUNTIME_ERROR
        }
        if (submitResult.toLowerCase().contains("time")) {
            return SubmitResult.TIME_LIMIT_EXCEED
        }
        if (submitResult.toLowerCase().contains("memory")) {
            return SubmitResult.MEMORY_LIMIT_EXCEED
        }
        if (submitResult.toLowerCase().contains("compile")
            || submitResult.toLowerCase().contains("compilation error")
        ) {
            return SubmitResult.COMPILE_ERROR
        }
        if (submitResult.toLowerCase().contains("wait")
            || submitResult.toLowerCase().contains("queuing")
            || submitResult.toLowerCase().contains("compiling")
            || submitResult.toLowerCase().contains("running")
        ) {
            return SubmitResult.WAITING
        }
        return if (submitResult.toLowerCase().contains("output")) {
            SubmitResult.OUTPUT_LIMIT_EXCEED
        } else SubmitResult.UNKNOWN
    }

    abstract val supportLanguages: ArrayList<Language>
    open fun getProblemStatus(problemStatus: String): ProblemStatus {
        return if (problemStatus.contains("ac")) {
            ProblemStatus.ACCEPTED
        } else if (problemStatus.contains("wrong")) {
            ProblemStatus.WRONG
        } else {
            ProblemStatus.NEVER_TRY
        }
    }

    open fun getLanguage(language: String?): Language? {
        for (lang in supportLanguages) {
            if (lang.language_name == language) return lang
        }
        return null
    }

    /**
     * @return 是否遇到SubmitError.
     */
    open fun handleSubmitError(submitResponse: SubmitResponse?): Boolean {

        // 如果该指令没有返回SubmitResponse, 则默认认为该次提交成功.
        if (submitResponse == null) return false
        if (submitResponse.isActionError()) {
            Platform.runLater { DialogTools.errorDialog(submitResponse.actionError!!.errorMessage) }
            return true
        }
        return false
    }

    /**
     * @return 副账号的提交是否通过.
     */
    @AUTO
    open fun two_step_submit(subaccountSubmitRequest: SubmitRequest): Boolean {
        /** SubAccount -> Call: SubmitAction.  */
        currentActionSet.submitAction!!.execute(subaccountSubmitRequest)
        /** SubAccount -> Call: TraceAction.  */
        val traceRequest = TraceRequest(subaccountSubmitRequest)
        val traceResponse = currentActionSet.traceAction!!.execute(traceRequest)

        // 若TraceResponse为null, 则视为SubmitResponse失败.
        return traceResponse != null &&
                !traceResponse.isActionError()
    }
}