package com.sakurawald.silicon.util

import com.sakurawald.silicon.debug.LoggerManager
import java.awt.Desktop
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URISyntaxException
import java.net.URLDecoder
import java.net.URLEncoder

@Suppress("unused")
object HttpUtil {
    /**
     * Open URL.
     */
    fun openURL(URL: String?) {
        try {
            Desktop.getDesktop().browse(URI(URL))
        } catch (e: IOException) {
            LoggerManager.reportException(e)
        } catch (e: URISyntaxException) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 将普通字符创转换成application/x-www-from-urlencoded字符串.
     */
    fun encodeURL(URL: String?, enc: String?): String? {
        var urlString: String? = null
        try {
            urlString = URLEncoder.encode(URL, enc)
        } catch (e: UnsupportedEncodingException) {
            LoggerManager.reportException(e)
        }
        return urlString
    }

    /**
     * 将application/x-www-from-urlencoded字符串转换成普通字符串.
     */
    fun decodeURL(URL: String?, enc: String?): String? {
        var keyWord: String? = null
        try {
            keyWord = URLDecoder.decode(URL, enc)
        } catch (e: UnsupportedEncodingException) {
            LoggerManager.reportException(e)
        }
        return keyWord
    }

    fun betweenString(str: String, left: String, right: String?): String {
        return str.substring(str.indexOf(left) + left.length, str.indexOf(right!!))
    }

    fun decodeHTMLSpecialChars(HTML: String): String {
        return HTML.replace("&nbsp;", " ")
            .replace("&gl;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
    }

    fun encodeHTMLSpecialChars(HTML: String): String {
        return HTML.replace(" ", "&nbsp;")
            .replace("<", "&gl;")
            .replace(">", "&gt;")
            .replace("&", "&amp;")
            .replace("\"", "&quot;")
    }
}