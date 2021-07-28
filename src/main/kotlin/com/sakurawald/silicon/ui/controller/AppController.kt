package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.Silicon.subAccount
import com.sakurawald.silicon.Silicon.updateAccounts
import com.sakurawald.silicon.action.actionset.abstracts.ActionSet
import com.sakurawald.silicon.action.actionset.manager.ActionSetManager
import com.sakurawald.silicon.data.beans.*
import com.sakurawald.silicon.data.beans.request.NoticeRequest
import com.sakurawald.silicon.data.beans.request.ProblemsRequest
import com.sakurawald.silicon.data.beans.request.SubmitRequest
import com.sakurawald.silicon.data.beans.request.TraceRequest
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.file.*
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.io.IOException

class AppController : Controller() {
    @FXML
    private var button_submit: Button? = null

    @FXML
    var combobox_actionSet: ComboBox<ActionSet?>? = null

    @FXML
    var textfield_problemID: TextField? = null

    @FXML
    var combobox_language: ComboBox<Language?>? = null

    @FXML
    var textarea_code: TextArea? = null

    @FXML
    private var button_status: Button? = null

    @FXML
    private var button_problems: Button? = null

    @FXML
    private var button_settings: Button? = null

    @FXML
    var label_notice: Label? = null

    @FXML
    var progressbar_submit: ProgressBar? = null
    @FXML
    fun label_notice_onMouseClicked(event: MouseEvent?) {
        if (currentActionSet.supportThisAction(currentActionSet.noticeAction)) {
            Thread {
                /** Call: NoticeAction.  */
                /** Call: NoticeAction.  */
                currentActionSet.noticeAction!!.execute(NoticeRequest(label_notice!!, event))
            }
                .start()
        }
    }

    fun updateLanguages() {
        combobox_language!!.items.clear()
        combobox_language!!.items.addAll(currentActionSet.supportLanguages)
        combobox_language!!.selectionModel.select(0)
    }

    @FXML
    fun combobox_actionSet_onAction(event: ActionEvent?) {
        /** Update Languages.  */
        updateLanguages()
        /** Update Accounts.  */
        updateAccounts()
    }

    override fun afterInitialize() {
        /** Load Languages.  */
        updateLanguages()
        /** Load Memories.  */
        loadMemories()
        /** Update Accounts.  */
        updateAccounts()
    }

    @FXML
    fun button_problems_onAction(event: ActionEvent?) {
        // Show Window.
        ProblemsController.Companion.showProblemsWindow()

        // Update Problems.
        App.Companion.problemsInstance.controller!!.updateProblems(ProblemsRequest(mainAccount, Page.HOME_PAGE))
    }

    @FXML
    fun initialize() {
        /** Init Data.  */
        update_combobox_actionset()
        /** Init ProgressBar.  */
        progressbar_submit!!.progress = ProgressBar.INDETERMINATE_PROGRESS
        progressbar_submit!!.style = "-fx-accent: #66CCFF;"
    }

    fun update_combobox_actionset() {
        combobox_actionSet!!.items.clear()
        combobox_actionSet!!.items.addAll(ActionSetManager.loadedActionSet)
        combobox_actionSet!!.selectionModel.select(0)
    }

    /**
     * App Window Move Event.
     */
    fun followAppWindowMove() {

        // Has Open Problems.fxml ?
        if (!App.Companion.problemsInstance.isEmpty) {
            App.Companion.problemsInstance.stage!!.setX(
                App.Companion.appInstance.stage!!.getX() + App.Companion.appInstance.stage!!
                    .getWidth() - WINDOWS_INTERVAL
            )
            App.Companion.problemsInstance.stage!!.setY(App.Companion.appInstance.stage!!.getY())
        }

        // Has Open Status.fxml ?
        if (!App.Companion.statusInstance.isEmpty) {
            App.Companion.statusInstance.stage!!.setX(
                App.Companion.appInstance.stage!!.getX() + App.Companion.appInstance.stage!!
                    .getWidth() - WINDOWS_INTERVAL
            )
            App.Companion.statusInstance.stage!!.setY(App.Companion.appInstance.stage!!.getY())
        }

        // Has Open Settings.fxml ?
        if (!App.Companion.settingsInstance.isEmpty) {
            App.Companion.settingsInstance.stage!!.setX(App.Companion.appInstance.stage!!.getX())
            App.Companion.settingsInstance.stage!!.setY(App.Companion.appInstance.stage!!.getY())
        }
    }

    fun loadMemories() {
        loadMemory_ActionSet()
        loadMemory_ProblemID()
        loadMemory_Language()
        loadMemory_Code()
    }

    /**
     * 加载存储的使用习惯, 若对应的数据不存在, 则自动选中默认项.
     */
    fun loadMemory_ActionSet() {

        // Load ActionSet
        for (`as` in combobox_actionSet!!.items) {
            if (`as`!!.actionSetName == FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.selectedActionSet) {
                combobox_actionSet!!.selectionModel.select(`as`)
                return
            }
        }
        combobox_actionSet!!.selectionModel.select(0)
    }

    fun loadMemory_Language() {

        // Load Language
        for (lang in combobox_language!!.items) {
            if (lang!!.language_name == FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.selectedLanguage) {
                combobox_language!!.selectionModel.select(lang)
                return
            }
        }
        combobox_language!!.selectionModel.select(0)
    }

    fun loadMemory_ProblemID() {
        textfield_problemID!!.text =
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.inputProblemID
    }

    fun loadMemory_Code() {
        textarea_code!!.text =
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.inputCode
    }

