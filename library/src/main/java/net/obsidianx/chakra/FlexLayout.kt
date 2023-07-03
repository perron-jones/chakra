package net.obsidianx.chakra

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaNodeFactory
import net.obsidianx.chakra.debug.inspectableFlexContainer
import net.obsidianx.chakra.layout.YogaMeasurePolicy
import net.obsidianx.chakra.layout.flexInternal

@Composable
fun FlexLayout(
    modifier: Modifier = Modifier,
    style: FlexStyle = FlexStyle(),
    content: @Composable FlexLayoutScope.() -> Unit
) {
    val context = LocalContext.current
    val parentNode = LocalFlexContainer.current

    val containerNode = remember {
        SoLoader.init(context, false)
        YogaNodeFactory.create()
    }
    style.apply(containerNode)

    Layout(
        content = {
            CompositionLocalProvider(LocalFlexContainer provides containerNode) {
                content(FlexLayoutScope())
            }
        },
        modifier = modifier
            .inspectableFlexContainer(style) {
                if (parentNode != null) {
                    flexInternal(style, containerNode)
                } else {
                    this
                }
            }
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min),
        measurePolicy = YogaMeasurePolicy(containerNode),
    )
}
