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

import cn.nekocode.meepo.GotoMethod;
import cn.nekocode.meepo.annotation.RequestCode;
import cn.nekocode.meepo.annotation.TargetClassName;
import cn.nekocode.meepo.config.Config;
import cn.nekocode.meepo.MeepoUtils;
import cn.nekocode.meepo.annotation.Bundle;
import cn.nekocode.meepo.annotation.Path;
import cn.nekocode.meepo.annotation.Query;
import cn.nekocode.meepo.annotation.QueryMap;
import cn.nekocode.meepo.annotation.TargetAction;
import cn.nekocode.meepo.annotation.TargetClass;
import cn.nekocode.meepo.annotation.TargetFlags;
import cn.nekocode.meepo.annotation.TargetPath;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class DefaultParser implements Parser {

    @NonNull
    @Override
    public GotoMethod parseMethod(@NonNull Config config, @NonNull Method method) {
        final Annotation[] methodAnnotations = method.getAnnotations();
        final Annotation[][] parameterAnnotationsArray = method.getParameterAnnotations();

        final GotoMethod goTo = new GotoMethod();
        parseMethodAnnotations(goTo, methodAnnotations, parameterAnnotationsArray);
        parseParameterAnnotation(goTo, parameterAnnotationsArray);

        return goTo;
    }

    protected void parseMethodAnnotations(
            @NonNull GotoMethod goTo, @NonNull Annotation[] methodAnnotations, @NonNull Annotation[][] parameterAnnotationsArray) {

        final HashMap<String, Integer> positions = new HashMap<>();
        for (int i = 0; i < parameterAnnotationsArray.length; i++) {
            final Annotation[] annotations = parameterAnnotationsArray[i];

            for (Annotation annotation : annotations) {
                if (annotation instanceof Path) {
                    positions.put(((Path) annotation).value(), i);
                }
            }
        }

        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof TargetClass) {
                goTo.setTargetClass(((TargetClass) annotation).value());

            } else if (annotation instanceof TargetClassName) {
                goTo.setTargetClassName(((TargetClassName) annotation).value());

            } else if (annotation instanceof TargetPath) {
                final TargetPath path = (TargetPath) annotation;
                final String segements[] = path.value().split("[{}]");

                for (int i = 0; i < segements.length; i++) {
                    final String segment = segements[i];

                    if (i % 2 == 0) {
                        if (!MeepoUtils.isTextEmpty(segment)) {
                            goTo.addPathSegement(segment);
                        }
                    } else {
                        final Integer position = positions.get(segment);
                        if (position != null) {
                            goTo.addPathSegement(position);
                        } else {
                            throw new RuntimeException(String.format(Locale.getDefault(),
                                    "@Path(\"%s\") not found.", segment));
                        }
                    }
                }

                if (!MeepoUtils.isTextEmpty(path.mimeType())) {
                    goTo.setMimeType(path.mimeType());
                }

            } else if (annotation instanceof TargetFlags) {
                goTo.setTargetFlags(((TargetFlags) annotation).value());

            } else if (annotation instanceof TargetAction) {
                goTo.setTargetAction(((TargetAction) annotation).value());

            }
        }
    }

    protected void parseParameterAnnotation(@NonNull GotoMethod goTo, @NonNull Annotation[][] parameterAnnotationsArray) {
        for (int i = 0; i < parameterAnnotationsArray.length; i++) {
            final Annotation[] annotations = parameterAnnotationsArray[i];

            for (Annotation annotation : annotations) {
                if (annotation instanceof Bundle) {
                    goTo.addBundlePositions(((Bundle) annotation).value(), i);

                } else if (annotation instanceof Query) {
                    goTo.addQueryPositions(((Query) annotation).value(), i);

                } else if (annotation instanceof QueryMap) {
                    goTo.addQueryMapPositions(i);

                } else if (annotation instanceof RequestCode) {
                    goTo.setRequestCodePosition(i);
                }
            }
        }
    }
}
