package com.lms.common.core.domain.entity;

import lombok.Data;

/**
 * @author whn
 */
@Data
public class Pair<K,V> {
    private K key;
    private V val;
}
