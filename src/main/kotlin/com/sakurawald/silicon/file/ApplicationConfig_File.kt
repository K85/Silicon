package com.sakurawald.silicon.file

class ApplicationConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<*>?
) : ConfigFile(path, fineName, configDataClass) {
    val specificDataInstance: ApplicationConfig_Data?
        get() = super.getConfigDataClassInstance() as ApplicationConfig_Data
}