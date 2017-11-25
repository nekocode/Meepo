package cn.nekocode.meepo.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.nekocode.meepo.Meepo;
import cn.nekocode.meepo.config.UriConfig;
import cn.nekocode.meepo.sample.custom.ModuleCallAdapter;
import cn.nekocode.meepo.sample.custom.ModuleConfig;
import cn.nekocode.meepo.sample.router.ActivityRouter;
import cn.nekocode.meepo.sample.router.ModuleRouter;

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
public class MainActivity extends AppCompatActivity {
    final Meepo meepo = new Meepo.Builder()
            .config(new UriConfig().scheme(BuildConfig.SCHEME).host(BuildConfig.APPLICATION_ID))
            .build();
    final ActivityRouter router = meepo.create(ActivityRouter.class);

    final ModuleRouter moduleRouter = new Meepo.Builder()
            .config(new ModuleConfig("TEST"))
            .adapter(new ModuleCallAdapter())
            .build()
            .create(ModuleRouter.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router.gotoA(MainActivity.this, "AActivity Title");
            }
        });

        findViewById(R.id.button_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router.gotoB(MainActivity.this, "BActivity Title");
            }
        });

        findViewById(R.id.button_module).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleRouter.gotoTestModule(MainActivity.this);
            }
        });

        findViewById(R.id.button_module2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleRouter.gotoTestModule2(MainActivity.this);
            }
        });
    }
}
