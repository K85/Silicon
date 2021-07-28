package com.sakurawald.silicon.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import com.sakurawald.silicon.util.FileUtil
import java.io.*

/**
 * 描述一个<配置文件对象>.
</配置文件对象> */
open class ConfigFile(filePath: String?, fileName: String?, configDataClass: Class<*>?) {
    /**
     * 用于<反射>的<配置文件Data对象>
    </配置文件Data对象></反射> */
    var configDataClass: Class<*>? = null

    /**
     * @return 获取<该配置文件>的<路径名>
    </路径名></该配置文件> */
    var filePath: String? = null

    /**
     * @return 获取<该配置文件>的<文件名>
    </文件名></该配置文件> */
    var fileName: String? = null

    /**
     * 标注该对象是否已完成初始化
     */
    var isHasInit = false

    /**
     * 存储<Data类的实例对象>
    </Data类的实例对象> */
    private var configDataClassInstance: Any? = null

    /**
     * 创建<该Data类的实例对象>
    </该Data类的实例对象> */
    fun createConfigDataClassInstance() {
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
     * 在<本地存储>中<创建空文件>.
    </创建空文件></本地存储> */
    fun createFile() {
        val file = File(filePath + fileName)
        file.getParentFile().mkdirs()
        try {
            file.createNewFile()
        } catch (e: IOException) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * @return 获取<该Data类的实例对象>
    </该Data类的实例对象> */
    fun getConfigDataClassInstance(): Any? {
        if (configDataClassInstance == null) {
            createConfigDataClassInstance()
        }
        return configDataClassInstance
    }

    /**
     * @return 获取<该配置文件>的<File对象>
    </File对象></该配置文件> */
    val file: File
        get() = File(filePath + fileName)

    /**
     * 初始化方法. 一般在创建完该对象后, 立即调用init().
     */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class, IOException::class)
    fun init() {

        // 调用方法, 给该File的Data的静态变量进行赋值
        if (isExist == false) {
            createFile()
            writeNormalFile()
        }
        LoggerManager.logDebug(
            "FileSystem",
            "Load Local File to Memory >> " + fileName, true
        )

        // 从本地存储加载相应的配置文件
        loadFile()

        // Set Flag.
        isHasInit = true
    }

    /**
     * @return 判断该<配置文件>是否已经存在.
    </配置文件> */
    val isExist: Boolean
        get() {
            val file = File(filePath + fileName)
            return file.exists()
        }

    /**
     * 从<本地存储>加载<数据>到<内存>.
    </内存></数据></本地存储> */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    fun loadFile() {
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
     * 重新从<本地存储>加载<数据>到<内存>. 该方法会<覆盖><内存>中<已有的数据>.
    </已有的数据></内存></覆盖></内存></数据></本地存储> */
    fun reloadFile() {
        logDebug("FileSystem", "Start to Reload Config: " + fileName)
        try {
            loadFile()
        } catch (e: IllegalArgumentException) {
            LoggerManager.reportException(e)
        } catch (e: IllegalAccessException) {
            LoggerManager.reportException(e)
        }
    }

    /**
     * 保存<配置文件>到本地存储.
    </配置文件> */
    fun saveFile() {
        logDebug(
            "FileSystem",
            "Save Memory Data to Local File >> " + fileName
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
     * 写出<该配置文件>的<默认配置文件数据>.
    </默认配置文件数据></该配置文件> */
    @Throws(IllegalArgumentException::class, IllegalAccessException::class, IOException::class)
    fun writeNormalFile() {
        logDebug("FileSystem", "Start to Write Default ConfigFile Data >> " + fileName)

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
         * @return 该应用程序的[配置文件存储路径].
         */
        val applicationConfigPath: String
            get() {
                var result: String? = null
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