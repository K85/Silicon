package com.sakurawald.silicon.ui.controller

import com.sakurawald.silicon.ui.App
import com.sakurawald.silicon.ui.controller.AppController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.web.WebView

@Suppress("PropertyName", "unused")
open class WebViewController {
    @FXML
    var webview_core: WebView? = null

    open fun followAppWindow() {
        webview_core!!.scene.window.x = App.appInstance.stage!!.x + App.appInstance.stage!!
            .width - AppController.WINDOWS_INTERVAL
        webview_core!!.scene.window.y = App.appInstance.stage!!.y
    }

    open fun createContextMenu() {
        val webView = webview_core
        val contextMenu = ContextMenu()
        val reload = MenuItem("Reload")
        reload.onAction = EventHandler { webView!!.engine.reload() }
        val savePage = MenuItem("Next Page")
        savePage.onAction = EventHandler {
            try {
                webView!!.engine.history.go(1)
            } catch (exception: IndexOutOfBoundsException) {
                // Do nothing.
            }
        }
        val hideImages = MenuItem("Prev Page")
        hideImages.onAction = EventHandler {
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