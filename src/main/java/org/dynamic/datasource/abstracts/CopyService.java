package org.dynamic.datasource.abstracts;

import org.dynamic.datasource.model.DataBaseInfo;
import org.dynamic.datasource.model.DynamicSourceInfo;
import org.dynamic.datasource.model.MongoDataBaseInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 复制数据库默认实现
 * @author: JiangWH
 * @date: 2023/12/27 16:16
 * @version: 1.0.0
 */
@Component
public abstract class CopyService {
    
    @Value("${custom.dynamic.containerName.mysql:mysql}")
    private String mysqlContainerName;
    
    @Value("${custom.dynamic.containerName.mongodb:mongodb}")
    private String mongoContainerName;

    public Map<String, Object> copy(String sourceId, String targetId, String ignoreTable) throws Exception {
        assert DynamicSourceInfo.getDataSourceIds().contains(sourceId) : "sourceId is not in dataSourceList";
        Map<String, Object> result = new HashMap<>();
        copyMysql(sourceId, targetId, ignoreTable, result);
        copyMongo(sourceId, targetId, result);
        return result;
    }
    
    private void copyMysql(String sourceId, String targetId, String ignoreTable, Map<String, Object> result) throws Exception {
        DataBaseInfo dataBaseInfo = DynamicSourceInfo.getDataBaseInfo(sourceId);
    
        String sourceDataBaseName = dataBaseInfo.getDatabaseName();
        String targetDataBaseName = sourceDataBaseName.replace("_" + sourceId, "");
        targetDataBaseName = targetDataBaseName + "_" + targetId;
    
        String message = runShell("classpath:shell/mysql_copy.sh", sourceDataBaseName, targetDataBaseName, dataBaseInfo.getHost(),
                dataBaseInfo.getPort(), dataBaseInfo.getUsername(), dataBaseInfo.getPassword(), mysqlContainerName, ignoreTable);
        
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("dataBaseName", targetDataBaseName);
        result.put("mysql", map);
    }
    
    private void copyMongo(String sourceId, String targetId, Map<String, Object> result) throws Exception {
        MongoDataBaseInfo mongoDataBaseInfo = DynamicSourceInfo.getMongoDataBaseInfo(sourceId);
        copyMongo(sourceId, targetId, mongoDataBaseInfo.getDatabaseName(), mongoDataBaseInfo, result, "mongo");
        copyMongo(sourceId, targetId, mongoDataBaseInfo.getMongoProDatabase(), mongoDataBaseInfo, result, "mongoPro");
    }
    
    private void copyMongo(String sourceId, String targetId, String dataBaseName, MongoDataBaseInfo mongoDataBaseInfo,
                          Map<String, Object> result, String resultKey) throws Exception {
        //  mongodb数据库名称
        String targetMongoDataBaseName = dataBaseName.replace("_" + sourceId, "");
        targetMongoDataBaseName = targetMongoDataBaseName + "_" + targetId;
    
        String shellMessage = runShell("classpath:shell/mongodb_copy.sh", dataBaseName, targetMongoDataBaseName,
                mongoDataBaseInfo.getHost(), mongoDataBaseInfo.getPort(), mongoDataBaseInfo.getUsername(),
                mongoDataBaseInfo.getPassword(), mongoDataBaseInfo.getMongoAuthDatabase(), mongoContainerName);
    
        Map<String, Object> map = new HashMap<>();
        map.put("message", shellMessage);
        map.put("dataBaseName", targetMongoDataBaseName);
        result.put(resultKey, map);
    }
    
    private String runShell(String ...params) throws Exception {
        // 创建ProcessBuilder对象，并设置命令和参数
        List<String> commands = new ArrayList<>();
        commands.add("sh");
        commands.addAll(Arrays.asList(params));
    
        ProcessBuilder pb = new ProcessBuilder(commands);
    
        // 启动进程
        Process process = pb.start();
    
        // 获取进程的输出流和错误流
        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();
    
        // 读取输出和错误信息（如果需要）
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
    
        String line;
        StringBuilder infoLine = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            infoLine.append(line);
        }
    
        StringBuilder errorLine = new StringBuilder();
        while ((line = errorReader.readLine()) != null) {
            errorLine.append(line);
        }
    
        // 等待进程结束
        int exitCode = process.waitFor();
        return infoLine.toString() + errorLine.toString();
    }

}
