package cn.nekocode.meepo.sample.custom;

import android.support.annotation.NonNull;

import cn.nekocode.meepo.config.Config;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class ModuleConfig implements Config {
    private String textPrefix;

    public ModuleConfig(@NonNull String textPrefix) {
        this.textPrefix = textPrefix;
    }

    public String getTextPrefix() {
        return textPrefix;
    }
}
