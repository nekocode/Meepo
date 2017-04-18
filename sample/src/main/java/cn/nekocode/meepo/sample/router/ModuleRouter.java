package cn.nekocode.meepo.sample.router;

import android.content.Context;

import cn.nekocode.meepo.annonation.TargetClass;
import cn.nekocode.meepo.sample.custom.TestModule;
import cn.nekocode.meepo.sample.custom.TestModule2;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public interface ModuleRouter {

    @TargetClass(TestModule.class)
    void gotoTestModule(Context context);

    @TargetClass(TestModule2.class)
    void gotoTestModule2(Context context);
}
