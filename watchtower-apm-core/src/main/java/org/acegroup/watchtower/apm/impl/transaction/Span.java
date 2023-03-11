package org.acegroup.watchtower.apm.impl.transaction;

import lombok.Data;

/**
 * 跨度(动态上下文) 描述计算机操作，跨度有一个名称，一个开始时间，一个持续时间，以及一组属性。
 * 标准操作是使用语义约定来描述的，但也有一些特定的应用属性，比如ProjectID,和AccountID,可以由应用开发者添加，
 * 跨度也是我们描述因果关系的方式，为了正确记录整个事务，我们需要知道哪些操作是由其它哪些操作触发的，
 * 为了做到这一点，我们需要给跨度增加三个属性:
 * TraceID:识别整个事务
 * SpanID：识别当前操作
 * ParentID：识别父操作
 * 这三个属性是openTelemetry的基础，通过添加这些属性，我们所有的事件才可以被组织成一个图，代表它们的因果关系，
 *
 * @author hexiangtao
 * @date 2023/3/11 18:51
 */
@Data
public class Span implements DynamicContext {


    /***
     * 当前跨度ID
     */
    private String id;

    /**
     * 父ID
     */
    private String parentId;


    /**
     * 跨度名称
     */
    private String name;


    /**
     * 开始时间
     */
    private Long timestamp;


    /**
     * 持续时间(微秒)
     */
    private long durationUs;


    /**
     * 事件的一组属性
     */
    private Attribute[] attributes;


    /***
     * 全局事务的traceId
     */
    private String traceId;

}
