package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.ui.controller.AppController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView

open class WebViewController {
    @FXML
    open var webview_core: WebView? = null

    open fun followAppWindow() {
        webview_core!!.scene.window.x = App.Companion.appInstance.stage!!.getX() + App.Companion.appInstance.stage!!
            .getWidth() - AppController.Companion.WINDOWS_INTERVAL
        webview_core!!.scene.window.y = App.Companion.appInstance.stage!!.getY()
    }

    open fun createContextMenu() {
        var webView = webview_core
        var contextMenu = ContextMenu()
        var reload = MenuItem("Reload")
        reload.onAction = EventHandler { e: ActionEvent? -> webView!!.engine.reload() }
        var savePage = MenuItem("Next Page")
        savePage.onAction = EventHandler { e: ActionEvent? ->
            try {
                webView!!.engine.history.go(1)
            } catch (exception: IndexOutOfBoundsException) {
                // Do nothing.
            }
        }
        var hideImages = MenuItem("Prev Page")
        hideImages.onAction = EventHandler { e: ActionEvent? ->
            try {
                webView!!.engine.history.go(-1)
            } catch (exception: IndexOutOfBoundsException) {
                // Do nothing.
            }
        }
        contextMenu.items.addAll(reload, savePage, hideImages)
        webView!!.onMousePressed = EventHandler { e: MouseEvent ->
            if (e.button == MouseButton.SECONDARY) {
                contextMenu.show(webView, e.screenX, e.screenY)
            } else {
                contextMenu.hide()
            }
        }
    }

    @FXML
    open fun initialize() {
        webview_core!!.isContextMenuEnabled = false
    }
}