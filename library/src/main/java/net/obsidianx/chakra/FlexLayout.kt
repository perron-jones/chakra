package net.obsidianx.chakra

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MultiMeasureLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaNodeFactory
import net.obsidianx.chakra.measure.YogaMeasurePolicy

@Composable
fun FlexLayout(
    modifier: Modifier = Modifier,
    style: FlexStyle = FlexStyle(),
    content: @Composable FlexLayoutScope.() -> Unit
) {
    val context = LocalContext.current

    val containerNode = remember {
        SoLoader.init(context, false)
        YogaNodeFactory.create()
    }
    style.apply(containerNode)

    MultiMeasureLayout(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min)
            .inspectable(inspectorInfo = debugInspectorInfo {
                style.run {
                    name = "FlexContainer"
                    properties["alignContent"] = alignContent.toString()
                    properties["alignItems"] = alignItems.toString()
                    properties["direction"] = flexDirection.toString()
                    properties["justifyContent"] = justifyContent.toString()
                    properties["wrap"] = wrap.toString()
                }
            }) { this },
        content = { content(FlexLayoutScope()) },
        measurePolicy = YogaMeasurePolicy(containerNode)
    )
}
