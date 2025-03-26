package com.example.tmdb.navigation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tmdb.R
import com.example.tmdb.ui.MovieDetails
import com.example.tmdb.ui.MovieFavourite
import com.example.tmdb.ui.MovieScreen
import com.example.tmdb.ui.MovieSettings
import com.example.tmdb.viewModel.MovieViewModel
import kotlinx.coroutines.launch

/**
 * Clave que maneja la navegacion de la aplicacion entre pantallas, las barras superior e inferior
 * y el buscador
 *
 * @param navController Llamada a NavHostController para manejar la navegacion
 * @param movieViewModel Llamada a MovieViewModel para controlar el buscador y la logica de las clases
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController, movieViewModel: MovieViewModel) {
    //Controla el contexto de la aplicaion (en mi caso para cerrarla cuando se seleccione la opcion de salir)
    val context = LocalContext.current
    //Maneja el menu lateral (empieza cerrado)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    //Corrutinas para manejar la navegacion
    val scope = rememberCoroutineScope()
    //Manejo del Search (empieza deseleccionado)
    var isSearchActive by remember { mutableStateOf(false) }
    //Control del searchText (empieza vacio)
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    /**
     * Menu lateral que se abre al deslizar a la izquierda
     *
     * Opciones de lista de peliculas, lista de favoritos, ajustes de la aplicacion y salida de esta
     */
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.padding(8.dp)) {
                    Image(
                        painterResource(R.drawable.tmdb),
                        contentDescription = "Imagen TMDB",
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    HorizontalDivider()

                    // Navegación
                    NavigationDrawerItem(
                        label = { Text("Peliculas", color = Color.Blue, fontSize = 20.sp) },
                        selected = navController.currentDestination?.route == "MovieScreen",
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = "Peliculas",
                                tint = Color.Blue
                            )
                        },
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (navController.currentDestination?.route != "MovieScreen") {
                                navController.navigate("MovieScreen")
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Favoritos", color = Color.Blue, fontSize = 20.sp) },
                        selected = navController.currentDestination?.route == "MovieFavorite",
                        icon = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Favoritos",
                                tint = Color.Blue
                            )
                        },
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (navController.currentDestination?.route != "MovieFavorite") {
                                navController.navigate("MovieFavorite")
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Ajustes", color = Color.Blue, fontSize = 20.sp) },
                        selected = navController.currentDestination?.route == "MovieSettings",
                        icon = {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Ajustes",
                                tint = Color.Blue
                            )
                        },
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (navController.currentDestination?.route != "MovieSettings") {
                                navController.navigate("MovieSettings")
                            }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Salir", color = Color.Blue, fontSize = 20.sp) },
                        selected = false,
                        icon = {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Salir",
                                tint = Color.Blue
                            )
                        },
                        onClick = { (context as Activity).finish() }
                    )
                }
            }
        },
    ) {
        /**
         * Scaffold para tener un control ordenado de las barras superior e inferior y de su contenido
         *
         * La barra superior contiene una flecha para volver atras (popBackStack), un titulo y un Search
         * que abre su buscador cuando clickas en el icono, lo que cambiara el titulo de la app por el
         * TextField
         *
         * La barra inferior contiene una simple navegacion entre las pantallas de lista de peliculas, lista
         * de favoritos y ajustes de la app
         */
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (isSearchActive) {
                            TextField(
                                value = searchText,
                                onValueChange = {
                                    searchText = it
                                    movieViewModel.setSearchQuery(it.text)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Buscar...", color = Color.White) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                        } else {
                            Text("TheMovieDB")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Blue,
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Icono para volver atrás",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        if (isSearchActive) {
                            IconButton(onClick = {
                                searchText = TextFieldValue("")
                                isSearchActive = false
                                movieViewModel.setSearchQuery("")
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Cerrar búsqueda",
                                    tint = Color.White
                                )
                            }
                        } else {
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Buscador",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                )
            },

            bottomBar = {
                BottomAppBar(containerColor = Color.Blue, contentColor = Color.White) {
                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieScreen") navController.navigate(
                                "MovieScreen"
                            )
                        },
                        modifier = Modifier.weight(1f) // Se aplica el peso para distribuir uniformemente
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Peliculas")
                    }

                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieFavorite") navController.navigate(
                                "MovieFavorite"
                            )
                        },
                        modifier = Modifier.weight(1f) // Se aplica el peso para distribuir uniformemente
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favoritos")
                    }

                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieSettings") navController.navigate(
                                "MovieSettings"
                            )
                        },
                        modifier = Modifier.weight(1f) // Se aplica el peso para distribuir uniformemente
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Ajustes")
                    }
                }

            }
        ) { innerPadding ->
            /**
             * NavHost que controla la navegacion entre todas las pantallas de la app
             */
            NavHost(
                navController = navController,
                startDestination = "MovieScreen",
                Modifier.padding(innerPadding)
            ) {
                composable("MovieScreen") {
                    MovieScreen(movieViewModel, navController)
                }
                composable("MovieFavorite") { MovieFavourite(movieViewModel, navController) }
                composable("MovieSettings") { MovieSettings() }
                composable("MovieDetails/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")

                    //Pelicula correspondiente de acuerdo con su id
                    val movieDetails = movieViewModel.items.find { it.id.toString() == movieId }

                    if (movieDetails != null) {
                        //Muestra los detalles de la película
                        MovieDetails(movieDetails, movieViewModel)
                    }
                }
            }
        }
    }
}
