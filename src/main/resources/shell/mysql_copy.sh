#!/bin/bash

# 测试参数  ./copy_database_sh.sh labscare_core labscare_core123 mysql 3306 labscare_root LabsCareDB@adminA6688 mysql "t_component_logs,t_temp_count_log"

# 设置源数据库和目标数据库名称
SRC_DB=$1
DST_DB=$2

# 设置默认的数据库连接参数
HOST=$3
PORT=$4
USER=$5
PASS=$6
CONTAINER_NAME=$7
IGNORE_LIST=$8

# 检查是否提供了源数据库和目标数据库名称参数
if [ "$#" -ne 8 ]; then
    echo "Usage: $0 source_database target_database mysql_host mysql_port user password CONTAINER_NAME IGNORE_LIST"
    exit 1
fi

# 使用IFS= 来临时更改IFS，以便正确分割数组字符串
IFS=, read -ra my_array <<< "$IGNORE_LIST"

docker exec $CONTAINER_NAME mysql -h$HOST -P$PORT -u$USER -p$PASS -e "CREATE DATABASE IF NOT EXISTS $DST_DB;"
if [ ${#my_array[@]} -eq 0 ]; then
	# 没有就全量复制
	docker exec $CONTAINER_NAME sh -c "mysqldump -h$HOST -P$PORT -u$USER -p$PASS $SRC_DB -R -E --triggers > $tmp_dir/$SRC_DB.sql"
	docker exec $CONTAINER_NAME sh -c "mysql -h$HOST -P$PORT -u$USER -p$PASS $DST_DB < $tmp_dir/$SRC_DB.sql --force"
else
	# 有需要分割数组, 做命令拼接
	STRUCTURE_ONLY=''
	FULL=''
    for element in "${my_array[@]}"; do
		# 重新赋值的时候好像不能加'$', 会被识别为获取执行命令的返回结果
		STRUCTURE_ONLY+=" $element"
		FULL+=" --ignore-table=$SRC_DB.$element"
		echo "$element"
	done
	docker exec $CONTAINER_NAME sh -c "mysqldump -h$HOST -P$PORT -u$USER -p$PASS -d $SRC_DB $STRUCTURE_ONLY > $tmp_dir/${SRC_DB}_structure.sql"
	docker exec $CONTAINER_NAME sh -c "mysql -h$HOST -P$PORT -u$USER -p$PASS $DST_DB < $tmp_dir/${SRC_DB}_structure.sql --force"
	docker exec $CONTAINER_NAME sh -c "mysqldump -h$HOST -P$PORT -u$USER -p$PASS $SRC_DB -R -E --triggers $FULL > $tmp_dir/${SRC_DB}_full.sql"
	docker exec $CONTAINER_NAME sh -c "mysql -h$HOST -P$PORT -u$USER -p$PASS $DST_DB < $tmp_dir/${SRC_DB}_full.sql --force"
fi
