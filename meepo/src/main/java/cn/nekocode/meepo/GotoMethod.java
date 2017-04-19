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
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class GotoMethod {
    private String mimeType;
    private Class targetClass;
    private int targetFlags;
    private String targetAction;

    private ArrayList<Object> pathSegements = new ArrayList<>(); // [segementString, position, ...]
    private HashMap<String, Integer> queryPositions = new HashMap<>(); // {key: position, ...}
    private ArrayList<Integer> queryMapPositions = new ArrayList<>(); // [position, position, ...]
    private HashMap<String, Integer> bundlePositions = new HashMap<>();
    private Integer requestCodePosition;


    public String getUri(String scheme, String host, Object[] args) {
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

    public String getMimeType() {
        return mimeType;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public int getTargetFlags() {
        return targetFlags;
    }

    public String getTargetAction() {
        return targetAction;
    }


    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setTargetFlags(int targetFlags) {
        this.targetFlags = targetFlags;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public void addPathSegement(Object pathSegements) {
        this.pathSegements.add(pathSegements);
    }

    public void addQueryPositions(String key, Integer queryPosition) {
        this.queryPositions.put(key, queryPosition);
    }

    public void addQueryMapPositions(Integer queryMapPosition) {
        this.queryMapPositions.add(queryMapPosition);
    }

    public void addBundlePositions(String key, Integer bundlePosition) {
        this.bundlePositions.put(key, bundlePosition);
    }

    private String getPath(Object[] args) {
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

    private String getQueryString(Object[] args) {
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

    public Bundle getBundle(Object[] args) {
        final Bundle bundle = new Bundle();

        for (Map.Entry<String, Integer> entry : bundlePositions.entrySet()) {
            final String key = entry.getKey();
            final Object value = args[entry.getValue()];

            MeepoUtils.putValueToBundle(bundle, key, value);
        }

        return bundle;
    }

    public void setRequestCodePosition(Integer requestCodePosition) {
        this.requestCodePosition = requestCodePosition;
    }

    public Integer getRequestCode(Object[] args) {
        return requestCodePosition == null ? null : (Integer) args[requestCodePosition];
    }
}
