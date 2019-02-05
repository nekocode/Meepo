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

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], manifest = "src/test/AndroidManifest.xml")
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

        @Action("testAction")
        fun action(context: Context,
                   @RequestCodeParam requestCode: Int,
                   @BundleParam("i") i: Int,
                   @BundleParam("b") b: Boolean,
                   @BundleParam("s") s: String?
        ): Boolean

        @Path(value = "testUri/{id}/end", mimeType = "text/plain")
        fun uri(context: Context,
                @PathParam("id") id: String,
                @QueryParam("on") on: Boolean,
                @QueryParam("s") s: String?,
                @QueryMapParam queries: Map<String, Any>
        ): Boolean

        @Path("x")
        fun special(context: Context?): Boolean

        @Path("x/{x}")
        fun special2(context: Context): Boolean
    }

    @Test
    fun test() {
        val a = Robolectric.setupActivity(AActivity::class.java)
        val shadowA = Shadows.shadowOf(a)

        fun nextActivity() = shadowA.nextStartedActivityForResult
        val bClassName = BActivity::class.java.name
        var next: ShadowActivity.IntentForResult

        val meepo = Meepo.Builder()
                .config(UriConfig().scheme(SCHEME).host(HOST))
                .build()
        val router = meepo.create(TestRouter::class.java)

        Assert.assertTrue(router.clazz(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.intent.component!!.className)
        Assert.assertEquals(Intent.FLAG_ACTIVITY_SINGLE_TOP, next.intent.flags)

        Assert.assertTrue(router.clazzName(a))
        next = nextActivity()
        Assert.assertEquals(bClassName, next.intent.component!!.className)
        Assert.assertEquals(110, next.requestCode)

        Assert.assertTrue(router.action(a, 101, 1, true, null))
        next = nextActivity()
        Assert.assertEquals("testAction", next.intent.action)
        Assert.assertEquals(101, next.requestCode)
        Assert.assertEquals(1, next.intent.getIntExtra("i", 0))
        Assert.assertEquals(true, next.intent.getBooleanExtra("b", false))
        Assert.assertEquals(null, next.intent.getStringExtra("s"))

        val queries = mapOf("test" to 111)
        Assert.assertTrue(router.uri(a, "id0", true, null, queries))
        next = nextActivity()
        Assert.assertEquals("test://test.com/testUri/id0/end?on=true&test=111",
                next.intent.data!!.toString())
        Assert.assertEquals("text/plain", next.intent.type)

        // Special cases
        Assert.assertFalse(router.special(null))
        Assert.assertFalse(router.special(a))

        val e = try {
            router.special2(a)
            null
        } catch (exception: RuntimeException) {
            Assert.assertTrue(
                    exception.message!!.startsWith("@Path(") &&
                            exception.message!!.endsWith(") not found.")
            )
            exception
        }
        Assert.assertNotNull(e)
    }
}