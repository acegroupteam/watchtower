package org.acegroup.watchtower.apm.impl.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hexiangtao
 * @date 2023/3/11 19:01
 */
@Data
@NoArgsConstructor
public class Attribute implements Serializable {


    /**
     * 属性名
     */
    private String name;

    /**
     * 属性值
     */
    private String value;


    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
