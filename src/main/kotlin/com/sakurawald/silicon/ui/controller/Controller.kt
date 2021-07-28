package com.sakurawald.silicon.ui.controller

open class Controller {
    /**
     * 在该方法中调用需要JavaFX界面完全加载完后才能执行的方法.
     * 注意: 需要自行手动调用afterInitialize().
     */
    open fun afterInitialize() {
        // Do nothing.
    }
}