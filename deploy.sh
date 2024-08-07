#!/bin/bash

if [ ! -n "$1" -o ! -n "$2" ]; then
  echo "Can not continue, please pass app name and port"
  exit 1
fi

is_runnable_app() {
  app_names=("server")
  for i in "${app_names[@]}"
  do
    [ "$i" == "$app_name" ] && return "1";
  done
  return "0"
}

[ "$(is_runnable_app)" == "0" ] && echo "can not run this app"

app_name="$1"
echo "app_name: ${app_name}"

app_version_name="$app_name-0.0.1-SNAPSHOT"
echo "app_version_name: ${app_version_name}"
port="$2"
current_path=$(pwd)
echo "Current path '$current_path'"

get_pid() {
  echo "$(lsof -i:$port | awk 'BEGIN {for (i=1;i<3;i++) {getline;pid=$2}} END {print pid}')"
}

get_resource_path() {
  echo "./helloscala-backend/${app_name}/build/distributions/${app_version_name}.tar"
  # echo $app_name | grep -q -E '\-site$' && echo "./${app_name}/build/distributions/${app_name}.tar"
}

echo "resource path:$(get_resource_path)"

kill_exist_app() {
  pid=$(get_pid)

  if [ -n "$pid" ]; then
    echo "Existing app runing on pid:$pid"
    kill $pid
    echo "Killed $pid"
  fi
  return 1
}

deploy_app() {
  echo "Starting deploy app: $1"
  rm -rf ./deployment/${app_version_name}
  mkdir -p ./deployment
  cp $(get_resource_path) ./deployment
  cd ./deployment/
  tar -xvf ./${app_version_name}.tar
  cd ${app_version_name}/

  nohup ./bin/${app_name} --server.port=$port --spring.profiles.active=prod \
  --wechat.appId=$WECHAT_APPID --wechat.secret=$WECHAT_SECRET --wechat.token=$WECHAT_TOKEN --wechat.aesKey=$WECHAT_AESKEY \
  --coze.accessToken=$COZE_ACCESS_TOKEN --coze.spaceId=$COZE_SPACE_ID --coze.botId=$COZE_BOT_ID \
   > ${app_version_name}.log 2>&1 &
  echo "Waiting startup..."
  for i in {1..60};
  do
    new_pid=$(get_pid)
    if [ ! -n "$new_pid" ]; then
        sleep 1
        echo -n "$i..."
    else
        echo "new pid: [$new_pid]"
        echo "Successfully start app: [${app_name}] on port [${port}], pid:[${new_pid}]"
        echo "Deploy app: [$app_name] success!"
        return 0
    fi
  done;
  if [ ! -n "$new_pid" ]; then
    echo "Failed to start app:${app_name}"
    exit 1
  fi
}

kill_exist_app
deploy_app $1