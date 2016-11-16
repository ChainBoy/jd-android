path_jar="/sdcard/test"
path_so="/data/local/tmp"
adb push Root.jar $path_jar
adb push libs/armeabi/libjdmobilesecurity.so $path_so/
adb push run_jar.sh $path_jar
echo "push success .."
