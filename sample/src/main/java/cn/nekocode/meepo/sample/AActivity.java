package cn.nekocode.meepo.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class AActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        setTitle(getIntent().getData().getQueryParameter("title"));
    }
}
