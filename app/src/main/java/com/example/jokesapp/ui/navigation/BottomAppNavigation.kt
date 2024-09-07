package com.example.jokesapp.ui.navigation

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.jokesapp.R
import com.example.jokesapp.ui.destinations.TopLevelDestinations
import com.example.jokesapp.ui.viewmodel.JokesViewmodel
import com.example.jokesapp.util.addSoundEffect
import com.example.jokesapp.util.toastMsg

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppNavigation(
    viewmodel: JokesViewmodel = hiltViewModel(),
    bottomNavItems: List<BottomNavigationItem>
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()
    val view: View = LocalView.current
    Scaffold(
        topBar = {
            val context = LocalContext.current
            TopAppBar(modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(text = "Jokes", fontFamily = FontFamily.Cursive, fontSize = 50.sp)
                },
                actions = {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            modifier = Modifier.clickable {
                                addSoundEffect(view)
                                toastMsg(context, "feature will be added soon")
                            },
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "notification"
                        )
                        Icon(
                            modifier = Modifier.clickable {
                                addSoundEffect(view)
                                toastMsg(context, "feature will be added soon")
                            },
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                },
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.ic_app),
                        contentDescription = "App Icon"
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                bottomNavItems.forEachIndexed { index, bottomItem ->
                    NavigationBarItem(
                        label = { Text(text = bottomItem.title) },
                        selected = index == selectedIndex,
                        onClick = {
                            addSoundEffect(view)
                            navController.popBackStack()
                            selectedIndex = index
                            if (selectedIndex == 1) {
                                navController.navigate(TopLevelDestinations.BookMarks.route)
                            } else if (selectedIndex == 2) {
                                navController.navigate(TopLevelDestinations.Delete.route)
                            } else {
                                navController.navigate(TopLevelDestinations.Home.route)
                            }
                        },
                        icon = {
                            val id =
                                if (index == selectedIndex) bottomItem.selectedIcon else
                                    bottomItem.unselectedIcon
                            Icon(
                                painter = painterResource(id = id),
                                contentDescription = "Bottom Navigation Item"
                            )
                        })
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            viewModel = viewmodel,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )

    }
}