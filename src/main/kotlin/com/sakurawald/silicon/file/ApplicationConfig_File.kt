package com.sakurawald.silicon.file

class ApplicationConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<ApplicationConfig_Data>?
) : ConfigFile<ApplicationConfig_Data>(path, fineName, configDataClass)