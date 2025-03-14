package com.dalmuina.portolioapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dalmuina.portolioapp.ui.theme.CUSTOM_BLACK
import com.dalmuina.portolioapp.ui.view.components.CardGame
import com.dalmuina.portolioapp.ui.view.components.MainTopBar
import com.dalmuina.portolioapp.ui.viewmodel.GamesViewModel

@Composable
fun HomeView(viewModel: GamesViewModel, navController: NavController){
    Scaffold(
        topBar = {
            MainTopBar(title = "API GAMES", onClickBackButton = {})
        }
    ) {
        ContentHomeView(viewModel, it, navController)
    }

}

@Composable
fun ContentHomeView(viewModel: GamesViewModel, pad: PaddingValues, navController: NavController){
    val games by viewModel.games.collectAsState()
    LazyColumn(
        modifier = Modifier
            .padding(pad)
            .background(CUSTOM_BLACK)
    ){
        items(games){ item ->
            CardGame(item) {
                navController.navigate("DetailView/${item.id}")
            }
            item.name?.let {
                Text(text = it,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }
}
