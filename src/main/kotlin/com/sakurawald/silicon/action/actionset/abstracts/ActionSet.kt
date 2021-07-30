package com.sakurawald.silicon.action.actionset.abstracts

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.Silicon.subAccount
import com.sakurawald.silicon.action.abstracts.*
import com.sakurawald.silicon.annotation.AUTO_USE
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.annotation.OPTIONAL
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
 * 描述一组动作集.
 */
@Suppress("unused")
abstract class ActionSet {
    @NECESSARY
    abstract val submitAction: SubmitAction

    @NECESSARY
    abstract val loginAction: LoginAction
    abstract val statusAction: StatusAction?
    abstract val problemsAction: ProblemsAction?
    abstract val problemDetailAction: ProblemDetailAction?
    abstract val sourceDetailAction: SourceDetailAction?
    open val noticeAction: NoticeAction? = null
        get() {
            if (field == null) return NoticeAction()
            return field
        }
    abstract val compileDetailAction: CompileDetailAction?

    @NECESSARY
    open val traceAction: TraceAction? = null
        get() {
            if (field == null) return TraceAction()
            return field
        }
    abstract val baseURL: String
    abstract val actionSetName: String
    open val codeEditorURL: String
        get() = "http://clioude.space/"

    abstract val supportLanguages: ArrayList<Language>

    /**
     * 对获取的原始HTML代码进行适当的编码转换, 以支持中文文本, 防止乱码.
     * 如果当前OnlineJudge站点可以直接正常访问, 不需要编码转换, 该该方法直接抛出UnsupportedOperationException()即可.
     */
    @OPTIONAL
    open fun encodeHTML(rawHTML: String?): String? {
        throw UnsupportedOperationException()
    }

    /**
     * @see encodeHTML
     */
    @OPTIONAL
    open fun decodeHTML(rawHTML: String?): String? {
        throw UnsupportedOperationException()
    }

    /**
     * 对资源相对路径的转化.
     */
    @OPTIONAL
    open fun transferBaseURL(rawHTML: String): String {
        return rawHTML.replace("src=\"", "src=\"$baseURL")
            .replace("href=\"", "href=\"$baseURL")
    }

    override fun toString(): String {
        return actionSetName
    }

    @AUTO_USE
    fun supportThisAction(action: Action<*, *>?): Boolean {
        return action != null
    }

    @AUTO_USE
    open fun cloneAccount() {
        App.settingsInstance.controller!!.progressbar_clone_account!!.progress = ProgressBar.INDETERMINATE_PROGRESS
        App.settingsInstance.controller!!.button_clone_account!!.isDisable = true
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
                val code = sourceDetailResponse.source
                val languageID = getLanguage(submitResponse.language)!!.language_id
                val submitRequest = SubmitRequest(mainAccount!!, submitResponse.problemID, languageID, code!!)
                currentActionSet.submitAction.execute(submitRequest)

                // Submit Interval.
                try {
                    Thread.sleep((1000 * 5).toLong())
                } catch (e: InterruptedException) {
                    LoggerManager.reportException(e)
                }
            }
            Platform.runLater {
                App.settingsInstance.controller!!.progressbar_clone_account!!.progress = 0.0
                App.settingsInstance.controller!!.button_clone_account!!.isDisable = false
                DialogTools.informationDialog("Account Clone Done!")
            }
        }.start()
    }


    /**
     * 将结果字符串转化为SubmitResult对象.
     */
    @OPTIONAL
    open fun getSubmitResult(submitResult: String): SubmitResult {

        val submitResultText = submitResult.toLowerCase()
        when {
            submitResultText.contains("accept") -> return SubmitResult.ACCEPTED
            submitResultText.contains("wrong") -> return SubmitResult.WRONG_ANSWER
            submitResultText.contains("presentation") -> return SubmitResult.PRESENTATION_ERROR
            submitResultText.contains("runtime") -> return SubmitResult.RUNTIME_ERROR
            submitResultText.contains("time") -> return SubmitResult.TIME_LIMIT_EXCEED
            submitResultText.contains("memory") -> return SubmitResult.MEMORY_LIMIT_EXCEED
            submitResultText.contains("compile") || submitResultText.contains("compilation error") -> return SubmitResult.COMPILE_ERROR
            submitResultText.contains("wait")
                    || submitResultText.contains("queuing")
                    || submitResultText.contains("compiling")
                    || submitResultText.contains("running")
            -> return SubmitResult.WAITING
            submitResultText.contains("output") -> return SubmitResult.OUTPUT_LIMIT_EXCEED
            else -> return SubmitResult.UNKNOWN
        }
    }

    @OPTIONAL
    open fun getProblemStatus(problemStatus: String): ProblemStatus {
        return when {
            problemStatus.contains("ac") -> ProblemStatus.ACCEPTED
            problemStatus.contains("wrong") -> ProblemStatus.WRONG
            else -> ProblemStatus.NEVER_TRY
        }
    }

    @AUTO_USE
    open fun getLanguage(languageName: String?): Language? {
        for (language in supportLanguages) {
            if (language.language_name == languageName) return language
        }
        return null
    }

    /**
     * @return 是否存在ActionError.
     */
    @AUTO_USE
    open fun handleActionError(actionBean: ActionBean): Boolean {
        if (actionBean.isActionError()) {
            Platform.runLater { DialogTools.errorDialog(actionBean.actionError!!.errorMessage) }
            return true
        }
        return false
    }

    /**
     * @return 副账号的提交是否通过.
     */
    @AUTO_USE
    open fun testTwoStepSubmit(subaccountSubmitRequest: SubmitRequest): Boolean {
        /** SubAccount -> Call: SubmitAction.  */
        currentActionSet.submitAction.execute(subaccountSubmitRequest)
        /** SubAccount -> Call: TraceAction.  */
        val traceRequest = TraceRequest(subaccountSubmitRequest)
        val traceResponse = currentActionSet.traceAction!!.execute(traceRequest)

        /** Does SubAccount Get Accepted without Any Error ? **/
        return !traceResponse.isActionError() && traceResponse.traceSubmitResponse != null && traceResponse.traceSubmitResponse!!.submitResult == SubmitResult.ACCEPTED
    }
}