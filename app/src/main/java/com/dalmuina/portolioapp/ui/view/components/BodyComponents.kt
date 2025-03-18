package com.dalmuina.portolioapp.ui.view.components


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dalmuina.portolioapp.domain.model.GameItem

@Preview(widthDp = 200)
@Preview(widthDp = 300)
@Preview(widthDp = 400)
@Preview(widthDp = 500)
@Preview(uiMode = UI_MODE_NIGHT_YES)
annotation class PreviewWidths

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(title: String, showBackButton: Boolean = false, onClickBackButton: () -> Unit){
    TopAppBar(
        title = { Text(text = title, color = Color.White, fontWeight = FontWeight.ExtraBold) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            if(showBackButton){
                IconButton(onClick={onClickBackButton()}) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "",tint = Color.White)
                }
            }
        },
        actions = {
            if(!showBackButton){
                IconButton(onClick={onClickBackButton()}) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "",tint = Color.White)
                }
            }
        }
    )
}

@PreviewWidths
@Composable
fun MainTopBarPreviewBack(){
    MainTopBar(title = "API GAMES",true){}
}

@Composable
fun MainTopBarPreviewMain(){
    MainTopBar(title = "API GAMES",false){}
}

@Composable
fun CardGame(game: GameItem, onClick:()->Unit){
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .shadow(40.dp)
            .clickable{onClick()}
    )   {
        Column {
            MainImage(game.backgroundImage)
        }

    }
}

@Composable
fun MainImage(imageUrl:String?){
    val image = rememberAsyncImagePainter(model = imageUrl)

    Image(painter = image,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp))
}

@SuppressLint("UseKtx")
@Composable
fun MetaWebsite(url:String){
    val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    Column{
        Text(text= "METASCORE",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            modifier = Modifier.padding(top = 10.dp,bottom = 10.dp))
        Button(onClick = {context.startActivity(intent)},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray,
                contentColor = Color.White)
        ){
            Text(text="Sitio Web")
        }
    }
}

@Composable
fun ReviewCard(metascore: Int){
    Card(
        modifier = Modifier
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ){
            Text(text= metascore.toString(),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize= 50.sp)
        }
    }
}

@Composable
fun Loader(){
    val circleColors:List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary
    )

    val infiniteTransition = rememberInfiniteTransition(label="")
    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue= 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 360,
                easing = LinearEasing
            )
        ), label = ""
    )

    CircularProgressIndicator(
        progress = { 1f },
        modifier = Modifier
            .size(size = 100.dp)
            .rotate(degrees = rotateAnimation)
            .border(
                width = 4.dp,
                brush = Brush.sweepGradient(circleColors),
                shape = CircleShape
            ),
        color = MaterialTheme.colorScheme.background,
        strokeWidth = 1.dp,
        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    )

}
