package cn.nekocode.meepo.sample.router;

import android.content.Context;

import cn.nekocode.meepo.annotation.Clazz;
import cn.nekocode.meepo.sample.custom.TestModule;
import cn.nekocode.meepo.sample.custom.TestModule2;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public interface ModuleRouter {

    @Clazz(TestModule.class)
    void gotoTestModule(Context context);

    @Clazz(TestModule2.class)
    void gotoTestModule2(Context context);
}
