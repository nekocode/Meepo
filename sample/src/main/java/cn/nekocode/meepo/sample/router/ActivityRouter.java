package cn.nekocode.meepo.sample.router;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.nekocode.meepo.Meepo;
import cn.nekocode.meepo.annotation.BundleParam;
import cn.nekocode.meepo.annotation.QueryParam;
import cn.nekocode.meepo.annotation.Clazz;
import cn.nekocode.meepo.annotation.Flags;
import cn.nekocode.meepo.annotation.Path;
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


    @Path("a")
    boolean gotoA(
            @NonNull Context context,
            @QueryParam(ARG_TITLE) @NonNull String title,
            @QueryParam(ARG_NULLABLE) @Nullable String nullable
    );

    @Clazz(BActivity.class)
    @Flags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    void gotoB(
            @NonNull Context context,
            @BundleParam(ARG_TITLE) @NonNull String title
    );
}
