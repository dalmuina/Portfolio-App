package com.dalmuina.portolioapp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dalmuina.portolioapp.domain.model.GameDetail
import com.dalmuina.portolioapp.ui.theme.PortolioAppTheme
import com.dalmuina.portolioapp.ui.theme.primaryContainerDark
import com.dalmuina.portolioapp.ui.view.components.MainImage
import com.dalmuina.portolioapp.ui.view.components.MainTopBar
import com.dalmuina.portolioapp.ui.view.components.MetaWebsite
import com.dalmuina.portolioapp.ui.view.components.ReviewCard
import com.dalmuina.portolioapp.ui.viewmodel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(navController: NavController, id: Int,viewModel: GamesViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getGameById(id)
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.clean()
        }
    }
    val detail by viewModel.detail.collectAsState()

    Scaffold(
        topBar = {
            MainTopBar (title = detail.name, showBackButton = true, onClickBackButton = {
                navController.popBackStack() })
        }
    ) {
        ContentDetailView(it, detail)
    }
}

@Composable
fun ContentDetailView(pad: PaddingValues, detail: GameDetail) {

    Column(
        modifier = Modifier
            .padding(pad)
            .background(primaryContainerDark)
    ) {
        MainImage(imageUrl = detail.backgroundImage)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 5.dp)
        ) {
            MetaWebsite(detail.website)
            ReviewCard(detail.metacritic)
        }

        val scroll = rememberScrollState(0)
        Text(text = detail.descriptionRaw,
            color = Color.White,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                .verticalScroll(scroll)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailView(){
    PortolioAppTheme {
        val mockGameDetail = GameDetail(
            name = "Gta",
            descriptionRaw = "This is a description of the GTA game, this is a " +
                    "description of the " +
                    "GTA game, this is a description of the GTA game, this is a " +
                    "description of the GTA game",
            metacritic = 100,
            website = "www.google.com",
            backgroundImage = "https://media-rockstargames-com.akamaized.net/mfe6/prod" +
                    "/__common/img/71d4d17edcd49703a5ea446cc0e588e6.jpg")

        ContentDetailView(PaddingValues(16.dp), mockGameDetail)
    }
}
