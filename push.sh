path_jar="/sdcard/test"
path_so="/data/local/tmp"
adb push Root.jar $path_jar
adb push libs/armeabi/libjdmobilesecurity.so $path_so/
echo "push success .."
