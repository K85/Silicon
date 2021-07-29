package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.Silicon.mainAccount
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.Page
import com.sakurawald.silicon.data.beans.Problem
import com.sakurawald.silicon.data.beans.SubmitResult
import com.sakurawald.silicon.data.beans.request.StatusRequest
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.util.JavaFxUtil.DialogTools
import com.sakurawald.silicon.util.JavaFxUtil.WindowTools
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
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

@Suppress("PrivatePropertyName", "FunctionName", "MemberVisibilityCanBePrivate", "PropertyName")
class StatusController : Controller() {
    /**
     * Status Control Params.
     */
    var currentQueryProblemID: String? = null
    var currentQueryUserID: String? = null

    @FXML
     var textfield_page: TextField? = null

    @FXML
     var button_go: Button? = null

    @FXML
     var button_prev_page: Button? = null

    @FXML
     var button_next_page: Button? = null

    @FXML
     var tableview_submit_results: TableView<SubmitResponse?>? = null

    @FXML
     var tablecolumn_run_id: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_user: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_problem: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_result: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_memory: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_time: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_language: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_code_length: TableColumn<SubmitResponse, String?>? = null

    @FXML
     var tablecolumn_submit_time: TableColumn<SubmitResponse, String?>? = null

    @FXML
    fun button_go_onAction() {
        update_status(StatusRequest(mainAccount, currentQueryProblemID, currentQueryUserID, textfield_page!!.text))
    }

    @FXML
    fun button_next_page_onAction() {
        /** Update Page Textfield.  */
        try {
            textfield_page!!.text = (textfield_page!!.text.toInt() + 1).toString()
        } catch (e: NumberFormatException) {
            textfield_page!!.text = Page.FIRST_PAGE
        }
        /** Call: StatusAction.  */
        if (currentActionSet.statusAction!!.supportStatusPageSkip()) {
            button_go_onAction()
        } else {
            update_status(StatusRequest(mainAccount, currentQueryProblemID, currentQueryUserID, Page.NEXT_PAGE))
        }
    }

    @FXML
    fun button_prev_page_onAction() {
        /** Update Page Textfield.  */
        try {
            val newPage = textfield_page!!.text.toInt() - 1

            // 当Status的页码为1时, 点击"Prev Page"将不会进行任何更新.
            if (newPage < 1) {
                return
            } else {
                textfield_page!!.text = newPage.toString()
            }
        } catch (e: NumberFormatException) {
            textfield_page!!.text = Page.FIRST_PAGE
        }
        /** Call: StatusAction.  */
        if (currentActionSet.statusAction!!.supportStatusPageSkip()) {
            button_go_onAction()
        } else {
            update_status(StatusRequest(mainAccount, currentQueryProblemID, currentQueryUserID, Page.PREV_PAGE))
        }
    }

    @FXML
    fun tableview_submit_results_onMouseClicked(event: MouseEvent) {

        // Prevent NPE.
        if (tableview_submit_results!!.selectionModel.selectedItem == null) {
            return
        }

        // Double Left Click -> Show ProblemDetail.
        if (event.clickCount == 2 && event.button == MouseButton.SECONDARY) {
            SourceDetailController.showSourceDetailWindow(tableview_submit_results!!.selectionModel.selectedItem!!)
        }

        // Double Right Click -> Show SourceDetail.
        if (event.clickCount == 2 && event.button == MouseButton.PRIMARY) {
            ProblemDetailController.showProblemDetailWindow(Problem(tableview_submit_results!!.selectionModel.selectedItem!!.problemID))
        }

        // Middle Click -> Show CompileDetail.
        if (event.button == MouseButton.MIDDLE) {
            CompileDetailController.showCompileDetailWindow(tableview_submit_results!!.selectionModel.selectedItem!!)
        }
    }

    @FXML
    fun button_home_page_onAction() {
        textfield_page!!.text = Page.FIRST_PAGE
        update_status(StatusRequest(mainAccount, currentQueryProblemID, currentQueryUserID, Page.HOME_PAGE))
    }

    fun updateSubmitHistory(problem: Problem) {
        textfield_page!!.text = Page.FIRST_PAGE
        update_status(StatusRequest(mainAccount, problem.problemID, mainAccount!!.userID, Page.HOME_PAGE))
    }

