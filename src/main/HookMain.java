package main;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.widget.Toast;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import hook.Login;
import hook.Messagemanipulation;
import hook.PaymentAssistant;
import hook.Personalreceipt;
import hook.Reddot;
import hook.Redpacket;

import static com.example.activity.Setting.readUrl;

public class HookMain implements IXposedHookLoadPackage {
    private static String k = new String();
    public static ClassLoader c = null;
    public static LoadPackageParam params = null;
    public static boolean t = true;
    public static String userId = "";
    private boolean loaded = false;
    public static boolean ALIPAY_PACKAGE_ISHOOK = false;
    private static String ALIPAY_PACKAGE = "com.eg.android.AlipayGphone";


    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        // TODO Auto-generated method stub
        if (this.loaded) {
            XposedBridge.log("已经加载handleLoadPackage过了,无需再次加载");
            return;
        }
        this.loaded = true;
        if (lpparam.appInfo == null || (lpparam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM |
                ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
            return;
        }


        final String packageName = lpparam.packageName;
        final String processName = lpparam.processName;
        if (ALIPAY_PACKAGE.equals(packageName) && ALIPAY_PACKAGE.equals(processName)) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    final Context context = (Context) param.args[0];
                    final ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    if (ALIPAY_PACKAGE.equals(processName) && !ALIPAY_PACKAGE_ISHOOK) {
                        try {
                            //登录
                            synchronized (this) {
                                if (ALIPAY_PACKAGE_ISHOOK == false) {
                                    ALIPAY_PACKAGE_ISHOOK = true;
                                    Utils.context = context;
                                    MWebSocket.getInstance().init();
                                    String url = MWebSocket.getInstance().getWebsUrl();
                                    System.out.println("-------NewWebSocketUrl:--" + url);
                                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
                                    Login.hook(cl, context);
                                    //消息列表
                                    Messagemanipulation.hook(cl, context, true, true, true);
                                    //红包+心跳
                                    Redpacket.hook(cl, context);
                                    //去红点
                                    Reddot.hook(cl, context);
                                    //个人收款
                                    Personalreceipt.hook(cl, context);
                                    PaymentAssistant.hook(cl, context);
                                }

                            }


                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                }
            });
        }

    }

    public void log(Object str) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        XposedBridge.log("[" + df.format(new Date()) + "]:  "
                + str.toString());
    }


}
