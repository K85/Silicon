package com.sakurawald.silicon.file

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.file.AccountsConfig_Data.AccountConfig
import java.util.*

class AccountsConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<AccountsConfig_Data>?
) : ConfigFile<AccountsConfig_Data>(path, fineName, configDataClass) {

    private val accountConfigs: ArrayList<AccountConfig>
        get() = FileManager.accountsConfig_file!!.getConfigDataClassInstance().accountArrayList

    /** Create AccountConfig IF not exist.  */
    val currentAccountConfig: AccountConfig
        get() {
            var currentAccountConfig = getAccountConfig(currentActionSet.actionSetName)
            /** Create AccountConfig IF not exist.  */
            if (currentAccountConfig == null) {
                currentAccountConfig = AccountConfig()
                currentAccountConfig.actionSet = currentActionSet.actionSetName
                FileManager.accountsConfig_file!!.accountConfigs.add(currentAccountConfig)
            }
            return currentAccountConfig
        }

    fun getAccountConfig(actionSetName: String?): AccountConfig? {
        for (accountConfig in FileManager.accountsConfig_file!!.getConfigDataClassInstance().accountArrayList) {
            if (actionSetName == accountConfig.actionSet) {
                return accountConfig
            }
        }
        return null
    }
}