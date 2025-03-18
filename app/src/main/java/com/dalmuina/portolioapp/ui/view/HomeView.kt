package com.dalmuina.portolioapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dalmuina.portolioapp.domain.model.GameItem
import com.dalmuina.portolioapp.ui.theme.PortolioAppTheme
import com.dalmuina.portolioapp.ui.theme.primaryContainerDark
import com.dalmuina.portolioapp.ui.view.components.CardGame
import com.dalmuina.portolioapp.ui.view.components.MainTopBar
import com.dalmuina.portolioapp.ui.viewmodel.GamesViewModel

@Composable
fun HomeView(navController: NavController, viewModel: GamesViewModel = hiltViewModel()){
    val games by viewModel.games.collectAsState()
    Scaffold(
        topBar = {
            MainTopBar(title = "API GAMES", onClickBackButton = {})
        }
    ) {
        ContentHomeView(it, games){
            navController.navigate("DetailView/${it}")
        }
    }

}

@Composable
fun ContentHomeView(pad: PaddingValues, games: List<GameItem>, onClick:(id:Int)->Unit){
    LazyColumn(
        modifier = Modifier
            .padding(pad)
            .background(primaryContainerDark)
    ){
        items(games){ item ->
            CardGame(item) {
                onClick(item.id)
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

@Preview(showBackground = true)
@Composable
fun PreviewContentHomeView() {
    PortolioAppTheme {
        val mockGames = listOf(
            GameItem(id = 1, name = "GTA","https://media-rockstargames-com.akamaized.net/mfe6/prod" +
                    "/__common/img/71d4d17edcd49703a5ea446cc0e588e6.jpg"),
            GameItem(id = 1, name = "Read Dead Redemption","Image"),
            GameItem(id = 1, name = "Tetris","Image"),
        )
        ContentHomeView(
            pad = PaddingValues(16.dp),
            games = mockGames,
            onClick = {}
        )
    }
}
