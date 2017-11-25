/*
 * Copyright 2017. nekocode (nekocode.cn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.nekocode.meepo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.nekocode.meepo.CallMethod;
import cn.nekocode.meepo.config.Config;
import cn.nekocode.meepo.MeepoUtils;
import cn.nekocode.meepo.config.UriConfig;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class ActivityCallAdapter implements CallAdapter<Boolean> {

    @Nullable
    @Override
    public Boolean call(@NonNull Config config, @NonNull CallMethod method, @NonNull Object[] args) {
        final Context context = MeepoUtils.getContextFromFirstParameter(args);
        if (context == null) {
            return false;
        }

        String uri = null;
        if (config instanceof UriConfig) {
            final UriConfig uriConfig = (UriConfig) config;
            uri = method.getUri(uriConfig.getScheme(), uriConfig.getHost(), args);
        }

        final Class targetClass = method.getTargetClass();
        final Intent intent = new Intent();
        if (targetClass != null) {
            intent.setClass(context, targetClass);
        }
        final String targetClassName = method.getTargetClassName();
        if (targetClassName != null) {
            intent.setClassName(context, targetClassName);
        }
        if (uri != null) {
            intent.setDataAndType(Uri.parse(uri), method.getMimeType());
        }
        intent.putExtras(method.getBundle(args));
        intent.setFlags(method.getTargetFlags());
        intent.setAction(method.getTargetAction());

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final Integer requestCode = method.getRequestCode(args);

            if (requestCode != null && context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, requestCode);

            } else {
                context.startActivity(intent);
            }
            return true;
        }

        return false;
    }

}
