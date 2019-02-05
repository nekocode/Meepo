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

import android.os.*
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.Serializable
import android.os.Parcel
import org.robolectric.RuntimeEnvironment
import java.util.*

/**
 * @author nekocode (nekocode.cn@gmail.com)
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21])
class MeepoUtilsTest {

    @Test
    fun isTextNotEmpty() {
        Assert.assertFalse(MeepoUtils.isTextNotEmpty(null))
        Assert.assertFalse(MeepoUtils.isTextNotEmpty(""))
        Assert.assertTrue(MeepoUtils.isTextNotEmpty("1"))
    }

    @Test
    fun getContextFromFirstParameter() {
        Assert.assertNull(MeepoUtils.getContextFromFirstParameter(arrayOf<Any?>(null)))
        Assert.assertNotNull(MeepoUtils.getContextFromFirstParameter(
                arrayOf(RuntimeEnvironment.systemContext)))

        var e = try {
            MeepoUtils.getContextFromFirstParameter(emptyArray())
            null
        } catch (exception: RuntimeException) {
            Assert.assertEquals("First parameter must be context.", exception.message)
            exception
        }
        Assert.assertNotNull(e)

        e = try {
            MeepoUtils.getContextFromFirstParameter(arrayOf(Any()))
            null
        } catch (exception: RuntimeException) {
            Assert.assertEquals("First parameter must be context.", exception.message)
            exception
        }
        Assert.assertNotNull(e)
    }

    @Test
    fun putValueToBundle() {
        val bundle = Bundle()

        class TestBinder : Binder(), Serializable

        val values = arrayOf(
                "0",
                0,
                true,
                0L,
                0.toShort(),
                0.0,
                0f,
                '0',
                '0'.toByte(),
                SpannableString("0"), // CharSequence
                Bundle(),
                AbsoluteSizeSpan(0), // Parcelable

                arrayOf("0"),
                intArrayOf(0),
                booleanArrayOf(true),
                longArrayOf(0L),
                shortArrayOf(0),
                doubleArrayOf(0.0),
                floatArrayOf(0f),
                charArrayOf('0'),
                byteArrayOf('0'.toByte()),
                arrayOf(SpannableString("0")),
                arrayOf(AbsoluteSizeSpan(0)),

                arrayListOf(0),
                SparseArray<Parcelable>().also { it.append(0, AbsoluteSizeSpan(0)) },
                TestBinder(),
                Size(0, 0),
                SizeF(0f, 0f),
                Date() // Serializable
        )

        values.forEachIndexed { i, value ->
            MeepoUtils.putValueToBundle(bundle, i.toString(), value)
        }

        val e = try {
            MeepoUtils.putValueToBundle(bundle, "any", Any())
            null
        } catch (exception: RuntimeException) {
            Assert.assertTrue(exception.message?.contains("has wrong type") ?: false)
            exception
        }
        Assert.assertNotNull(e)

        try {
            val parcel = Parcel.obtain()
            bundle.writeToParcel(parcel, 0)
            parcel.marshall()
            parcel.recycle()
        } catch (throwable: Throwable) {
            Assert.fail()
        }
    }
}