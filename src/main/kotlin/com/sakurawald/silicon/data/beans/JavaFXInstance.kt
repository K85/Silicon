package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.ui.controller.*
import javafx.fxml.FXMLLoader
import javafx.stage.Stage

class JavaFXInstance<T : Controller?> {
    var loader: FXMLLoader? = null
        private set
    var stage: Stage? = null
        private set
    var controller: T? = null
        private set

    constructor() {
        // Do nothing.
    }

    val isEmpty: Boolean
        get() = loader == null && stage == null && controller == null

    fun updateInstance(loader: FXMLLoader?, stage: Stage?, controller: T) {
        this.loader = loader
        this.stage = stage
        this.controller = controller
    }

    fun emptyInstance() {
        loader = null
        stage = null
        controller = null
    }

    constructor(loader: FXMLLoader?, stage: Stage?, controller: T) {
        this.loader = loader
        this.stage = stage
        this.controller = controller
    }
}