package com.sakurawald.silicon.file

class TempConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<*>?
) : ConfigFile(path, fineName, configDataClass) {
    /**
     * 具体实例
     */
    val specificDataInstance: TempConfig_Data?
        get() = super.getConfigDataClassInstance() as TempConfig_Data
}