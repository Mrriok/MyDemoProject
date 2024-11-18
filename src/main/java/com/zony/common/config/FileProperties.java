
package com.zony.common.config;

import com.zony.common.utils.CommonConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    private ElPath mac;

    private ElPath linux;

    private ElPath windows;

    public ElPath getPath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(CommonConstant.WIN)) {
            return windows;
        } else if(os.toLowerCase().startsWith(CommonConstant.MAC)){
            return mac;
        }
        return linux;
    }

    @Data
    public static class ElPath{
        private String path;
    }
}
