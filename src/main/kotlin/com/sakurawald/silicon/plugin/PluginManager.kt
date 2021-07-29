package com.sakurawald.silicon.plugin

import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import com.sakurawald.silicon.file.ConfigFile
import java.io.File
import java.net.URL
import java.net.URLClassLoader

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object PluginManager {
    /**
     * 从本地磁盘加载插件文件.
     * @return 成功加载的插件数量.
     */
    fun loadPlugins(): Int {
        val pluginsFolder = File(ConfigFile.applicationConfigPath + "\\Plugins")
        var loadedPluginCount = 0
        for (file in pluginsFolder.listFiles()) {
            if (!file.isFile) continue
            logDebug("ScanPlugin: " + file.name)
            try {
                val urls = arrayOf(URL("file:" + file.absolutePath))
                val ucl = URLClassLoader(urls)

                /** Load Plugin File: 要求Plugin File的FileName为SiliconPlugin类的全限定名.
                 * 且不带有后缀 .jar
                 */
                val clazz = ucl.loadClass(file.name) // Load Class.
                val onLoadMethod = clazz.getMethod("onLoad") // Get Method.
                onLoadMethod.invoke(clazz.getDeclaredConstructor().newInstance())
                loadedPluginCount++
            } catch (e: Exception) {
                LoggerManager.reportException(e)
            }
        }
        return loadedPluginCount
    }
}