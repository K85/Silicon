package com.sakurawald.silicon.file

import java.util.*

class AccountsConfig_Data {
    var accountArrayList = ArrayList<AccountConfig>()

    class AccountConfig {
        var actionSet: String? = null
        var mainAccount = MainAccount()

        inner class MainAccount {
            var userID: String? = null
            var password: String? = null
        }

        var subAccount = SubAccount()

        inner class SubAccount {
            var userID: String? = null
            var password: String? = null
            var two_step_submit = false
        }
    }
}