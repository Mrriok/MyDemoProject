package com.zony.common.filenet.util;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Factory;

public class ConnectionUtils {

    public static Connection getConnection() {
        return Factory.Connection.getConnection(FnConfigOptions.getContentEngineUrl());
    }
}
