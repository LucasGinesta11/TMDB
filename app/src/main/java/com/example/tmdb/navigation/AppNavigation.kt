package com.example.proyecto.navigation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.tmdb.ui.MovieScreen
import com.example.tmdb.R
import com.example.tmdb.ui.MovieDetails
import com.example.tmdb.ui.MovieFavourite
import com.example.tmdb.ui.MovieSettings
import com.example.tmdb.viewModel.MovieViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController, movieViewModel: MovieViewModel) {
    /**
     * Contexto actual de la aplicacion (en mi caso me ayuda a cerrar la aplicacion)
     */
    val context = LocalContext.current

    /**
     * Estado del drawer que queda guardado(empieza cerrado)
     */
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    /**
     * Permite lanzar corrutinas (en este caso para las opciones de navegacion)
     */
    val scope = rememberCoroutineScope()

    /**
     * Estado del buscador que queda guardado(empieza cerrado)
     */
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    /**
     * Texto de la busqueda (empieza vacio)
     */
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    /**
     * ModalNavigationDrawer que crea el menu lateral con sus diferentes opciones
     */
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            /**
             * ModalDrawerSheet donde se ubican las opciones del menu
             */
            ModalDrawerSheet {
                Column(Modifier.padding(8.dp)) {
                    /**
                     * Imagen de cabecera de la API
                     */
                    Image(
                        painterResource(R.drawable.tmdb),
                        contentDescription = "Imagen TMDB",
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    HorizontalDivider()

                    /**
                     * Primera opcion que lleva a la lista de cards de peliculas si no se
                     * esta en esta pantalla
                     */
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
                            scope.launch {
                                drawerState.close()
                            }

                            if (navController.currentDestination?.route != "MovieScreen") {
                                navController.navigate("MovieScreen")
                            }
                        }

                    )

                    /**
                     * Segunda opcion que lleva a la lista de peliculas agregadas a favoritas
                     * si no se esta en esta pantalla
                     */
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
                            scope.launch {
                                drawerState.close()

                                if (navController.currentDestination?.route != "MovieFavorite") {
                                    navController.navigate("MovieFavorite")
                                }
                            }
                        }
                    )

                    /**
                     * Tercera opcion que lleva a los ajustes de la aplicacion (por ahora es algo visual)
                     */
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
                            scope.launch {
                                drawerState.close()
                            }
                            if (navController.currentDestination?.route != "MovieSettings") {
                                navController.navigate("MovieSettings")
                            }
                        }
                    )

                    /**
                     * La cuarta opcion coge el contexto de la app y sale de ella (la finaliza)
                     */
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
         * Scaffold para manejar correctamente la barra superior e inferior y el contenido
         */
        Scaffold(
            /**
             * TopBar que contiene una flecha para volver atras (popBackStack), titulo de la API
             * y un icono Search para buscar peliculas
             */
            topBar = {
                TopAppBar(
                    title = {
                        //Icono de busqueda seleccionado
                        if (isSearchActive) {
                            TextField(
                                value = searchText,
                                onValueChange = {
                                    searchText = it
                                    movieViewModel.setSearchQuery(it.text)

                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
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
                                contentDescription = "Icono para volver atras",
                                tint = Color.White
                            )
                        }

                    },

                    actions = {
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Buscador",
                                tint = Color.White,
                            )
                        }
                    }

                )
            },

            /**
             * BottomBar para desplazarse entre las 3 screens principales (lista de card de peliculas,
             * peliculas favoritas y ajustes de la aplicacion)
             */
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ) {

                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieScreen") {
                                navController.navigate("MovieScreen")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "Peliculas",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieFavorite") {
                                navController.navigate("MovieFavorite")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favoritos",
                            modifier = Modifier.size(32.dp)
                        )

                    }

                    IconButton(
                        onClick = {
                            if (navController.currentDestination?.route != "MovieSettings") {
                                navController.navigate("MovieSettings")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Ajustes",
                            modifier = Modifier.size(32.dp)
                        )

                    }
                }

            }
        ) { innerPadding ->
            /**
             * NavHost que maneja la navegacion entre pantallas
             */
            NavHost(
                navController = navController,
                startDestination = "MovieScreen",
                Modifier.padding(innerPadding)
            ) {
                composable("MovieScreen") {
                    MovieScreen(
                        movieViewModel,
                        navController
                    )
                }
                composable("MovieFavorite") { MovieFavourite(movieViewModel, navController) }
                composable("MovieSettings") { MovieSettings() }
                composable("MovieDetails/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")
                    if (movieId != null) {
                        val movieItem = movieViewModel.selectedMovie.value
                        if (movieItem != null) {
                            MovieDetails(movieItem, movieViewModel)
                        }
                    }
                }
            }
        }
    }
}