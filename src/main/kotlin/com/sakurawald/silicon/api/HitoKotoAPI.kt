package com.sakurawald.silicon.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * 一言的API.
 */
@Suppress("LocalVariableName")
object HitoKotoAPI {
    /**
     * 获取一言Get请求的地址
     */
    private val requestURL: String
        get() = "https://v1.hitokoto.cn"
    /** 封装JSON数据  */// 构造JsonObject对象
    /** 解析JSON数据  */
    /** 获取JSON数据  */

    // 若未找到结果，则返回null
    /**
     * 返回随机的一句一言
     *
     * @return 获取到的句子, 失败返回Sentence空对象.
     */
    val randomSentence: Sentence
        get() {
            /** 获取JSON数据  */
            /** 获取JSON数据  */
            val JSON = randomSentence_JSON ?: return Sentence.nullSentence

            // 若未找到结果，则返回null
            /** 解析JSON数据  */

            val jo: JsonObject = JsonParser.parseString(JSON) as JsonObject // 构造JsonObject对象
            val response: JsonObject = jo.asJsonObject
            val id: Int = response.get("id").asInt
            val content: String = response.get("hitokoto").asString
            val type: String = response.get("type").asString
            val from: String = response.get("from").asString
            val creator: String = response.get("creator").asString
            val createdAt: String = response.get("created_at").asString

            /** 封装JSON数据  */
            val result = Sentence(
                id, content, type, from, creator,
                createdAt
            )
            logDebug("HitoKoto", "Get Sentence >> $result")
            return result
        }

    /** 关闭Response的body  */
    private val randomSentence_JSON: String?
        get() {
            logDebug("HitoKoto", "Get Random Sentence -> Run")
            var result: String? = null
            val client = OkHttpClient()
            val request: Request?
            val URL = requestURL
            logDebug("HitoKoto", "Request URL >> $URL")

            request = Request.Builder().url(URL).get().build()
            var response: Response? = null
            var JSON: String? = null
            try {
                response = client.newCall(request).execute()
                logDebug("HitoKoto", "Request Response >> $response")
                JSON = response.body!!.string()
                result = JSON
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }
            logDebug(
                "HitoKoto",
                "Get Random Sentence >> Response: JSON = $JSON"
            )
            /** 关闭Response的body  */
            if (response != null) {
                Objects.requireNonNull(response.body)!!.close()
            }
            return result
        }
}