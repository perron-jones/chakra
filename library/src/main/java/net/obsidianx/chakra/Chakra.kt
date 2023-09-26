package net.obsidianx.chakra

import android.content.Context
import com.facebook.soloader.SoLoader

object Chakra {
    /**
     * Manually load the Yoga native library at a more convenient time in your app
     * rather than waiting for the first render of a Flexbox layout which may result
     * in dropped frames.
     */
    fun init(context: Context) {
        SoLoader.init(context, false)
    }

    /**
     * When enabled, verbose debugging output is logged to Logcat to inspect how Chakra
     * is managing measuring and placing nodes within Flexbox layouts.  This is likely
     * only useful when hacking on Chakra itself.
     */
    var debugLogging: Boolean = false
}