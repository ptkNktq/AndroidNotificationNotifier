package me.nya_n.notificationnotifier.ui.screen.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import me.nya_n.notificationnotifier.ui.common.BubbleView
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    AboutContent(
        screenWidth,
        screenHeight,
        onLinkSelected = {
            uriHandler.openUri(it)
        }
    )
}

@Composable
private fun AboutContent(
    screenWidth: Float,
    screenHeight: Float,
    onLinkSelected: (String) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                BubbleView(context, screenWidth, screenHeight)
            }
        )
        AboutView(
            onLinkSelected = onLinkSelected
        )
    }
}

@Composable
private fun AboutView(
    onLinkSelected: (String) -> Unit
) {
    val about = """
            {
                "Nickname": "kani",
                "HP": "https://nya-n.me",
                "GitHub": "https://github.com/ptkNktq",
                "WishList": "https://wish.nya-n.me"
            }
    """.trimIndent()
    val annotated = buildAnnotatedString {
        append(about)
        Regex("https://.*\"").findAll(about).forEach {
            addStyle(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline
                ),
                start = it.range.first,
                end = it.range.last
            )
            addStringAnnotation(
                tag = "URL",
                annotation = it.value.substring(0, it.value.length - 1), // 末尾の"を切り捨てる
                start = it.range.first,
                end = it.range.last
            )
        }
    }
    ClickableText(
        text = annotated,
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 48.sp
        ),
        onClick = {
            val link = annotated.getStringAnnotations(
                tag = "URL",
                start = it,
                end = it
            ).firstOrNull() ?: return@ClickableText
            onLinkSelected(link.item)
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    AppTheme {
        AboutView(
            onLinkSelected = { }
        )
    }
}