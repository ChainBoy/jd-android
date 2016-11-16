#!/system/bin/sh
path="/sdcard/test"
export CLASSPATH=$path/Root.jar
app_process32 $path com.jingdong.app.Root &
#app_process32 $path com.jingdong.app.Root --debug &
