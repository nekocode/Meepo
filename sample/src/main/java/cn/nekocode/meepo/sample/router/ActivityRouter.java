package cn.nekocode.meepo.sample.router;

import android.content.Context;
import android.content.Intent;

import cn.nekocode.meepo.annotation.Bundle;
import cn.nekocode.meepo.annotation.Query;
import cn.nekocode.meepo.annotation.TargetClass;
import cn.nekocode.meepo.annotation.TargetFlags;
import cn.nekocode.meepo.annotation.TargetPath;
import cn.nekocode.meepo.sample.BActivity;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public interface ActivityRouter {

    @TargetPath("a")
    boolean gotoA(Context context, @Query("title") String title);

    @TargetClass(BActivity.class)
    @TargetFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(Context context, @Bundle("title") String title);
}
