package com.sakurawald.silicon.util

import java.io.File

object FileUtil {
    /**
     * 该方法也有以下几种实现原理:
     * String result = Class.class.getClass().getResource("/").getPath();
     * String result = System.getProperty("user.dir");
     */

    // 利用 new File()相对路径原理

    // 清理掉new File()产生的"头部/"

    // 相对路径 转 绝对路径
    /**
     * 可能的输出结果:
     * D:\LocalWorkSpace\Java\workspace\Silicon
     *
     * @return 应用程序的运行路径.
     */
    val javaRunPath: String
        get() {
            /**
             * 该方法也有以下几种实现原理:
             * String result = Class.class.getClass().getResource("/").getPath();
             * String result = System.getProperty("user.dir");
             */

            // 利用 new File()相对路径原理
            var result = File("").absolutePath + "/"

            // 清理掉new File()产生的"头部/"
            result = result.replaceFirst("/".toRegex(), "")

            // 相对路径 转 绝对路径
            result = result.replace("/", "\\")
            return result
        }
}