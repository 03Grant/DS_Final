package org.grant.server;
import org.grant.IPAddress;
import org.grant.IPSetting;

import java.util.HashMap;
import java.util.Map;

public class storageSetting {
    public static Map<IPAddress, String> serverStorageMap = new HashMap<>();

    static {
        // 添加服务器IP与文件夹名字的映射
        serverStorageMap.put(IPSetting.serverIP[0], "Ware/fileWare1/");
        serverStorageMap.put(IPSetting.serverIP[1], "Ware/fileWare2/");
        serverStorageMap.put(IPSetting.serverIP[2], "Ware/fileWare3/");
    }
}
