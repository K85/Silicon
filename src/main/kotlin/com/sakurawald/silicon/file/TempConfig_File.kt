package com.sakurawald.silicon.file

class TempConfig_File(
    path: String?, fineName: String?,
    configDataClass: Class<TempConfig_Data>?
) : ConfigFile<TempConfig_Data>(path, fineName, configDataClass)