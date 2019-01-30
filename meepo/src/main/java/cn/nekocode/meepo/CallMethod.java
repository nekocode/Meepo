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
    private Class clazz;
    private String clazzName;
    private String action;
    private Integer flags;
    private Integer requestCode;
    private String mimeType;

    private ArrayList<Object> pathSegments = new ArrayList<>(); // [segmentString, position, ...]
    private HashMap<String, Integer> queryPositions = new HashMap<>(); // {key: position, ...}
    private ArrayList<Integer> queryMapPositions = new ArrayList<>(); // [position, position, ...]
    private HashMap<String, Integer> bundlePositions = new HashMap<>();
    private Integer requestCodePosition;


    @Nullable
    public Class getClazz() {
        return clazz;
    }

    public void setClazz(@Nullable Class clazz) {
        this.clazz = clazz;
    }

    @Nullable
    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(@Nullable String clazzName) {
        this.clazzName = clazzName;
    }

    @Nullable
    public String getAction() {
        return action;
    }

    public void setAction(@Nullable String action) {
        this.action = action;
    }

    @Nullable
    public Integer getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setRequestCodePosition(@Nullable Integer requestCodePosition) {
        this.requestCodePosition = requestCodePosition;
    }

    @Nullable
    public Integer getRequestCode(@NonNull Object[] args) {
        return requestCodePosition == null ? requestCode : (Integer) args[requestCodePosition];
    }

    @Nullable
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(@Nullable String mimeType) {
        this.mimeType = mimeType;
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

    public void addPathSegment(@NonNull Object pathSegment) {
        this.pathSegments.add(pathSegment);
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

        for (Object segment : pathSegments) {
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
        String key, value;
        Object arg;
        for (Map.Entry<String, Integer> entry : queryPositions.entrySet()) {
            key = entry.getKey();
            arg = args[entry.getValue()];
            if (arg == null) continue;
            value = arg.toString();

            if (count++ == 0) {
                stringBuilder.append(key).append("=").append(value);
            } else {
                stringBuilder.append("&").append(key).append("=").append(value);
            }
        }

        Map<String, ?> queryMap;
        for (Integer position : queryMapPositions) {
            arg = args[position];
            if (arg == null) continue;
            queryMap = (Map<String, ?>) arg;

            Object tmpValue;
            for (Map.Entry<String, ?> entry : queryMap.entrySet()) {
                key = entry.getKey();
                tmpValue = entry.getValue();
                if (tmpValue == null) continue;
                value = tmpValue.toString();

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

        String key;
        Object value;
        for (Map.Entry<String, Integer> entry : bundlePositions.entrySet()) {
            key = entry.getKey();
            value = args[entry.getValue()];
            if (value == null) continue;

            MeepoUtils.putValueToBundle(bundle, key, value);
        }

        return bundle;
    }
}
