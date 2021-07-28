package com.sakurawald.silicon.file

class ApplicationConfig_Data {
    var debug: Debug = Debug()

    inner class Debug {
        // Debug Mode
        var debug = false
    }
}