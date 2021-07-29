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

@Suppress("MemberVisibilityCanBePrivate", "LocalVariableName", "unused", "DuplicatedCode", "FunctionName")
object PluginUtil {
    private val encodeTable = arrayOfNulls<String>(256)
    fun fastSetRAS(RAS_Text: String, targetProblem: Problem) {
        val AC = HttpUtil.betweenString(RAS_Text, "(", "/").toInt()
        val submit = HttpUtil.betweenString(RAS_Text, "/", ")").toInt()
        val ratio = AC.toDouble() / submit
        targetProblem.AC = AC
        targetProblem.submit = submit
        targetProblem.ratio = ratio
    }

    @JvmOverloads
    fun fastGetSubmitResponseList(
        HTML: String?,
        statusTableSelector: String?,
        skipRows: Int = 1,
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
        elements.stream().skip(skipRows.toLong()).forEach { element ->
            var runID: String? = null
            var userID: String? = null
            var problemID: String? = null
            var result: SubmitResult? = null
            var memory: String? = null
            var time: String? = null
            var language: String? = null
            var codeLength: String? = null
            var submitTime: String? = null

            if (tablecolumn_index_runID != -1) {
                runID = element.child(tablecolumn_index_runID).text()
            }
            if (tablecolumn_index_userID != -1) {
                userID = element.child(tablecolumn_index_userID).text()
            }
            if (tablecolumn_index_problemID != -1) {
                problemID = element.child(tablecolumn_index_problemID).text()
            }
            if (tablecolumn_index_result != -1) {
                result = Silicon.currentActionSet.getSubmitResult(element.child(tablecolumn_index_result).text())
            }
            if (tablecolumn_index_memory != -1) {
                memory = element.child(tablecolumn_index_memory).text()
            }
            if (tablecolumn_index_time != -1) {
                time = element.child(tablecolumn_index_time).text()
            }
            if (tablecolumn_index_language != -1) {
                language = element.child(tablecolumn_index_language).text()
            }
            if (tablecolumn_index_codeLength != -1) {
                codeLength = element.child(tablecolumn_index_codeLength).text()
            }
            if (tablecolumn_index_submitTime != -1) {
                submitTime = element.child(tablecolumn_index_submitTime).text()
            }

            val submitResponse = SubmitResponse(
                runID!!,
                Account(userID, null),
                problemID!!,
                language!!,
                memory,
                time,
                codeLength,
                submitTime,
                result!!
            )
            submitResponseList.add(submitResponse)
        }
        return submitResponseList
    }

    @JvmOverloads
    fun fastGetProblemList(
        HTML: String?,
        problemListTableSelector: String?,
        skipRows: Int = 1,
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
        val doc: Document = Jsoup.parse(HTML)
        val problemListTable: Elements = doc.select(problemListTableSelector)

        // Skip TableHeader.
        problemListTable.stream().skip(skipRows.toLong()).forEach { e ->

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
                problem.difficulty = problemDifficulty
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

    fun fastEncodeURL_V3(URL: String): String {
        val sb = StringBuilder()
        for (element in URL) {
            sb.append(encodeTable[element.toInt() and 0xFF])
        }
        return sb.toString()
    }

    /**
     * 其他编程语言通用的URLEncode标准.
     */
    fun fastEncodeURL_V2(URL: String, charset: String): String {
        return URLEncoder.encode(URL, charset)
            .replace("+", "%20")
            .replace("%28", "(")
            .replace("%29", ")")
            .replace("%21", "!")
            .replace("%27", "'")
            .replace("%7E", "~")
    }

    /**
     * Java语言的URLEncode标准
     */
    fun fastEncodeURL(rawHTML: String, enc: String?): String? {
        return HttpUtil.encodeURL(rawHTML, enc)
    }

    /**
     * @see fastEncodeURL
     */
    fun fastDecodeURL(rawHTML: String, enc: String?): String? {
        return HttpUtil.decodeURL(rawHTML, enc)
    }

    /**
     * HTML编码转换.
     */
    fun fastRecodeHTML(rawHTML: String, fromCharset: String?, toCharset: String?): String {
        try {
            return String(rawHTML.toByteArray(charset(fromCharset!!)), charset(toCharset!!))
        } catch (e: UnsupportedEncodingException) {
            LoggerManager.reportException(e)
        }
        return rawHTML
    }

    fun fastBase64Encode(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }

    fun fastBase64Decode(str: String): ByteArray? {
        return Base64.getDecoder().decode(str.toByteArray())
    }

    init {
        /** Init EncodeTable.  */
        for (i in 0..255) {
            if (i >= '0'.toInt() && i <= '9'.toInt() || i >= 'a'.toInt() && i <= 'z'.toInt() || i >= 'A'.toInt() && i <= 'Z'.toInt() || i == '-'.toInt() || i == '_'.toInt() || i == '.'.toInt() || i == '!'.toInt() || i == '*'.toInt() || i == '('.toInt() || i == ')'.toInt()) {
                encodeTable[i] = ""
            } else {
                encodeTable[i] = "%" + String.format("%02x", i).toUpperCase()
            }
        }
    }
}