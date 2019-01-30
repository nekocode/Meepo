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

package cn.nekocode.meepo.parser;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

import cn.nekocode.meepo.CallMethod;
import cn.nekocode.meepo.annotation.RequestCode;
import cn.nekocode.meepo.annotation.RequestCodeParam;
import cn.nekocode.meepo.annotation.ClazzName;
import cn.nekocode.meepo.config.Config;
import cn.nekocode.meepo.MeepoUtils;
import cn.nekocode.meepo.annotation.BundleParam;
import cn.nekocode.meepo.annotation.PathParam;
import cn.nekocode.meepo.annotation.QueryParam;
import cn.nekocode.meepo.annotation.QueryMapParam;
import cn.nekocode.meepo.annotation.Action;
import cn.nekocode.meepo.annotation.Clazz;
import cn.nekocode.meepo.annotation.Flags;
import cn.nekocode.meepo.annotation.Path;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class DefaultParser implements Parser {

    @NonNull
    @Override
    public CallMethod parseMethod(@NonNull Config config, @NonNull Method method) {
        final Annotation[] methodAnnotations = method.getAnnotations();
        final Annotation[][] parameterAnnotationsArray = method.getParameterAnnotations();

        final CallMethod callMethod = new CallMethod();
        parseMethodAnnotations(callMethod, methodAnnotations, parameterAnnotationsArray);
        parseParameterAnnotation(callMethod, parameterAnnotationsArray);

        return callMethod;
    }

    protected void parseMethodAnnotations(
            @NonNull CallMethod callMethod, @NonNull Annotation[] methodAnnotations, @NonNull Annotation[][] parameterAnnotationsArray) {

        final HashMap<String, Integer> positions = new HashMap<>();
        for (int i = 0; i < parameterAnnotationsArray.length; i++) {
            final Annotation[] annotations = parameterAnnotationsArray[i];

            for (Annotation annotation : annotations) {
                if (annotation instanceof PathParam) {
                    positions.put(((PathParam) annotation).value(), i);
                }
            }
        }

        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof Clazz) {
                callMethod.setClazz(((Clazz) annotation).value());

            } else if (annotation instanceof ClazzName) {
                callMethod.setClazzName(((ClazzName) annotation).value());

            } else if (annotation instanceof Path) {
                final Path path = (Path) annotation;
                final String segments[] = path.value().split("[{}]");

                for (int i = 0; i < segments.length; i++) {
                    final String segment = segments[i];

                    if (i % 2 == 0) {
                        if (MeepoUtils.isTextNotEmpty(segment)) {
                            callMethod.addPathSegment(segment);
                        }
                    } else {
                        final Integer position = positions.get(segment);
                        if (position != null) {
                            callMethod.addPathSegment(position);
                        } else {
                            throw new RuntimeException(String.format(Locale.getDefault(),
                                    "@Path(\"%s\") not found.", segment));
                        }
                    }
                }

                if (MeepoUtils.isTextNotEmpty(path.mimeType())) {
                    callMethod.setMimeType(path.mimeType());
                }

            } else if (annotation instanceof Flags) {
                callMethod.setFlags(((Flags) annotation).value());

            } else if (annotation instanceof Action) {
                callMethod.setAction(((Action) annotation).value());

            } else if (annotation instanceof RequestCode) {
                callMethod.setRequestCode(((RequestCode) annotation).value());
            }
        }
    }

    protected void parseParameterAnnotation(@NonNull CallMethod callMethod, @NonNull Annotation[][] parameterAnnotationsArray) {
        for (int i = 0; i < parameterAnnotationsArray.length; i++) {
            final Annotation[] annotations = parameterAnnotationsArray[i];

            for (Annotation annotation : annotations) {
                if (annotation instanceof BundleParam) {
                    callMethod.addBundlePositions(((BundleParam) annotation).value(), i);

                } else if (annotation instanceof QueryParam) {
                    callMethod.addQueryPositions(((QueryParam) annotation).value(), i);

                } else if (annotation instanceof QueryMapParam) {
                    callMethod.addQueryMapPositions(i);

                } else if (annotation instanceof RequestCodeParam) {
                    callMethod.setRequestCodePosition(i);
                }
            }
        }
    }
}
