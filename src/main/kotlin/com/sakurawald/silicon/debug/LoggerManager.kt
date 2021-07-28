package com.sakurawald.silicon.debug

import com.sakurawald.silicon.file.*
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import org.apache.logging.log4j.LogManager
import java.io.*

object LoggerManager {
    const val SAD_FACIAL_EXPRESSION = "(ó﹏ò｡)"

    /**
     * 本地磁盘记录器对象.
     */
    val logger = LogManager.getLogger(
        LoggerManager::class.java
    )

    @JvmOverloads
    fun logDebug(type: String, msg: String, forceLog: Boolean = false) {
        logDebug("[$type] $msg", forceLog)
    }

    @JvmOverloads
    fun logDebug(content: String?, forceLog: Boolean = false) {
        if (forceLog) {
            logger.debug(content)
            return
        }

        // 如果不存在ApplicationConfig.json文件, 则默认开启Debug模式
        if (FileManager.Companion.applicationConfig_File == null ||
            !FileManager.Companion.applicationConfig_File!!.isHasInit ||
            FileManager.Companion.applicationConfig_File!!.specificDataInstance!!.debug.debug
        ) {
            logger.debug(content)
        }
    }

    fun logError(e: Exception) {
        logger.error(getExceptionInfo(e))
    }

    /**
     * 输出Exception到本地存储, 并且展示错误对话框.
     */
    fun reportException(e: Exception) {

        // 输出到<本地存储>
        logger.error(getExceptionInfo(e))

        // Show Dialog
        showErrorDialog(e)
    }

    fun getExceptionInfo(e: Exception): String {

        // 添加Exception基础信息
        return """
               错误类型: ${e.javaClass}
               原因: ${e.cause}
               消息: ${e.message}
               栈追踪: 
               ${getExceptionStack(e)}
               """.trimIndent()
    }

    fun getExceptionStack(e: Exception): String {
        val result = StringBuilder()
        for (s in e.stackTrace) {
            result.append("\tat ").append(s).append("\r\n")
        }
        return result.toString()
    }

    fun showErrorDialog(e: Exception) {
        Platform.runLater {
            val alert = Alert(AlertType.ERROR)
            DialogTools.setIcon(alert)
            alert.title = "错误对话框"
            alert.headerText = """
                哦不！一个错误发生了 $SAD_FACIAL_EXPRESSION
                您可以在"设置"中找到那个愚蠢的作者的联系方式？！！
                """.trimIndent()
            alert.contentText = """
                错误类型：${e.javaClass}
                错误原因：${e.cause}
                错误消息：${e.message}
                """.trimIndent()

            // Create expandable Exception.
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            val exceptionText = sw.toString()
            val label = Label("错误栈追踪：")
            val textArea = TextArea(exceptionText)
            textArea.isEditable = false
            textArea.isWrapText = true
            textArea.text = getExceptionStack(e)
            textArea.maxWidth = Double.MAX_VALUE
            textArea.maxHeight = Double.MAX_VALUE
            GridPane.setVgrow(textArea, Priority.ALWAYS)
            GridPane.setHgrow(textArea, Priority.ALWAYS)
            val expContent = GridPane()
            expContent.maxWidth = Double.MAX_VALUE
            expContent.add(label, 0, 0)
            expContent.add(textArea, 0, 1)

            // Set expandable Exception into the dialog pane.
            alert.dialogPane.expandableContent = expContent
            alert.show()
        }
    }
}