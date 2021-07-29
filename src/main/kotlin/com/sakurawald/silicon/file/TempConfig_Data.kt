package com.sakurawald.silicon.file

@Suppress("unused")
class TempConfig_Data {
    var siliconTempMemory = SiliconTempMemory()

    inner class SiliconTempMemory {
        var selectedActionSet: String? = null
        var inputProblemID: String? = null
        var selectedLanguage: String? = null
        var inputCode: String? = null
    }
}