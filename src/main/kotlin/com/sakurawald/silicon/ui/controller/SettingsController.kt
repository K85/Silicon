package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon
import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.updateAccounts
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.request.LoginRequest
import com.sakurawald.silicon.file.AccountsConfig_Data.AccountConfig
import com.sakurawald.silicon.file.FileManager
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType

@Suppress("PropertyName", "FunctionName", "MemberVisibilityCanBePrivate", "PrivatePropertyName")
class SettingsController : Controller() {
    @FXML
    var textfield_mainaccount_user: TextField? = null

    @FXML
     var titledpane_account_clone: TitledPane? = null

    @FXML
     var button_subaccount_test: Button? = null

    @FXML
     var button_mainaccount_test: Button? = null

    @FXML
     var textfield_subaccount_user: TextField? = null

    @FXML
    var button_clone_account: Button? = null

    @FXML
     var passwordfield_mainaccount_password: PasswordField? = null

    @FXML
     var passwordfield_subaccount_password: PasswordField? = null

    @FXML
    var progressbar_clone_account: ProgressBar? = null

    @FXML
     var checkbox_two_step_submit: CheckBox? = null

    @FXML
     var hyperlink_about_1: Hyperlink? = null

    @FXML
     var hyperlink_about_2: Hyperlink? = null

    @FXML
     var hyperlink_about_3: Hyperlink? = null

    @FXML
     var hyperlink_about_4: Hyperlink? = null

    @FXML
    fun button_mainaccount_test_onAction() {
        testAccountLogin(Account(textfield_mainaccount_user!!.text, passwordfield_mainaccount_password!!.text))
    }

    @FXML
    fun button_subaccount_test_onAction() {
        testAccountLogin(Account(textfield_subaccount_user!!.text, passwordfield_subaccount_password!!.text))
    }

    @FXML
    fun initialize() {
        /** Init CloneAccount ProgressBar.  */
        progressbar_clone_account!!.style = "-fx-accent: #66CCFF;"
        /** Load UIConfig.  */
        loadUIConfig()
        /** Load About.  */
        loadAbout()
        /** Support: Account Clone ?  */
        if (!currentActionSet.supportThisAction(currentActionSet.sourceDetailAction) || !currentActionSet.sourceDetailAction!!.supportAccountClone()) {
            titledpane_account_clone!!.isVisible = false
        }
    }

    fun loadAbout() {
        hyperlink_about_1?.text = "About: "
        hyperlink_about_2?.text = "Author: SakuraWald / K85"
        hyperlink_about_3?.text = "Email: SakuraWald@Gmail.com"
        hyperlink_about_4?.text = "Version: " + Silicon.siliconVersion
    }

    @FXML
    fun button_clone_account_onAction(event: ActionEvent) {
        val askAlert = Alert(AlertType.CONFIRMATION)
        DialogTools.setIcon(askAlert)
        askAlert.title = "Clone Account"
        askAlert.headerText = """
            Are you sure to clone account?
            WARNING: Disallow any operation during account clone.
            WARNING: The operation is indeterminate in different actionsets.
            """.trimIndent()
        val result = askAlert.showAndWait()
        if (result.get() == ButtonType.OK) {
            currentActionSet.cloneAccount()
        } else {
            event.consume()
        }
    }

    fun testAccountLogin(account: Account) {
        Thread {
            val loginRequest = LoginRequest(account)
            val loginResponse = currentActionSet.loginAction.execute(loginRequest)
            Platform.runLater {
                DialogTools.informationDialog(
                    """
    [Login Test] 
    UserID: ${account.userID}
    Token: ${loginResponse.token}
    LoginResponse: $loginResponse
    """.trimIndent()
                )
            }
        }.start()
    }

    fun saveUIConfig() {
        FileManager.accountsConfig_file!!.saveMemoryConfigToDisk()
    }

    fun loadUIConfig() {
        // Load ApplicationConfig.json and Update
        val currentAccountConfig: AccountConfig = FileManager.accountsConfig_file!!.currentAccountConfig
        textfield_mainaccount_user?.text = currentAccountConfig.mainAccount.userID
        passwordfield_mainaccount_password?.text = currentAccountConfig.mainAccount.password
        textfield_subaccount_user?.text = currentAccountConfig.subAccount.userID
        passwordfield_subaccount_password?.text = currentAccountConfig.subAccount.password
        checkbox_two_step_submit?.isSelected = currentAccountConfig.subAccount.twoStepSubmit
    }

    @FXML
    fun checkbox_two_step_submit_onAction(event: ActionEvent) {
        // Save UIConfig.
        val src = event.source as CheckBox
        FileManager.accountsConfig_file!!.currentAccountConfig.subAccount.twoStepSubmit = src.isSelected
        saveUIConfig()
    }

    @FXML
    fun textfield_mainaccount_user_onKeyTyped() {

        // Save UIConfig.
        FileManager.accountsConfig_file!!.currentAccountConfig.mainAccount.userID =
            textfield_mainaccount_user?.text

        saveUIConfig()

        // Update Account.
        updateAccounts()
    }

    @FXML
    fun passwordfield_mainaccount_password_onKeyTyped() {
        // Save UIConfig.
        FileManager.accountsConfig_file!!.currentAccountConfig.mainAccount.password =
            passwordfield_mainaccount_password?.text
        saveUIConfig()

        // Update Account.
        updateAccounts()
    }

    @FXML
    fun passwordfield_subaccount_password_onKeyTyped() {
        // Save UIConfig.
        FileManager.accountsConfig_file!!.currentAccountConfig.subAccount.password =
            passwordfield_subaccount_password?.text
        saveUIConfig()

        // Update Account.
        updateAccounts()
    }

    @FXML
    fun textfield_subaccount_user_onKeyTyped() {
        // Save UIConfig.
        FileManager.accountsConfig_file!!.currentAccountConfig.subAccount.userID =
            textfield_subaccount_user?.text
        saveUIConfig()

        // Update Account.
        updateAccounts()
    }

    fun saveAllSettings() {
        // Save UIConfig.
        val currentAccountConfig: AccountConfig = FileManager.accountsConfig_file!!.currentAccountConfig
        currentAccountConfig.mainAccount.userID = textfield_mainaccount_user?.text
        currentAccountConfig.mainAccount.password = passwordfield_mainaccount_password?.text
        currentAccountConfig.subAccount.userID = textfield_subaccount_user?.text
        currentAccountConfig.subAccount.password = passwordfield_subaccount_password?.text
        currentAccountConfig.subAccount.twoStepSubmit = checkbox_two_step_submit?.isSelected ?: false
        saveUIConfig()

        // Update Account.
        updateAccounts()
    }
}