package com.example.jokesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.jokesapp.ui.navigation.BottomAppNavigation
import com.example.jokesapp.ui.navigation.BottomNavigationItem
import com.example.jokesapp.ui.theme.JokesAppTheme
import com.example.jokesapp.ui.viewmodel.JokesViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<JokesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JokesAppTheme {
                // A surface container using the 'background' color from the theme
                val bottomNavItems = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = R.drawable.baseline_home_24,
                        unselectedIcon = R.drawable.outline_home_24
                    ),
                    BottomNavigationItem(
                        title = "Bookmarks",
                        selectedIcon = R.drawable.baseline_bookmarks_24,
                        unselectedIcon = R.drawable.outline_bookmarks_24
                    ),
                    BottomNavigationItem(
                        title = "Delete",
                        selectedIcon = R.drawable.baseline_delete_24,
                        unselectedIcon = R.drawable.baseline_delete_outline_24
                    )
                )
                BottomAppNavigation(viewmodel = viewModel, bottomNavItems = bottomNavItems)
            }
        }
    }
}



