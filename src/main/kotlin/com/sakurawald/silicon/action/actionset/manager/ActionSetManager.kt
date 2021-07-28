package com.sakurawald.silicon.action.actionset.manager

import com.sakurawald.silicon.action.actionset.abstracts.ActionSet
import java.util.*

object ActionSetManager {
    /**
     * 已加载在内存中的ActionSet.
     * 请注意, loadedActionSets并不会动态地与JavaFX中显示的ActionSet选项进行同步.
     */
    val loadedActionSet = ArrayList<ActionSet>()
    fun addActionSet(actionSet: ActionSet) {
        loadedActionSet.add(actionSet)
    }
}