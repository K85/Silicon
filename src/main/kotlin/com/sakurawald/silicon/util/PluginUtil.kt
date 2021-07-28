package com.sakurawald.silicon.util

import com.sakurawald.silicon.Silicon
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.Problem
import com.sakurawald.silicon.data.beans.Problem.Companion.transPercentage
import com.sakurawald.silicon.data.beans.ProblemStatus
import com.sakurawald.silicon.data.beans.SubmitResult
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

object PluginUtil {
    val encodeTable = arrayOfNulls<String>(256)
    fun fastSetRAS(RAS_Text: String, targetProblem: Problem) {
        val AC = HttpUtil.betweenString(RAS_Text, "(", "/").toInt()
        val submit = HttpUtil.betweenString(RAS_Text, "/", ")").toInt()
        val ratio = AC.toDouble() / submit
        targetProblem.aC = AC
        targetProblem.submit = submit
        targetProblem.ratio = ratio
    }

    fun fastGetSubmitResponseList(
        HTML: String?,
        statusTableSelector: String?,
        tablecolumn_index_runID: Int,
        tablecolumn_index_userID: Int,
        tablecolumn_index_problemID: Int,
        tablecolumn_index_result: Int,
        tablecolumn_index_memory: Int,
        tablecolumn_index_time: Int,
        tablecolumn_index_language: Int,
        tablecolumn_index_codeLength: Int,
        tablecolumn_index_submitTime: Int
    ): ArrayList<SubmitResponse> {
        return fastGetSubmitResponseList(
            HTML, statusTableSelector, 1, tablecolumn_index_runID,
            tablecolumn_index_userID, tablecolumn_index_problemID, tablecolumn_index_result,
            tablecolumn_index_memory, tablecolumn_index_time, tablecolumn_index_language, tablecolumn_index_codeLength,
            tablecolumn_index_submitTime
        )
    }

    fun fastGetSubmitResponseList(
        HTML: String?,
        statusTableSelector: String?,
        skipColumns: Int,
        tablecolumn_index_runID: Int,
        tablecolumn_index_userID: Int,
        tablecolumn_index_problemID: Int,
        tablecolumn_index_result: Int,
        tablecolumn_index_memory: Int,
        tablecolumn_index_time: Int,
        tablecolumn_index_language: Int,
        tablecolumn_index_codeLength: Int,
        tablecolumn_index_submitTime: Int
    ): ArrayList<SubmitResponse> {
        val submitResponseList: ArrayList<SubmitResponse> = ArrayList<SubmitResponse>()
        val doc: Document = Jsoup.parse(HTML)
        val elements: Elements = doc.select(statusTableSelector)
        /** Status.  */
        elements.stream().skip(skipColumns.toLong()).forEach { element ->
            val submitResponse = SubmitResponse()
            if (tablecolumn_index_runID != -1) {
                val runID: String = element.child(tablecolumn_index_runID).text()
                submitResponse.runID = runID
            }
            if (tablecolumn_index_userID != -1) {
                val userID: String = element.child(tablecolumn_index_userID).text()
                // Sync SubmitResponse.Account with userID.
                submitResponse.submitAccount = Account(userID, null)
            }
            if (tablecolumn_index_problemID != -1) {
                val problemID: String = element.child(tablecolumn_index_problemID).text()
                submitResponse.problemID = problemID
            }
            if (tablecolumn_index_result != -1) {
                val result: SubmitResult =
                    Silicon.currentActionSet.getSubmitResult(element.child(tablecolumn_index_result).text())
                submitResponse.submitResult = result
            }
            if (tablecolumn_index_memory != -1) {
                val memory: String = element.child(tablecolumn_index_memory).text()
                submitResponse.memory = memory
            }
            if (tablecolumn_index_time != -1) {
                val time: String = element.child(tablecolumn_index_time).text()
                submitResponse.time = time
            }
            if (tablecolumn_index_language != -1) {
                val language: String = element.child(tablecolumn_index_language).text()
                submitResponse.language = language
            }
            if (tablecolumn_index_codeLength != -1) {
                val codeLength: String = element.child(tablecolumn_index_codeLength).text()
                submitResponse.codeLength = codeLength
            }
            if (tablecolumn_index_submitTime != -1) {
                val submitTime: String = element.child(tablecolumn_index_submitTime).text()
                submitResponse.submitTime = submitTime
            }
            submitResponseList.add(submitResponse)
        }
        return submitResponseList
    }

    fun fastGetProblemList(
        HTML: String?,
        problemListTableSelector: String?,
        td_amount_withProblemStatus: Int,
        tablecolumn_index_problemstatus_withProblemStatus: Int,
        tablecolumn_index_problemID_withoutProblemStatus: Int,
        tablecolumn_index_problemTitle_withoutProblemStatus: Int,
        tablecolumn_index_RAS_Text_withoutProblemStatus: Int,
        tablecolumn_index_ProblemDifficulty_withoutProblemStatus: Int,
        tablecolumn_index_ProblemDate_withoutProblemStatus: Int
    ): ArrayList<Problem> {
        return fastGetProblemList(
            HTML,
            problemListTableSelector,
            1,
            td_amount_withProblemStatus,
            tablecolumn_index_problemstatus_withProblemStatus,
            tablecolumn_index_problemID_withoutProblemStatus,
            tablecolumn_index_problemTitle_withoutProblemStatus,
            tablecolumn_index_RAS_Text_withoutProblemStatus,
            tablecolumn_index_ProblemDifficulty_withoutProblemStatus,
            tablecolumn_index_ProblemDate_withoutProblemStatus
        )
    }

