#!/bin/bash

# 测试参数: mongodb_copy.sh labscare_core labscare_core123 mongodb 27017 labscare_root LabsCare_MongoDB@Admin6688 admin mongodb

# 检查是否提供了源数据库和目标数据库名称参数
if [ "$#" -ne 8 ]; then
    echo "Usage: $0 source_database target_database host port username password auth_database container_name"
    exit 1
fi

# 定义源数据库和目标数据库的名称
source_db=$1
target_db=$2

# 连接参数
mongo_host=$3
mongo_port=$4
mongo_username=$5
mongo_password=$6
authentication_database=$7
container_name=$8

# 定义临时数据目录
tmp_dir=$(mktemp -d)

# 使用mongodump备份源数据库
docker exec $container_name mongodump --host $mongo_host --port $mongo_port --username $mongo_username --password $mongo_password --authenticationDatabase $authentication_database --db $source_db --out $tmp_dir

# 使用mongorestore将备份的数据恢复到目标数据库
docker exec $container_name mongorestore --host $mongo_host --port $mongo_port --username $mongo_username --password $mongo_password --authenticationDatabase $authentication_database --drop --db $target_db $tmp_dir/$source_db

# 删除临时数据目录
rm -r $tmp_dir

echo "Database copy from $source_db to $target_db completed."