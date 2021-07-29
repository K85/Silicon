package com.sakurawald.silicon.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import com.sakurawald.silicon.util.FileUtil
import java.io.*

/**
 * 描述一个配置文件对象.*/
@Suppress("MemberVisibilityCanBePrivate")
open class ConfigFile<DO>(filePath: String?, fileName: String?, configDataClass: Class<DO>?) {
    /**
     * 用于反射的配置文件Data对象*/
    private var configDataClass: Class<DO>? = null

    /**
     * @return 获取该配置文件的路径名*/
    private var filePath: String? = null

    /**
     * @return 获取该配置文件的文件名*/
    private var fileName: String? = null

    /**
     * 标注该对象是否已完成初始化
     */
    var initialized = false

    /**
     * 存储Data类的实例对象*/
    private var configDataClassInstance: DO? = null

    /**
     * @return 获取该配置文件的File对象*/
    private val file: File
        get() = File(filePath + fileName)

    /**
     * 创建该Data类的实例对>*/
    private fun createConfigDataClassInstance() {
        logDebug(
            "FileSystem",
            "Use Reflect to Create the instance of Data Class >> " + configDataClass!!.simpleName
        )
        try {
            configDataClassInstance = configDataClass!!.newInstance()
        } catch (e: Exception) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 在本地存储中创建空文件>*/
    fun createConfigEmptyFileOnDisk() {
        val file = File(filePath + fileName)
        file.parentFile.mkdirs()
        try {
            file.createNewFile()
        } catch (e: IOException) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 初始化方法. 一般在创建完该对象后, 立即调用init().
     */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class, IOException::class)
    fun init() {

        // 调用方法, 给该File的Data的静态变量进行赋值
        if (!isConfigFileExistOnDisk) {
            createConfigEmptyFileOnDisk()
            saveDefaultConfigToDisk()
        }
        logDebug(
            "FileSystem",
            "Load Local File to Memory >> $fileName", true
        )

        // 从本地存储加载相应的配置文件
        loadConfigToMemoryFromDisk()

        // Set Flag.
        initialized = true
    }

    /**
     * @return 判断该配置文件是否已经存在.*/
    val isConfigFileExistOnDisk: Boolean
        get() {
            val file = File(filePath + fileName)
            return file.exists()
        }

    /**
     * @return 获取该Data类的实例对象*/
    fun getConfigDataClassInstance(): DO {
        if (configDataClassInstance == null) {
            createConfigDataClassInstance()
        }
        return configDataClassInstance!!
    }

    /**
     * 从本地存储加载数据到内存.*/
    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun loadConfigToMemoryFromDisk() {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader(file))
            // 从<本地存储>中<读取JSON配置文件数据>
            configDataClassInstance = Gson().fromJson(
                reader,
                configDataClass
            )
        } catch (e: FileNotFoundException) {
            LoggerManager.reportException(e)
        } finally {

            // Load完毕立即close掉本地文件流, 以免资源占用.
            try {
                reader!!.close()
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }
        }
    }

    /**
     * 重新从本地存储加载数据到内存. 该方法会覆盖内存中已有的数据.*/
    fun reloadFile() {
        logDebug("FileSystem", "Start to Reload Config: $fileName")
        try {
            loadConfigToMemoryFromDisk()
        } catch (e: IllegalArgumentException) {
            LoggerManager.reportException(e)
        } catch (e: IllegalAccessException) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 保存配置文件到本地存储.*/
    fun saveMemoryConfigToDisk() {
        logDebug(
            "FileSystem",
            "Save Memory Data to Local File >> $fileName"
        )

        // 定义要写出的本地配置文件
        val file = File(filePath + fileName)
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(file)
            val gson: Gson = GsonBuilder().serializeNulls().create()
            val nowJson: String = gson.toJson(getConfigDataClassInstance())
            fos.write(nowJson.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 写出该配置文件的默认配置文件数据.*/
    @Throws(IllegalArgumentException::class, IllegalAccessException::class, IOException::class)
    fun saveDefaultConfigToDisk() {
        logDebug("FileSystem", "Start to Write Default ConfigFile Data >> $fileName")

        // 定义要写出的本地配置文件
        val file = File(filePath + fileName)
        val fos = FileOutputStream(file)
        val gson: Gson = GsonBuilder().serializeNulls().create()
        val defaultJson: String = gson.toJson(getConfigDataClassInstance())
        fos.write(defaultJson.toByteArray())
        fos.flush()
        fos.close()
    }

    companion object {
        /**
         * @return 该应用程序的配置文件存储路径.
         */
        val applicationConfigPath: String
            get() {
                var result: String?
                result = FileUtil.javaRunPath
                result = "$result\\Silicon\\"
                return result
            }
    }

    init {
        this.filePath = filePath
        this.fileName = fileName
        this.configDataClass = configDataClass
    }
}