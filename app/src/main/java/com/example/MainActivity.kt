package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.StudioFeature
import com.example.ui.ScreenDestination
import com.example.ui.StudioViewModel
import com.example.ui.components.StudioDrawerContent
import com.example.ui.screens.ChatStudioScreen
import com.example.ui.screens.FeatureWorkspaceScreen
import com.example.ui.screens.MediaPlayerScreen
import com.example.ui.screens.ProjectLibraryScreen
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioDarkSurface
import com.example.ui.theme.StudioObsidian
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: StudioViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedFeature by viewModel.selectedFeature.collectAsState()

    val screenTitle = when (currentScreen) {
        ScreenDestination.CHAT_STUDIO -> "MediaStudio AI"
        ScreenDestination.FEATURE_WORKSPACE -> selectedFeature.shortName
        ScreenDestination.PROJECT_LIBRARY -> "Project Library"
        ScreenDestination.MEDIA_PLAYER -> "Master Player"
    }

    val screenSubtitle = when (currentScreen) {
        ScreenDestination.CHAT_STUDIO -> "Conversational Audio/Video AI"
        ScreenDestination.FEATURE_WORKSPACE -> if (selectedFeature.isUnlimitedDuration) "Unlimited Duration Suite" else "Strict 30-50m Production"
        ScreenDestination.PROJECT_LIBRARY -> "Saved Exports & Sharing"
        ScreenDestination.MEDIA_PLAYER -> "4K 60FPS Dynamic Render"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = StudioDarkSurface
            ) {
                StudioDrawerContent(
                    viewModel = viewModel,
                    currentScreen = currentScreen,
                    selectedFeature = selectedFeature,
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            containerColor = StudioObsidian,
            topBar = {
                TopAppBar(
                    modifier = Modifier.testTag("app_top_bar"),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = StudioDarkSurface,
                        titleContentColor = TextPrimary,
                        navigationIconContentColor = TextPrimary,
                        actionIconContentColor = TextSecondary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("hamburger_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Features Menu",
                                tint = TextPrimary
                            )
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f, fill = false)) {
                                Text(
                                    text = screenTitle,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = screenSubtitle,
                                    fontSize = 10.sp,
                                    color = EmeraldLight
                                )
                            }
                        }
                    },
                    actions = {
                        // AI Status Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldPrimary.copy(alpha = 0.15f))
                                .border(1.dp, EmeraldPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(EmeraldLight)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "DSP Active",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Quick Navigation Action
                        if (currentScreen != ScreenDestination.CHAT_STUDIO) {
                            IconButton(
                                onClick = { viewModel.navigateTo(ScreenDestination.CHAT_STUDIO) },
                                modifier = Modifier.testTag("nav_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "Chat",
                                    tint = TextSecondary
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.navigateTo(ScreenDestination.PROJECT_LIBRARY) },
                                modifier = Modifier.testTag("nav_library_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Folder,
                                    contentDescription = "Library",
                                    tint = TextSecondary
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    ScreenDestination.CHAT_STUDIO -> ChatStudioScreen(viewModel = viewModel)
                    ScreenDestination.FEATURE_WORKSPACE -> FeatureWorkspaceScreen(viewModel = viewModel)
                    ScreenDestination.PROJECT_LIBRARY -> ProjectLibraryScreen(viewModel = viewModel)
                    ScreenDestination.MEDIA_PLAYER -> MediaPlayerScreen(viewModel = viewModel)
                }
            }
        }
    }
}
