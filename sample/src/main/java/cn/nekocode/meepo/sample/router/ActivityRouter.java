package cn.nekocode.meepo.sample.router;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.nekocode.meepo.Meepo;
import cn.nekocode.meepo.annotation.Bundle;
import cn.nekocode.meepo.annotation.Query;
import cn.nekocode.meepo.annotation.TargetClass;
import cn.nekocode.meepo.annotation.TargetFlags;
import cn.nekocode.meepo.annotation.TargetPath;
import cn.nekocode.meepo.config.UriConfig;
import cn.nekocode.meepo.sample.BActivity;
import cn.nekocode.meepo.sample.BuildConfig;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public interface ActivityRouter {
    ActivityRouter IMPL = new Meepo.Builder()
            .config(new UriConfig().scheme(BuildConfig.SCHEME).host(BuildConfig.APPLICATION_ID))
            .build().create(ActivityRouter.class);

    String ARG_TITLE = "title";
    String ARG_NULLABLE = "nullable";


    @TargetPath("a")
    boolean gotoA(
            @NonNull Context context,
            @Query(ARG_TITLE) @NonNull String title,
            @Query(ARG_NULLABLE) @Nullable String nullable
    );

    @TargetClass(BActivity.class)
    @TargetFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(
            @NonNull Context context,
            @Bundle(ARG_TITLE) @NonNull String title
    );
}
