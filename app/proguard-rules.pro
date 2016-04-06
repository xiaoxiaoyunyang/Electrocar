# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhu/Downloads/adt-bundle-mac-x86_64-20131030/sdk/tools/proguard/proguard-android.txt
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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-dontoptimize
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keepattributes *Annotation*
-keepclasseswithmembernames class * {
	native <methods>;
}

-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
	public static **[] values();
	public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
	public static final android.os.Parcelable$Creator *;
}

#高德地图
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**  {*;}
-keep class com.autonavi.**  {*;}

#umeng
-dontwarn com.umeng.**
-keep class com.umeng*.** {*; }

#Msc
-dontwarn com.iflytek.**
-keep class com.iflytek.** { *; }

#阿里OSS
-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn org.apache.**
-keep class org.apache.** {*; }

#首页动画
-keep class com.aiba.haimaelc.widget.CircleGradientProgressView { *; }
-keep class com.aiba.haimaelc.widget.HoloCircleRoundProgressView { *; }

#model
-dontwarn com.aiba.haimaelc.model.**
-keep class com.aiba.haimaelc.model.** {*; }
-keepclasseswithmembernames class com.aiba.haimaelc.model.** {*;}

#gson
-keep class com.google.gson.** {*;}
-keep class sun.misc.Unsafe {*;}
-keepattributes Signature