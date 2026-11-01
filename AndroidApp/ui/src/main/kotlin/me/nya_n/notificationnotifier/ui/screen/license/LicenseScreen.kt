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
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Developer
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License
import com.mikepenz.aboutlibraries.entity.Scm
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
    LicenseContent(
        snackbarHostState = snackbarHostState,
        libraries = libraries,
        onBack = {
            navController.popBackStack()
        }
    )
}

@Composable
fun LicenseContent(
    snackbarHostState: SnackbarHostState,
    libraries: Libs?,
    onBack: () -> Unit
) {
    AppScaffold(
        snackbarHostState = snackbarHostState,
        onBack = onBack
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
    val snackbarHostState = remember { SnackbarHostState() }
    val libs = Libs(
        libraries = listOf(
            Library(
                uniqueId = "sample1",
                artifactVersion = "1.2.3",
                name = "sample1",
                description = "test description",
                website = "https://nya-n.me",
                developers = listOf(Developer("kani", null)),
                organization = null,
                scm = Scm(null, null, null),
                licenses = setOf(
                    License(
                        name = "Apache-2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0",
                        hash = "abc123hash"
                    )
                )
            ),
            Library(
                uniqueId = "sample2",
                artifactVersion = null,
                name = "sample2",
                description = null,
                website = null,
                developers = listOf(),
                organization = null,
                scm = Scm(null, null, null),
                licenses = setOf(
                    License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT",
                        hash = "def456hash"
                    )
                ),
            ),
            Library(
                uniqueId = "sample3",
                artifactVersion = null,
                name = "sample3",
                description = null,
                website = null,
                developers = listOf(),
                organization = null,
                scm = null,
                licenses = setOf()
            )
        ),
        licenses = setOf(),
    )
    AppTheme {
        LicenseContent(
            snackbarHostState = snackbarHostState,
            libraries = libs,
            onBack = { }
        )
    }
}
