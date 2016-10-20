# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\sdk/tools/proguard/proguard-android.txt
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
-dontskipnonpubliclibraryclasses
-verbose
-dontwarn
-ignorewarnings
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes InnerClasses,LineNumberTable

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference


################common###############
-keep class com.dashihui.afford.ui.model.** { *; } #实体类不参与混淆
-keep class com.dashihui.afford.business.entity.** { *; } #实体类不参与混淆
-keep class com.dashihui.afford.ui.widget.** { *; } #自定义控件不参与混淆
-keep class com.handmark.pulltorefresh.** {*;}
-keep class com.handmark.pulltorefresh.library.PullToRefreshListView { <init>(...); }
-keep class com.dashihui.afford.thirdapi.** { *; }#第三方包引用
-keep class com.dashihui.afford.sqlite.** { *; }#
-keep class com.dashihui.afford.common.base.** { *; }#

################解密 加密###############
#-libraryjars  libs/sunjce_provider.jar
-keep class android.net.http.SslError
-dontwarn com.sun.crypto.provider.**
-keep class com.sun.crypto.provider.** { *;}

####################support.v4   v7#####################
-keep class android.support.v4.** { *; }
-dontwarn android.support.v4.**
-keepclassmembers  class  *  extends  android.support.v4.app.Fragment {
    public void  *(android.view.View);
    public boolean *(android.view.View);
}

-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-dontwarn android.support.v7.**


################微信支付###############

# 腾讯云分析混淆
#-libraryjars libs/mta-mid-sdk-2.0.4.jar
#-libraryjars libs/mta-stats-sdk-2.0.4.jar
#-libraryjars libs/libammsdk.jar
-dontwarn com.tencent.**
-keep class com.tencent.**  {* ;}
-keep class com.tencent.stat.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

-keep class com.dashihui.afford.wxapi.** {*;}
-keep public class com.dashihui.afford.wxapi.WXPayEntryActivity { } #双向反馈功能代码不混淆

################第三方数据库处理p###############
#-libraryjars   libs/greendao-1.3.7.jar
-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *; }
-keep public class * extends de.greenrobot.dao.**


################baidu map###############

#-libraryjars libs/baidumapapi_base_v3_6_1.jar
#-libraryjars libs/baidumapapi_cloud_v3_6_1.jar
#-libraryjars libs/locSDK_6.12.jar
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**



################xutils##################
-keep class com.lidroid.xutils.** { *; }
-keep public class * extends com.lidroid.xutils.**
-keep class com.lidroid.xutils.task.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep public interface com.lidroid.xutils.** {*;}
-dontwarn com.lidroid.xutils.**


################支付宝##################
#-libraryjars libs/alipaySdk-20160223.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


################fastjson##################
#-libraryjars libs/fastjson-1.2.7.jar
-keep class com.alibaba.fastjson.** {*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.alibaba.fastjson.**

################flymeapi##########
#-libraryjars libs/flymeapi.jar
-keep class com.meizu.flyme.reflect.** {*;}
-dontwarn com.meizu.flyme.reflect.**

################处理图片glide-3.6.1##########
#-libraryjars libs/glide-3.6.1.jar
-keep class com.bumptech.glide.** {*;}
-keep class * extends com.bumptech.glide { *; }
-dontwarn com.bumptech.glide.**

################httpmime/httpcore##########
#-libraryjars libs/httpcore-4.3.2.jar
#-libraryjars libs/httpmime-4.3.5.jar
#-keep class org.apache.http.** {*;}
#-dontwarn org.apache.http.**

######################################
-keep class com.handmark.pulltorefresh.** {*;}
-keep class * extends com.handmark.pulltorefresh.** { *; }

-keep class com.ta.** {*;}
-keep class * extends com.ta.** { *; }
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}
 #不混淆R类
-keep public class com.dashihui.afford.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}



####################BASE64Decoder##################
#-libraryjars libs/sun.misc.BASE64Decoder.jar



###################other####################
# slidingmenu 的混淆
-dontwarn com.jeremyfeinstein.slidingmenu.lib.**
-keep class com.jeremyfeinstein.slidingmenu.lib.** { *; }
# ActionBarSherlock混淆
-dontwarn com.actionbarsherlock.**
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class android.webkit.WebView{*;}
-keep class android.webkit.WebSettings.** { *;}
-keep class com.dashihui.afford.ui.adapter.** { *;}
-keep class com.dashihui.afford.ui.activity.fragment.FragmentShopDetailPager.**{ *;}
-keep class com.dashihui.afford.ui.activity.fragment.FragmentServerDetailPager.**{ *;}

