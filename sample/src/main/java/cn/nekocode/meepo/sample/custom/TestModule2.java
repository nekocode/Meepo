package cn.nekocode.meepo.sample.custom;

import android.content.Context;
import android.widget.Toast;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class TestModule2 extends TestModule {

    public TestModule2(String textPrefix) {
        super(textPrefix);
    }

    public void showToast(Context context) {
        Toast.makeText(context, textPrefix + ": TestModule2!", Toast.LENGTH_SHORT).show();
    }
}
