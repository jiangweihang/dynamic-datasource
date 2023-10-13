package org.dynamic.datasource.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: JiangWH
 * @date: 2023/10/10 11:22
 * @version: 1.0.0
 */
public class DynamicSourceInfo {
    
    /***
     * 数据库(MySQL)动态数据源id
     */
    private final static List<String> DATA_SOURCE_IDS = new ArrayList<>();
    
    public static List<String> getDataSourceIds() {
        return DATA_SOURCE_IDS;
    }
    
    public static void add(String id) {
        DATA_SOURCE_IDS.add(id);
    }
    
    public static void add(String... id) {
        DATA_SOURCE_IDS.addAll(Arrays.asList(id));
    }
    
}
