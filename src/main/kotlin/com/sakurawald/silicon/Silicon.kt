package com.sakurawald.silicon

import com.sakurawald.silicon.action.actionset.abstracts.ActionSet
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.file.FileManager
import com.sakurawald.silicon.plugin.PluginManager
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.FileUtil
import javafx.application.Application
import java.io.IOException


@Suppress("unused")
object Silicon {
    const val siliconVersion = "Khepri"
    private const val OPEN_SOURCE = "https://github.com/K85/silicon"

    /* Accounts. */
    @JvmStatic
    var mainAccount: Account? = null

    @JvmStatic
    var subAccount: Account? = null

    @JvmStatic
    val currentActionSet: ActionSet
        get() = App.appInstance.controller!!.combobox_actionSet!!.selectionModel.selectedItem!!

    @JvmStatic
    fun main(args: Array<String>) {
        /** Init Logger Path.  */
        val rootPath = FileUtil.javaRunPath
        System.setProperty("local_logger.base_path", rootPath)
        LoggerManager.logDebug("Start Application...", true)
        LoggerManager.logDebug("Init >> Start", true)
        /** Init FileSystem.  */
        try {
            FileManager.init()
        } catch (e: IllegalAccessException) {
            LoggerManager.reportException(e)
        } catch (e: IOException) {
            LoggerManager.reportException(e)
        }
        /** Load Plugins.  */
        LoggerManager.logDebug("Load Silicon Plugins", true)
        if (PluginManager.loadPlugins() == 0) {
            throw RuntimeException("No Plugin Found! (You Must Install At Least 1 Plugin.)")
        }
        LoggerManager.logDebug("Init >> End", true)
        LoggerManager.logDebug("JavaFX Application >> Launch", true)
        /** Launch JavaFX Application.  */
        Application.launch(App::class.java)
    }

    @JvmStatic
    fun updateAccounts() {
        /** Get CurrentAccountConfig.  */
        val currentAccountConfig = FileManager.accountsConfig_file!!.currentAccountConfig

        /** Set Accounts.  */
        val mainAccount = Account(
            currentAccountConfig.mainAccount.userID,
            currentAccountConfig.mainAccount.password
        )
        val subAccount = Account(
            currentAccountConfig.subAccount.userID,
            currentAccountConfig.subAccount.password
        )
        LoggerManager.logDebug("UpdateAccounts: MainAccount = $mainAccount, subAccount = $subAccount")
        Silicon.mainAccount = mainAccount
        Silicon.subAccount = subAccount
    }
}