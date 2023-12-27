package org.dynamic.datasource.model;

import java.util.*;

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
    
    /***
     * 数据库(MySQL)动态数据源详细信息
     */
    private final static Map<String, DataBaseInfo> DATA_SOURCE_MAP = new HashMap<>();
    
    /***
     * mongo动态数据源详细信息
     */
    private final static Map<String, MongoDataBaseInfo> MONGO_SOURCE_MAP = new HashMap<>();
    
    private DynamicSourceInfo() {}
    
    public static List<String> getDataSourceIds() {
        return DATA_SOURCE_IDS;
    }
    
    public static void add(String id) {
        DATA_SOURCE_IDS.add(id);
    }
    
    public static void add(String... id) {
        DATA_SOURCE_IDS.addAll(Arrays.asList(id));
    }
    
    public static void add(String id, DataBaseInfo dataBaseInfo) {
        DATA_SOURCE_MAP.put(id, dataBaseInfo);
    }
    
    public static void add(String id, MongoDataBaseInfo mongoDataBaseInfo) {
        MONGO_SOURCE_MAP.put(id, mongoDataBaseInfo);
    }
    
    public static DataBaseInfo getDataBaseInfo(String id) {
        return DATA_SOURCE_MAP.get(id);
    }
    
    public static MongoDataBaseInfo getMongoDataBaseInfo(String id) {
        return MONGO_SOURCE_MAP.get(id);
    }
    
}
