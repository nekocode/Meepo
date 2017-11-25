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

package cn.nekocode.meepo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class CallMethod {
    private String mimeType;
    private Class targetClass;
    private String targetClassName;
    private int targetFlags;
    private String targetAction;

    private ArrayList<Object> pathSegements = new ArrayList<>(); // [segementString, position, ...]
    private HashMap<String, Integer> queryPositions = new HashMap<>(); // {key: position, ...}
    private ArrayList<Integer> queryMapPositions = new ArrayList<>(); // [position, position, ...]
    private HashMap<String, Integer> bundlePositions = new HashMap<>();
    private Integer requestCodePosition;


    @Nullable
    public String getTargetAction() {
        return targetAction;
    }

    @Nullable
    public Class getTargetClass() {
        return targetClass;
    }

    @Nullable
    public String getTargetClassName() {
        return targetClassName;
    }

    public int getTargetFlags() {
        return targetFlags;
    }

    @Nullable
    public String getMimeType() {
        return mimeType;
    }

    @Nullable
    public String getUri(@Nullable String scheme, @Nullable String host, @NonNull Object[] args) {
        if (TextUtils.isEmpty(scheme)) {
            return null;
        }
        final String hostUri = scheme + "://" + (TextUtils.isEmpty(host) ? "" : host + "/");
        final String path = getPath(args);
        final String queryString = getQueryString(args);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return hostUri + path + queryString;
    }



    public void setTargetAction(@Nullable String targetAction) {
        this.targetAction = targetAction;
    }

    public void setTargetClass(@Nullable Class targetClass) {
        this.targetClass = targetClass;
    }

    public void setTargetClassName(@Nullable String targetClassName) {
        this.targetClassName = targetClassName;
    }

    public void setTargetFlags(int targetFlags) {
        this.targetFlags = targetFlags;
    }

    public void setMimeType(@Nullable String mimeType) {
        this.mimeType = mimeType;
    }

    public void addPathSegement(@NonNull Object pathSegements) {
        this.pathSegements.add(pathSegements);
    }

    public void addQueryPositions(@NonNull String key, @NonNull Integer queryPosition) {
        this.queryPositions.put(key, queryPosition);
    }

    public void addQueryMapPositions(@NonNull Integer queryMapPosition) {
        this.queryMapPositions.add(queryMapPosition);
    }

    public void addBundlePositions(@NonNull String key, @NonNull Integer bundlePosition) {
        this.bundlePositions.put(key, bundlePosition);
    }

    @NonNull
    private String getPath(@NonNull Object[] args) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (Object segment : pathSegements) {
            if (segment instanceof String) {
                stringBuilder.append((String) segment);
            } else if (segment instanceof Integer) {
                stringBuilder.append((String) args[(int) segment]);
            }
        }

        return stringBuilder.toString();
    }

    @NonNull
    private String getQueryString(@NonNull Object[] args) {
        final StringBuilder stringBuilder = new StringBuilder("?");

        int count = 0;
        for (Map.Entry<String, Integer> entry : queryPositions.entrySet()) {
            final String key = entry.getKey();
            final String value = String.valueOf(args[entry.getValue()]);

            if (count++ == 0) {
                stringBuilder.append(key).append("=").append(value);
            } else {
                stringBuilder.append("&").append(key).append("=").append(value);
            }
        }

        for (Integer position : queryMapPositions) {
            final Map<String, ?> queryMap = (Map<String, ?>) args[position];

            for (Map.Entry<String, ?> entry : queryMap.entrySet()) {
                final String key = entry.getKey();
                final String value = String.valueOf(entry.getValue());

                if (count++ == 0) {
                    stringBuilder.append(key).append("=").append(value);
                } else {
                    stringBuilder.append("&").append(key).append("=").append(value);
                }
            }
        }

        return count == 0 ? "" : stringBuilder.toString();
    }

    @NonNull
    public Bundle getBundle(@NonNull Object[] args) {
        final Bundle bundle = new Bundle();

        for (Map.Entry<String, Integer> entry : bundlePositions.entrySet()) {
            final String key = entry.getKey();
            final Object value = args[entry.getValue()];

            MeepoUtils.putValueToBundle(bundle, key, value);
        }

        return bundle;
    }

    public void setRequestCodePosition(@Nullable Integer requestCodePosition) {
        this.requestCodePosition = requestCodePosition;
    }

    @Nullable
    public Integer getRequestCode(@NonNull Object[] args) {
        return requestCodePosition == null ? null : (Integer) args[requestCodePosition];
    }
}
