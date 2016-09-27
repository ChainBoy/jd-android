path="/sdcard/test"
#adb shell "/system/bin/dalvikvm -classpath $path/Root.jar  com.jingdong.app.Root 358239051596619-020000000000,2782638,1,10"
adb shell "export CLASSPATH=$path/Root.jar && app_process32 $path "
