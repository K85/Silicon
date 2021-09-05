package com.sakurawald.silicon.action.actionset.manager

import com.sakurawald.silicon.action.actionset.abstracts.ActionSet
import java.util.*

@Suppress("unused")
object ActionSetManager {
    /**
     * 已加载在内存中的ActionSet. (注意: 该属性并不会动态地与JavaFX UI中的ActionSet同步. JavaFX中的ActionSet仅在App初始化时被更新.)
     */
    val loadedActionSet = ArrayList<ActionSet>()

    fun addActionSet(actionSet: ActionSet) {
        loadedActionSet.add(actionSet)
    }
}