/*
 * Copyright 2019. nekocode (nekocode.cn@gmail.com)
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

package cn.nekocode.meepo

import android.content.Context
import android.content.Intent
import cn.nekocode.meepo.annotation.Clazz
import cn.nekocode.meepo.annotation.ClazzName
import cn.nekocode.meepo.annotation.Flags
import cn.nekocode.meepo.annotation.RequestCode
import cn.nekocode.meepo.config.UriConfig
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowActivity

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class MeepoTest {
    companion object {
        const val SCHEME = "test"
        const val HOST = "test.com"
    }

    interface TestRouter {
        @Clazz(BActivity::class)
        @Flags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        fun clazz(context: Context): Boolean

        @ClazzName("cn.nekocode.meepo.BActivity")
        @RequestCode(110)
        fun clazzName(context: Context): Boolean
    }

    @Test
    fun test() {
        val a = Robolectric.setupActivity(AActivity::class.java)
        val shadowA = Shadows.shadowOf(a)

        fun nextActivity() = shadowA.nextStartedActivityForResult
        fun ShadowActivity.IntentForResult.className() = intent.component!!.className
        val bClassName = BActivity::class.java.name
        var next: ShadowActivity.IntentForResult

        val meepo = Meepo.Builder()
                .config(UriConfig().scheme(SCHEME).host(HOST))
                .build()
        val router = meepo.create(TestRouter::class.java)

        Assert.assertTrue(router.clazz(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.className())

        Assert.assertTrue(router.clazzName(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.className())
        Assert.assertEquals(110, next.requestCode)
    }
}