path="/sdcard/test"
javac -cp libs/android.jar src/com/jingdong/app/*
jar cvfm Root.jar MANIFEST.MF -C src/ .
d2j-jar2dex Root.jar -o classes.dex
zip -u -m Root.jar classes.dex
echo "build success .."
