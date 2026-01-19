package me.nya_n.notificationnotifier.ui.screen.license

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import me.nya_n.notificationnotifier.ui.common.AppScaffold
import me.nya_n.notificationnotifier.ui.theme.AppTheme

@Composable
fun LicenseScreen(navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val libraries by produceLibraries()
    AppScaffold(
        snackbarHostState = snackbarHostState,
        onBack = {
            navController.popBackStack()
        }
    ) {
        LibrariesContainer(
            libraries,
            Modifier.padding(it),
            colors = LibraryDefaults.libraryColors(
                libraryBackgroundColor = Color.Transparent,
                dialogBackgroundColor = AlertDialogDefaults.containerColor,
                dialogConfirmButtonColor = AlertDialogDefaults.textContentColor
            ),
            divider = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
            }
        )
    }
}

@Preview
@Composable
private fun LicensePreview() {
    val navController = rememberNavController()
    AppTheme {
        LicenseScreen(
            navController = navController
        )
    }
}
