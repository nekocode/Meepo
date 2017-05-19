package cn.nekocode.meepo.sample.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.nekocode.meepo.GotoMethod;
import cn.nekocode.meepo.MeepoUtils;
import cn.nekocode.meepo.adapter.GotoAdapter;
import cn.nekocode.meepo.config.Config;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class GotoModuleAdapter implements GotoAdapter {

    @Nullable
    @Override
    public Object goTo(@NonNull Config config, @NonNull GotoMethod method, @NonNull Object[] args) {
        final Context context = MeepoUtils.getContextFromFirstParameter(args);
        final Class targetClass = method.getTargetClass();

        String textPrefix = "";
        if (config instanceof ModuleConfig) {
            textPrefix = ((ModuleConfig) config).getTextPrefix();
        }

        if (targetClass != null &&
                (targetClass == TestModule.class || targetClass.getSuperclass() == TestModule.class)) {
            try {
                TestModule module = (TestModule) targetClass.getConstructor(String.class).newInstance(textPrefix);
                module.showToast(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