    @FXML
    fun initialize() {
        /** Init TableView.  */
        // Bind Data.
        tablecolumn_run_id!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.runID
            )
        }
        tablecolumn_user!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.submitAccount.userID
            )
        }
        tablecolumn_problem!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.problemID
            )
        }
        tablecolumn_result!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.submitResult.toString()
            )
        }
        tablecolumn_memory!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.memory
            )
        }
        tablecolumn_time!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.time
            )
        }
        tablecolumn_language!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.language
            )
        }
        tablecolumn_code_length!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.codeLength
            )
        }
        tablecolumn_submit_time!!.setCellValueFactory { cellData: TableColumn.CellDataFeatures<SubmitResponse, String?> ->
            SimpleStringProperty(
                cellData.value.submitTime
            )
        }

        // Color Display.
        tablecolumn_result!!.setCellFactory {
            object : TableCell<SubmitResponse?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!isEmpty) {
                        when {
                            SubmitResult.ACCEPTED.same(this.item!!) -> textFill = Color.BLUE
                            SubmitResult.COMPILE_ERROR.same(this.item!!) -> textFill = Color.GREEN
                            SubmitResult.RUNTIME_ERROR.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.PRESENTATION_ERROR.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.TIME_LIMIT_EXCEED.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.MEMORY_LIMIT_EXCEED.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.OUTPUT_LIMIT_EXCEED.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.WRONG_ANSWER.same(this.item!!) -> textFill = Color.RED
                            SubmitResult.WAITING.same(this.item!!) -> textFill = Color.GREEN
                            SubmitResult.UNKNOWN.same(this.item!!) -> textFill = Color.GRAY
                        }
                    }
                    text = item
                }
            }
        }
        tablecolumn_user!!.setCellFactory {
            object : TableCell<SubmitResponse?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!isEmpty) {
                        textFill = Color.BLUE
                    }
                    text = item
                }
            }
        }
        tablecolumn_problem!!.setCellFactory {
            object : TableCell<SubmitResponse?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (!isEmpty) {
                        textFill = Color.BLUE
                    }
                    text = item
                }
            }
        }
        /** Support PageSkip ?  */
        if (!currentActionSet.statusAction!!.supportStatusPageSkip()) {
            button_go!!.isVisible = false
            textfield_page!!.isDisable = true
        }
    }

    @FXML
    fun textfield_page_onKeyReleased(event: KeyEvent) {
        /** Enter Key: Go.  */
        if (event.code == KeyCode.ENTER && currentActionSet.statusAction!!.supportStatusPageSkip()) {
            button_go_onAction()
        }
    }

    fun update_status(statusRequest: StatusRequest) {
        /** Check MainAccount.  */
        if (Account.isEmpty(mainAccount)) {
            Platform.runLater { DialogTools.errorDialog("Main-Account cannot be empty.") }
            return
        }
        /** Call: StatusAction.  */
        Thread {
            val statusResponse = currentActionSet.statusAction!!.execute(statusRequest)
            Platform.runLater {
                tableview_submit_results!!.items.clear()
                tableview_submit_results!!.items.addAll(statusResponse.submitResponses!!)
                tableview_submit_results!!.scrollTo(0)
            }
        }.start()
    }

    companion object {
        fun showStatusWindow() {
            /** Support: StatusAction ?  */
            if (!currentActionSet.supportThisAction(currentActionSet.statusAction)) {
                DialogTools.unsupportOperationDialog()
                return
            }
            /** Has Open Window: Problems ?  */
            if (!App.statusInstance.isEmpty) {
                App.statusInstance.stage!!.toFront()
                return
            }
            /** Open Window: Problems  */
            val loader = FXMLLoader(App::class.java.getResource("Status.fxml"))
            val stage = Stage()
            try {
                val root = loader.load<Parent>()
                val scene = Scene(root)
                WindowTools.setWindowIcon(stage)
                stage.title = "Status"
                stage.scene = scene
                stage.isResizable = false

                // Update JavaFX Instances.
                App.statusInstance.updateInstance(loader, stage, loader.getController())
            } catch (e: IOException) {
                LoggerManager.reportException(e)
            }

            // Add Listeners.
            stage.onCloseRequest =
                EventHandler { App.statusInstance.emptyInstance() }

            // Re-move Window.
            App.appInstance.controller!!.followAppWindowMove()

            // Show Window.
            stage.show()
        }
    }
}