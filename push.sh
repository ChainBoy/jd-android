path_jar="/sdcard/test"
path_so="/data/local/tmp"
adb push Root.jar $path_jar
adb push ~/Downloads/jingdong/jingdong_3.9.9/lib/armeabi/libjdmobilesecurity.so $path_so/
echo "push success .."