    /**
     * 我知道这是个很糟糕的写法, 但是有效.
     */
    fun fastGetProblemList(
        HTML: String?,
        problemListTableSelector: String?,
        skipColumns: Int,
        td_amount_withProblemStatus: Int,
        tablecolumn_index_problemstatus_withProblemStatus: Int,
        tablecolumn_index_problemID_withoutProblemStatus: Int,
        tablecolumn_index_problemTitle_withoutProblemStatus: Int,
        tablecolumn_index_RAS_Text_withoutProblemStatus: Int,
        tablecolumn_index_ProblemDifficulty_withoutProblemStatus: Int,
        tablecolumn_index_ProblemDate_withoutProblemStatus: Int
    ): ArrayList<Problem> {
        val problemList: ArrayList<Problem> = ArrayList<Problem>()
        /** HTML Analyse.  */
        val doc: Document
        doc = Jsoup.parse(HTML)
        val problemListTable: Elements = doc.select(problemListTableSelector)

        // Skip TableHeader.
        problemListTable.stream().skip(skipColumns.toLong()).forEach { e ->

            // Construct Problem.
            val problem = Problem()
            if (tablecolumn_index_problemstatus_withProblemStatus != -1) {
                // Handle ProblemStatus and Delete Node.
                if (e.select("td").size == td_amount_withProblemStatus) {
                    problem.problemStatus = Silicon.currentActionSet.getProblemStatus(
                        e.child(
                            tablecolumn_index_problemstatus_withProblemStatus
                        ).html()
                    )
                    e.child(tablecolumn_index_problemstatus_withProblemStatus).remove()
                } else {
                    problem.problemStatus = ProblemStatus.NEVER_TRY
                }
            }
            if (tablecolumn_index_problemID_withoutProblemStatus != -1) {
                val problemID: String = e.child(tablecolumn_index_problemID_withoutProblemStatus).text()
                problem.problemID = problemID
            }
            if (tablecolumn_index_problemTitle_withoutProblemStatus != -1) {
                val problemTitle: String = e.child(tablecolumn_index_problemTitle_withoutProblemStatus).text()
                problem.problemTitle = problemTitle
            }
            if (tablecolumn_index_RAS_Text_withoutProblemStatus != -1) {
                val RAS_Text: String = e.child(tablecolumn_index_RAS_Text_withoutProblemStatus).text()
                fastSetRAS(RAS_Text, problem)
            }
            if (tablecolumn_index_ProblemDifficulty_withoutProblemStatus != -1) {
                val problemDifficulty: Double =
                    transPercentage(e.child(tablecolumn_index_ProblemDifficulty_withoutProblemStatus).text())
                problem.difficulty  = problemDifficulty
            }
            if (tablecolumn_index_ProblemDate_withoutProblemStatus != -1) {
                val problemDate: String = e.child(tablecolumn_index_ProblemDate_withoutProblemStatus).text()
                problem.date = problemDate
            }
            logDebug("Get Problem: $problem")
            problemList.add(problem)
        }
        return problemList
    }

    fun fastEncodeURL(URL: String): String {
        val sb = StringBuilder()
        for (element in URL) {
            sb.append(encodeTable[element.toInt() and 0xFF])
        }
        return sb.toString()
    }

    fun fastEncodeURL_V2(URL: String, charset: String): String {
        return URLEncoder.encode(URL, charset)
            .replace("+", "%20")
            .replace("%28", "(")
            .replace("%29", ")")
            .replace("%21", "!")
            .replace("%27", "'")
            .replace("%7E", "~")
    }

    fun fastEncodeHTML(rawHTML: String?, enc: String?): String? {
        return HttpUtil.encodeURL(rawHTML, enc)
    }

    fun fastDecodeHTML(rawHTML: String?, enc: String?): String? {
        return HttpUtil.decodeURL(rawHTML, enc)
    }

    fun fastRecodeHTML(rawHTML: String, fromCharset: String?, toCharset: String?): String {
        try {
            return String(rawHTML.toByteArray(charset(fromCharset!!)), charset(toCharset!!))
        } catch (e: UnsupportedEncodingException) {
            LoggerManager.reportException(e)
        }
        return rawHTML
    }

    init {
        /** Init encodeTable.  */
        for (i in 0..255) {
            if (i >= '0'.toInt() && i <= '9'.toInt() || i >= 'a'.toInt() && i <= 'z'.toInt() || i >= 'A'.toInt() && i <= 'Z'.toInt() || i == '-'.toInt() || i == '_'.toInt() || i == '.'.toInt() || i == '!'.toInt() || i == '*'.toInt() || i == '('.toInt() || i == ')'.toInt()) {
                encodeTable[i] = ""
            } else {
                encodeTable[i] = "%" + String.format("%02x", i).toUpperCase()
            }
        }
    }
}