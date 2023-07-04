package net.obsidianx.chakra.types

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaUnit
import com.facebook.yoga.YogaValue

sealed interface FlexValue {
    object Auto : FlexValue {
        override fun toString(): String = "Auto"
    }

    object Undefined : Value(Float.NaN) {
        override fun toString(): String = "Undefined"
    }

    object Zero : Value(0f)
    open class Value(val value: Float) : FlexValue {
        override fun toString(): String = value.toString()
    }

    data class Percent(val value: Float) : FlexValue {
        override fun toString(): String = "$value%"
    }

    fun apply(
        setValue: ((value: Float) -> Unit)? = null,
        setPercent: ((value: Float) -> Unit)? = null,
        setAuto: (() -> Unit)? = null
    ) {
        when (this) {
            is Value -> setValue?.invoke(value)
            is Percent -> setPercent?.invoke(value)
            else -> setAuto?.invoke() ?: setValue?.invoke(Float.NaN)
        }
    }

    companion object {
        @SuppressLint("ComposableNaming")
        @Composable
        fun Dp(value: androidx.compose.ui.unit.Dp) =
            Value(value.value * LocalDensity.current.density)
    }
}

val YogaValue.isSet
    get() = this.unit == YogaUnit.PERCENT || this.unit == YogaUnit.POINT