package hook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import main.Utils;

public class Reddot {
	private static Context context;
	private static Object socialRecentListView;

	public static void hook(ClassLoader cl, Context context) throws ClassNotFoundException {
		Class SocialRecentListView = cl.loadClass("com.alipay.mobile.socialwidget.ui.SocialRecentListView");
		findMethod(SocialRecentListView, context);
	}

	private static void findMethod(Class hookclass, Context c) {

		if (!hookclass.isInterface()) {
			for (Method method : hookclass.getDeclaredMethods()) {

				if (!Modifier.isAbstract(method.getModifiers())) {
					context = c;
					findParamFormMethod(method, hookclass.getName(), hookclass, context);

				}
			}
		}

	}

	private static void findParamFormMethod(final Method method, String name, final Class cc, final Context ccc) {
		// TODO Auto-generated method stub
		XposedBridge.hookMethod(method, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				// TODO Auto-generated method stub
				if (cc.getName().equals("com.alipay.mobile.socialwidget.ui.SocialRecentListView")) {
					if (param.thisObject != null) {
						socialRecentListView = param.thisObject;
						
					}

				}

			}

		});

	}

	public static void red(String username ,String from_u_id, String id,int i) {
		try {
			final Method mm = socialRecentListView.getClass().getDeclaredMethod("a", socialRecentListView.getClass(),
					int.class, String.class, int.class, String.class, String.class, String.class);
			mm.setAccessible(true);
			mm.invoke(socialRecentListView,
					new Object[] { socialRecentListView, 1, id + "_" + from_u_id, i, from_u_id, username, null });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
