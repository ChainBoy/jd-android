#!/system/bin/sh
path="/sdcard/test"
export CLASSPATH=$path/Root.jar
app_process32 $path com.jingdong.app.Root --alltype&
#app_process32 $path com.jingdong.app.Root --debug --alltype&
