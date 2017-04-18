package cn.nekocode.meepo.sample.custom;

import android.content.Context;
import android.widget.Toast;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class TestModule {
    protected String textPrefix;


    public TestModule(String textPrefix) {
        this.textPrefix = textPrefix;
    }

    public void showToast(Context context) {
        Toast.makeText(context, textPrefix + ": TestModule!", Toast.LENGTH_SHORT).show();
    }
}
