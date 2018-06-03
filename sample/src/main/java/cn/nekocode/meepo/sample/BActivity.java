package cn.nekocode.meepo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.nekocode.meepo.sample.router.ActivityRouter;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        setTitle(getIntent().getStringExtra(ActivityRouter.ARG_TITLE));
    }
}
