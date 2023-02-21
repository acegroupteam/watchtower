package org.acegroup.watchtower.web.autoconfigure;

import org.acegroup.watchtower.web.util.EasyProfilerUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = EasyProfilerUtil.EASY_PROFILER_PREFIX)
public class EasyProfilerConfigurationProperties {


    private boolean enableBasic = true;

    private String username = "profiler";

    private String password = "profiler";

    private String basePackage = "";

    private String excludeClass = "";


    public boolean isEnableBasic() {
        return enableBasic;
    }

    public void setEnableBasic(boolean enableBasic) {
        this.enableBasic = enableBasic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getExcludeClass() {
        return excludeClass;
    }

    public void setExcludeClass(String excludeClass) {
        this.excludeClass = excludeClass;
    }
}
