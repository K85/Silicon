package com.sakurawald.silicon.ui

import com.sakurawald.silicon.data.beans.JavaFXInstance
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.file.*
import com.sakurawald.silicon.ui.controller.AppController
import com.sakurawald.silicon.ui.controller.ProblemsController
import com.sakurawald.silicon.ui.controller.SettingsController
import com.sakurawald.silicon.ui.controller.StatusController
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.application.Application
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.stage.Stage
import javafx.stage.WindowEvent

class App : Application() {
    @Throws(Exception::class)
    override fun start(appStage: Stage) {
        val loader = FXMLLoader(javaClass.getResource("App.fxml"))
        val root = loader.load<Parent>()
        appInstance.updateInstance(loader, appStage, loader.getController())
        WindowTools.setWindowIcon(appStage)
        appStage.title = "Silicon"
        appStage.scene = Scene(root)
        appStage.isResizable = false

        // Show Window.
        appStage.show()

        // Add Listeners.
        appStage.onCloseRequest = EventHandler { event: WindowEvent ->
            val askAlert = Alert(AlertType.CONFIRMATION)
            DialogTools.setIcon(askAlert)
            askAlert.title = "Exit"
            askAlert.headerText = "Are you sure to exit the program?"
            val result = askAlert.showAndWait()
            if (result.get() == ButtonType.OK) {
                // Call: BeforeJVMExit().
                LoggerManager.logDebug("Shutdown >> Start", true)
                beforeJVMEXit()
                LoggerManager.logDebug("Shutdown >> End", true)
                LoggerManager.logDebug("End Application...", true)

                // Exit JVM.
                System.exit(0)
            } else {
                event.consume()
            }
        }
        appStage.xProperty()
            .addListener { observableValue: ObservableValue<out Number?>?, number: Number?, t1: Number? -> appInstance.controller!!.followAppWindowMove() }
        appStage.yProperty()
            .addListener { observableValue: ObservableValue<out Number?>?, number: Number?, t1: Number? -> appInstance.controller!!.followAppWindowMove() }
        /** AfterInit.  */
        appInstance.controller!!.afterInitialize()
    }

    fun beforeJVMEXit() {
        /** Save Memories.  */
        val inputProblemID = appInstance.controller!!.textfield_problemID!!.text
        if (inputProblemID != null) {
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.inputProblemID =
                inputProblemID
        }
        val inputCode = appInstance.controller!!.textarea_code!!.text
        if (inputCode != null) {
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.inputCode = inputCode
        }
        val selectedActionSet = appInstance.controller!!.combobox_actionSet!!.selectionModel.selectedItem
        if (selectedActionSet != null) {
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.selectedActionSet =
                selectedActionSet.actionSetName
        }
        val selectedLanguage = appInstance.controller!!.combobox_language!!.selectionModel.selectedItem
        if (selectedLanguage != null) {
            FileManager.Companion.tempConfig_File!!.specificDataInstance!!.siliconTempMemory.selectedLanguage =
                selectedLanguage.language_name
        }
        FileManager.Companion.tempConfig_File!!.saveFile()
    }

    companion object {
        /**
         * JavaFXInstance: App
         */
        val appInstance = JavaFXInstance<AppController>()

        /**
         * JavaFXInstance: Problems
         */
        val problemsInstance = JavaFXInstance<ProblemsController>()

        /**
         * JavaFXInstance: Status
         */
        val statusInstance = JavaFXInstance<StatusController>()

        /**
         * JavaFXInstance: Settings
         */
        val settingsInstance = JavaFXInstance<SettingsController>()
    }
}