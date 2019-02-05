package cn.nekocode.meepo

import android.app.Activity
import android.content.Context
import android.content.Intent
import cn.nekocode.meepo.annotation.*
import cn.nekocode.meepo.config.UriConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class MeepoTest {
    companion object {
        const val SCHEMA = "test"
        const val HOST = "test.com"
    }

    class AActivity: Activity()
    class BActivity: Activity()

    interface TestRouter {
        @Clazz(BActivity::class)
        @Flags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        fun clazz(context: Context): Boolean

        @ClazzName("cn.nekocode.meepo.MeepoTest\$BActivity")
        @RequestCode(110)
        fun clazzName(context: Context): Boolean
    }

    @Test
    fun test() {
        val a = Robolectric.setupActivity(AActivity::class.java)
        val shadowA = Shadows.shadowOf(a)

        val meepo = Meepo.Builder()
                .config(UriConfig().scheme(SCHEMA).host(HOST))
                .build()

        val router = meepo.create(TestRouter::class.java)

        fun nextActivity() = shadowA.nextStartedActivityForResult
        fun ShadowActivity.IntentForResult.className() = intent.component!!.className
        val bClassName = BActivity::class.java.name
        var next: ShadowActivity.IntentForResult

        Assert.assertTrue(router.clazz(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.className())

        Assert.assertTrue(router.clazzName(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.className())
        Assert.assertEquals(110, next.requestCode)
    }
}