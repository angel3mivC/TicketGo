package mx.tec.ticketgo.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import mx.tec.ticketgo.ui.theme.ActiveElement
import mx.tec.ticketgo.ui.theme.UnactiveElement

@Composable
fun RowScope.NavBarItem(navController: NavController, currentRoute: String?, route: String, title: String, icon: ImageVector){
    NavigationBarItem(
        icon = { Icon(icon, contentDescription = title) },
        label = { Text(title) },
        selected = currentRoute == route,
        onClick = {
            navController.navigate(route){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.ActiveElement,
            unselectedIconColor = MaterialTheme.colorScheme.UnactiveElement,
            selectedTextColor = MaterialTheme.colorScheme.ActiveElement,
            unselectedTextColor = MaterialTheme.colorScheme.UnactiveElement,
            indicatorColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun NavBar(navController: NavController, currentRoute: String?, middleBottomIcon: ImageVector, middleBottomRoute: String){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavBarItem(navController, currentRoute, "inicio", "Inicio", Icons.Default.Home)
        FloatingActionButton(
            onClick = {
                navController.navigate(middleBottomRoute){
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
            // modifier = Modifier.offset(y = -20.dp)
        ) {
            Icon(middleBottomIcon, "", tint = MaterialTheme.colorScheme.background)
        }
        NavBarItem(navController, currentRoute, "historial", "Historial", Icons.Default.Menu)
    }
}