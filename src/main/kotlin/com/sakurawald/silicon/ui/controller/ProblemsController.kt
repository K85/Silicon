package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.data.beans.*
import com.sakurawald.silicon.data.beans.request.ProblemsRequest
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.ui.controller.StatusController
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.io.IOException

class ProblemsController : Controller() {
    @FXML
    private var textfield_page: TextField? = null

    @FXML
    private var button_go: Button? = null

    @FXML
    private var button_prev_page: Button? = null

    @FXML
    private var button_next_page: Button? = null

    @FXML
    private var tableview_problems: TableView<Problem?>? = null

    @FXML
    private var tablecolumn_status: TableColumn<Problem, String?>? = null

    @FXML
    private var tablecolumn_id: TableColumn<Problem, String?>? = null

    @FXML
    private var tablecolumn_title: TableColumn<Problem, String?>? = null

    @FXML
    private var tablecolumn_ratio_ac_submit: TableColumn<Problem, String?>? = null

    @FXML
    private var tablecolumn_difficulty: TableColumn<Problem, String?>? = null

    @FXML
    private var tablecolumn_date: TableColumn<Problem, String?>? = null

    @FXML
    private var textfield_search: TextField? = null

    @FXML
    private var button_search: Button? = null
    @FXML
    fun button_search_onAction(event: ActionEvent?) {
        updateProblems(ProblemsRequest(mainAccount, textfield_page!!.text, textfield_search!!.text))
    }

    @FXML
    fun button_go_onAction(event: ActionEvent?) {
        updateProblems(ProblemsRequest(mainAccount, textfield_page!!.text))
    }

    @FXML
    fun button_next_page_onAction(event: ActionEvent?) {
        try {
            textfield_page!!.text = (textfield_page!!.text.toInt() + 1).toString()
        } catch (e: NumberFormatException) {
            textfield_page!!.text = Page.FIRST_PAGE
        }
        button_go_onAction(null)
    }

    @FXML
    fun button_prev_page_onAction(event: ActionEvent?) {
        try {
            textfield_page!!.text = (textfield_page!!.text.toInt() - 1).toString()
        } catch (e: NumberFormatException) {
            textfield_page!!.text = Page.FIRST_PAGE
        }
        button_go_onAction(null)
    }

    @FXML
    fun tableview_items_onMouseClicked(event: MouseEvent) {

        // Prevent NPE.
        if (tableview_problems!!.selectionModel.selectedItem == null) {
            return
        }

        // Double Left Click -> Show Problem Detail.
        if (event.clickCount == 2 && event.button == MouseButton.PRIMARY) {

            ProblemDetailController.showProblemDetailWindow(tableview_problems!!.selectionModel.selectedItem!!)
            App.Companion.appInstance.controller!!.textfield_problemID!!.setText(tableview_problems!!.selectionModel.selectedItem!!.problemID)
        }

        // Middle Click -> Set ProblemID.
        if (event.button == MouseButton.MIDDLE) {
            App.Companion.appInstance.controller!!.textfield_problemID!!.setText(tableview_problems!!.selectionModel.selectedItem!!.problemID)
        }

        // Double Right Click -> Query Submit History.
        if (event.clickCount == 2 && event.button == MouseButton.SECONDARY) {
            StatusController.Companion.showStatusWindow()
            App.Companion.statusInstance.controller
                ?.updateSubmitHistory(tableview_problems!!.selectionModel.selectedItem!!)
            App.Companion.statusInstance.controller
                ?.currentQueryProblemID = tableview_problems!!.selectionModel.selectedItem!!.problemID
            App.Companion.statusInstance.controller!!.currentQueryUserID = mainAccount!!.userID
        }
    }

    @FXML
    fun initialize() {
        /** Init TableView.  */
        // Bind Data.
        tablecolumn_status!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.problemStatus.toString()
            )
        }
        tablecolumn_id!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.problemID
            )
        }
        tablecolumn_title!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.problemTitle
            )
        }
        tablecolumn_ratio_ac_submit!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.rAS
            )
        }
        tablecolumn_difficulty!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.difficultyPercentage
            )
        }
        tablecolumn_date!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<Problem, String?> ->
            SimpleStringProperty(
                cellData.value.date
            )
        }

        // Color Display.
        tablecolumn_title!!.setCellFactory {
            object : TableCell<Problem?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!isEmpty) {
                        textFill = Color.BLUE
                    }
                    text = item
                }
            }
        }
        tablecolumn_status!!.setCellFactory {
            object : TableCell<Problem?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!isEmpty) {
                        if (ProblemStatus.ACCEPTED.same(this.item!!)) {
                            textFill = Color.GREEN
                        } else if (ProblemStatus.WRONG.same(this.item!!)) {
                            textFill = Color.RED
                        }
                    }
                    text = item
                }
            }
        }
        /** Support: Problem Search ?  */
        if (!currentActionSet.problemsAction!!.supportProblemSearch()) {
            textfield_search!!.isVisible = false
            button_search!!.isVisible = false
        }
    }

    @FXML
    fun textfield_search_onKeyReleased(event: KeyEvent) {
        /** Enter Key: Search.  */
        if (event.code == KeyCode.ENTER) {
            button_search_onAction(null)
        }
    }

    @FXML
    fun textfield_page_onKeyReleased(event: KeyEvent) {
        /** Enter Key: Go.  */
        if (event.code == KeyCode.ENTER) {
            button_go_onAction(null)
        }
    }

    fun updateProblems(problemsRequest: ProblemsRequest) {
        /** Check MainAccount.  */
        if (mainAccount == null) {
            Platform.runLater { DialogTools.errorDialog("Main-Account cannot be empty.") }
            return
        }
        Thread {
            val problemsResponse = currentActionSet.problemsAction!!.execute(problemsRequest)
            Platform.runLater {
                tableview_problems!!.items.clear()
                tableview_problems!!.items.addAll(problemsResponse!!.problems)
                tableview_problems!!.scrollTo(0)
            }
        }.start()
    }

    companion object {
        fun showProblemsWindow() {
            /** Support: ProblemsAction ?  */
            if (!currentActionSet.supportThisAction(currentActionSet.problemsAction)) {
                DialogTools.unsupportOperationDialog()
                return
            }
            /** Has Open Window: Problems ?  */
            if (!App.Companion.problemsInstance.isEmpty) {
                App.Companion.problemsInstance.stage!!.toFront()
                return
            }
            /** Open Window: Problems  */
            val loader = FXMLLoader(App::class.java.getResource("Problems.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Problems"
                stage.scene = scene
                stage.isResizable = false

                // Update JavaFX Instances.
                App.Companion.problemsInstance.updateInstance(loader, stage, loader.getController<ProblemsController>())

                // Re-move Window.
                App.Companion.appInstance.controller!!.followAppWindowMove()
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }

            // Add Listeners.
            stage.onCloseRequest =
                EventHandler { windowEvent: WindowEvent? -> App.Companion.problemsInstance.emptyInstance() }

            // Show Window.
            stage.show()
        }
    }
}