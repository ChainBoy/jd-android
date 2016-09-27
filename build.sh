d2j-jar2dex Root.jar -o classes.dex && ls classes.dex
zip -u -m Root.jar classes.dex
adb push Root.jar "/sdcard/test"
