# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\SDK/com.yisipu.chartmap.tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn dk.**.**
-keep class  dk.**.**{ *;}
-dontwarn com.google.**
-keep class com.google.**{ *;}
-dontwarn com.android.**
-keep class com.android.**{ *;}
-dontwarn android.app.**
-keep class android.app.**{ *;}
-dontwarn com.yisipu.serialport.SerialPort
-keep class com.yisipu.serialport.SerialPort { *; }

-dontwarn com.qozix.tileview.**
-keep class com.qozix.tileview.** { *; }

-dontwarn android.**
-keep class android.** { *; }

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }
