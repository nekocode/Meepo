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

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class MeepoUtils {

    public static boolean isTextNotEmpty(@Nullable CharSequence str) {
        return str != null && str.length() > 0;
    }

    @Nullable
    public static Context getContextFromFirstParameter(Object[] parameters) {
        final boolean hasContext = (parameters.length > 0) &&
                (parameters[0] instanceof Context || parameters[0] == null);
        if (hasContext) {
            return (Context) parameters[0];
        } else {
            throw new RuntimeException("First parameter must be context.");
        }
    }

    public static void putValueToBundle(
            @NonNull Bundle bundle, @NonNull String key, @NonNull Object value) {
        if (value instanceof String) {
            bundle.putString(key, (String) value);
        } else if (value instanceof Integer) {
            bundle.putInt(key, (int) value);
        } else if (value instanceof Boolean) {
            bundle.putBoolean(key, (boolean) value);
        } else if (value instanceof Long) {
            bundle.putLong(key, (long) value);
        } else if (value instanceof Short) {
            bundle.putShort(key, (short) value);
        } else if (value instanceof Double) {
            bundle.putDouble(key, (double) value);
        } else if (value instanceof Float) {
            bundle.putFloat(key, (float) value);
        } else if (value instanceof Character) {
            bundle.putChar(key, (char) value);
        } else if (value instanceof Byte) {
            bundle.putByte(key, (byte) value);
        } else if (value instanceof CharSequence) {
            bundle.putCharSequence(key, (CharSequence) value);
        } else if (value instanceof Bundle) {
            bundle.putBundle(key, (Bundle) value);
        } else if (value instanceof Parcelable) {
            bundle.putParcelable(key, (Parcelable) value);
        } else if (value instanceof String[]) {
            bundle.putStringArray(key, (String[]) value);
        } else if (value instanceof int[]) {
            bundle.putIntArray(key, (int[]) value);
        } else if (value instanceof boolean[]) {
            bundle.putBooleanArray(key, (boolean[]) value);
        } else if (value instanceof long[]) {
            bundle.putLongArray(key, (long[]) value);
        } else if (value instanceof short[]) {
            bundle.putShortArray(key, (short[]) value);
        } else if (value instanceof double[]) {
            bundle.putDoubleArray(key, (double[]) value);
        } else if (value instanceof float[]) {
            bundle.putFloatArray(key, (float[]) value);
        } else if (value instanceof char[]) {
            bundle.putCharArray(key, (char[]) value);
        } else if (value instanceof byte[]) {
            bundle.putByteArray(key, (byte[]) value);
        } else if (value instanceof CharSequence[]) {
            bundle.putCharSequenceArray(key, (CharSequence[]) value);
        } else if (value instanceof Parcelable[]) {
            bundle.putParcelableArray(key, (Parcelable[]) value);
        } else if (value instanceof ArrayList) {
            bundle.putIntegerArrayList(key, (ArrayList<Integer>) value);
        } else if (value instanceof SparseArray) {
            bundle.putSparseParcelableArray(key, (SparseArray<? extends Parcelable>) value);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                if (value instanceof IBinder) {
                    bundle.putBinder(key, (IBinder) value);
                    return;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (value instanceof Size) {
                    bundle.putSize(key, (Size) value);
                    return;
                } else if (value instanceof SizeF) {
                    bundle.putSizeF(key, (SizeF) value);
                    return;
                }
            }
            if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
                return;
            }

            throw new RuntimeException(String.format(Locale.getDefault(),
                    "Arguments extra %s has wrong type %s.", key, value.getClass().getName()));
        }
    }
}
