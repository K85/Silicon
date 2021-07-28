package com.sakurawald.silicon.file

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.file.AccountsConfig_Data.AccountConfig
import java.util.*

class AccountsConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<*>?
) : ConfigFile(path, fineName, configDataClass) {
    val specificDataInstance: AccountsConfig_Data?
        get() = super.getConfigDataClassInstance() as AccountsConfig_Data
    val accountConfigs: ArrayList<AccountConfig>
        get() = FileManager.Companion.accountsConfig_file!!.specificDataInstance!!.accountArrayList

    /** Create AccountConfig IF not exist.  */
    val currentAccountConfig: AccountConfig
        get() {
            var currentAccountConfig = getAccountConfig(currentActionSet.actionSetName)
            /** Create AccountConfig IF not exist.  */
            if (currentAccountConfig == null) {
                currentAccountConfig = AccountConfig()
                currentAccountConfig.actionSet = currentActionSet.actionSetName
                FileManager.Companion.accountsConfig_file!!.accountConfigs.add(currentAccountConfig)
            }
            return currentAccountConfig
        }

    fun getAccountConfig(actionSetName: String?): AccountConfig? {
        for (accountConfig in FileManager.Companion.accountsConfig_file!!.specificDataInstance!!.accountArrayList) {
            if (actionSetName == accountConfig.actionSet) {
                return accountConfig
            }
        }
        return null
    }
}