package com.sakurawald.silicon.file

import com.sakurawald.silicon.debug.LoggerManager
import java.io.File
import java.io.IOException

/**
 * 用于管理所有的本地配置文件，包括每一个配置文件的命名
 */
class FileManager private constructor() {
    /**
     * 调用本方法来<初始化>配置文件系统.
    </初始化> */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class, IOException::class)
    fun init() {
        LoggerManager.logDebug("FileSystem", "Init All Configs...", true)

        // Create Folders.
        LoggerManager.logDebug("FileSystem", "Create Plugins Folder", true)
        File(ConfigFile.Companion.applicationConfigPath + "\\Plugins").mkdirs()

        // ApplicationConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> ApplicationConfig.json", true)
        applicationConfig_File = ApplicationConfig_File(
            ConfigFile.Companion.applicationConfigPath,
            "ApplicationConfig.json", ApplicationConfig_Data::class.java
        )
        applicationConfig_File!!.init()

        // AccountsConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> AccountsConfig.json", true)
        accountsConfig_file = AccountsConfig_File(
            ConfigFile.Companion.applicationConfigPath,
            "AccountsConfig.json", AccountsConfig_Data::class.java
        )
        accountsConfig_file!!.init()

        // TempConfig.json
        LoggerManager.logDebug("FileSystem", "Init >> TempConfig.json", true)
        tempConfig_File = TempConfig_File(
            ConfigFile.Companion.applicationConfigPath,
            "TempConfig.json", TempConfig_Data::class.java
        )
        tempConfig_File!!.init()
    }

    companion object {
        /**
         * 单例模式
         */
        var instance: FileManager? = null
            get() {
                if (field == null) {
                    field = FileManager()
                }
                return field
            }
            private set

        /**
         * 配置文件列表
         */
        var applicationConfig_File: ApplicationConfig_File? = null
        var accountsConfig_file: AccountsConfig_File? = null
        var tempConfig_File: TempConfig_File? = null
    }
}