    @FXML
    fun button_settings_onAction(event: ActionEvent?) {

        val loader = FXMLLoader(App::class.java.getResource("Settings.fxml"))
        val stage = Stage()
        try {
            val root = loader.load<Parent>()
            val scene = Scene(root)
            WindowTools.setWindowIcon(stage)
            stage.scene = scene
            stage.initModality(Modality.APPLICATION_MODAL)
            stage.title = "Settings"
            stage.isResizable = false

            // Update JavaFX Instances.
            App.Companion.settingsInstance.updateInstance(loader, stage, loader.getController<SettingsController>())
        } catch (e: IOException) {
            LoggerManager.reportException(e)
        }

        // Add Listeners.
        stage.onCloseRequest = EventHandler { windowEvent: WindowEvent? ->
            // Save All Settings.
            App.Companion.settingsInstance.controller!!.saveAllSettings()

            // Update.
            App.Companion.settingsInstance.emptyInstance()
        }

        // Move Window.
        followAppWindowMove()

        // Show Window.
        stage.show()
    }

    @FXML
    fun button_status_onAction(event: ActionEvent?) {

        // Show Window.
        StatusController.Companion.showStatusWindow()

        // Set Status Control Params.
        App.Companion.statusInstance.controller!!.currentQueryProblemID = null
        App.Companion.statusInstance.controller!!.currentQueryUserID = null

        // Update Status.
        App.Companion.statusInstance.controller!!.button_home_page_onAction(null)
    }

    private fun checkInput(): Boolean {

        // Option: ActionSet.
        if (combobox_actionSet!!.selectionModel.selectedItem == null) {
            Platform.runLater { DialogTools.errorDialog("ActionSet cannot be empty.") }
            return false
        }

        // Option: Language.
        if (combobox_language!!.selectionModel.selectedItem == null) {
            Platform.runLater { DialogTools.errorDialog("Language cannot be empty.") }
            return false
        }

        // Option: ProblemID.
        if (textfield_problemID!!.text == null
            || textfield_problemID!!.text.trim { it <= ' ' }.isEmpty()
        ) {
            Platform.runLater { DialogTools.errorDialog("ProblemID cannot be empty.") }
            return false
        }

        // Option: MainAccount.
        if (Account.Companion.isEmpty(mainAccount)) {
            Platform.runLater { DialogTools.errorDialog("Main-Account cannot be empty.") }
            return false
        }
        return true
    }

    @FXML
    fun button_submit_onMouseClicked(event: MouseEvent) {
        /** Double Right Click: Show CodeEditor.  */
        if (event.button == MouseButton.SECONDARY
            && event.clickCount == 2
        ) {
            CodeEditorController.Companion.showCodeEditor()
        }
    }

    @FXML
    fun button_submit_onAction(event: ActionEvent?) {
        /** Check Input.  */
        if (!checkInput()) {
            return
        }
        Thread(label@ Runnable {
            /** Control ProgressBar.  */
            /** Control ProgressBar.  */
            App.Companion.appInstance.controller!!.showSubmitProgressBar()
            /** Call: NoticeAction.  */
            /** Call: NoticeAction.  */
            if (currentActionSet.supportThisAction(currentActionSet.noticeAction)) {
                currentActionSet.noticeAction!!.execute(
                    NoticeRequest(
                        App.Companion.appInstance.controller!!.label_notice!!,
                        true
                    )
                )
            }
            /** Get Data.  */
            /** Get Data.  */
            val language = combobox_language!!.selectionModel.selectedItem!!.language_id
            val code = textarea_code!!.text
            // Trim ProblemID.
            val problemID = textfield_problemID!!.text.trim { it <= ' ' }
            val submitAccount: Account?
            var submitRequest: SubmitRequest
            /** Two-Step Submit.  */
            /** Two-Step Submit.  */
            if (FileManager.Companion.accountsConfig_file!!.getAccountConfig(currentActionSet.actionSetName)!!.subAccount.two_step_submit) {
                /** Check SubAccount.  */
                /** Check SubAccount.  */
                if (Account.Companion.isEmpty(subAccount)) {
                    Platform.runLater { DialogTools.errorDialog("Sub-Account cannot be empty.") }
                    App.Companion.appInstance.controller!!.hideSubmitProgressBar()
                    return@Runnable
                }
                /** Call: two_step_submit().  */
                /** Call: two_step_submit().  */
                submitRequest = SubmitRequest(subAccount!!, problemID, language, code)
                val submitAccountAccepted = currentActionSet.two_step_submit(submitRequest)
                if (!submitAccountAccepted) {
                    App.Companion.appInstance.controller!!.hideSubmitProgressBar()
                    return@Runnable
                }
            }
            /** MainAccount -> Call: SubmitAction  */
            /** MainAccount -> Call: SubmitAction  */
            submitAccount = mainAccount
            submitRequest = SubmitRequest(submitAccount!!, problemID, language, code)
            val submitResponse = currentActionSet.submitAction!!.execute(submitRequest)
            /** Handle SubmitError.  */
            /** Handle SubmitError.  */
            if (currentActionSet.handleSubmitError(submitResponse)) {
                return@Runnable
            }
            /** MainAccount -> Call: TraceAction.  */
            /** MainAccount -> Call: TraceAction.  */
            if (currentActionSet.supportThisAction(currentActionSet.traceAction)) {
                currentActionSet.traceAction!!.execute(TraceRequest(submitRequest))
            } else {
                hideSubmitProgressBar()
            }
        }).start()
    }

    fun showSubmitProgressBar() {
        progressbar_submit!!.isVisible = true
    }

    fun hideSubmitProgressBar() {
        progressbar_submit!!.isVisible = false
    }

    companion object {
        /**
         * JavaFX
         */
        const val WINDOWS_INTERVAL = 0
    }
}