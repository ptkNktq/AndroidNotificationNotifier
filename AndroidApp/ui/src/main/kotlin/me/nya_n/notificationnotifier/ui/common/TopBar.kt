package me.nya_n.notificationnotifier.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.nya_n.notificationnotifier.ui.R
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    hasBackContent: Boolean = false,
    onBack: () -> Unit = { }
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.content_title),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            if (hasBackContent) {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    AppTheme {
        TopBar()
    }
}

@Preview
@Composable
private fun SubContentTopBarPreview() {
    AppTheme {
        TopBar(
            hasBackContent = true,
            onBack = { }
        )
    }
}