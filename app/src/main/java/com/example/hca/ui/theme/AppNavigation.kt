package com.example.hca.ui.theme

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hca.viewmodel.GithubViewModel

@Composable
fun AppNavigation(viewModel: GithubViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "repository_list") {
        composable("repository_list") {
            MainScreen(
                viewModel = viewModel,
                onRepositoryClick = { repository ->
                    navController.navigate("repository_detail/${repository.name}")
                }
            )
        }

        composable("repository_detail/{repositoryName}") { backStackEntry ->
            val repositoryName = backStackEntry.arguments?.getString("repositoryName")
            val repository =
                viewModel.getRepositoryByName(repositoryName)
            repository?.let {
                RepositoryDetailScreen(repository = it)
            }
        }
    }
}
