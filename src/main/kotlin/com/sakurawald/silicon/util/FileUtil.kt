package com.sakurawald.silicon.util

import java.io.File

object FileUtil {
    /**
     * @return 应用程序的运行路径.
     */
    val javaRunPath: String
        get() {
            return File("").absolutePath + File.separator
        }
}