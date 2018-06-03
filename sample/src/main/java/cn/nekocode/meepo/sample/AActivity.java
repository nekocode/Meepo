package cn.nekocode.meepo.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.nekocode.meepo.sample.router.ActivityRouter;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        setTitle(getIntent().getData().getQueryParameter(ActivityRouter.ARG_TITLE));
    }
}
