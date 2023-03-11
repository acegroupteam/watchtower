package org.acegroup.watchtower.apm.impl.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hexiangtao
 * @date 2023/3/11 19:07
 */
@Data
@NoArgsConstructor
public class Service implements StaticContext {

    /**
     * 服务的逻辑名
     */
    private String name;

    /**
     * 服务实例ID
     */
    private String instanceId;

    /**
     * 服务API或实现的版本号
     */
    private String version;
}
