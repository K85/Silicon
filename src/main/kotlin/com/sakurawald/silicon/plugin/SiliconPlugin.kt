package com.sakurawald.silicon.plugin

@Suppress("unused")
abstract class SiliconPlugin {
    /**
     * Silicon Plugin在被加载到内存时所调用的方法,
     * 要求Plugin在改方法中添加自己的ActionSet.
     */
    abstract fun onLoad()
}