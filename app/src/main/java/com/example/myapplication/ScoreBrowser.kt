@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


data class Score(
    var  title: String,
    var desc: String,
    var path: String,
    var tags: List<String>? = null,
    var id: Number = 0,
    var type: String = "proprium"
)

@Composable
fun ScoreCard(score: Score, navController: NavHostController, type: String)  {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        onClick = { navController.navigate("browser/" + score.id + "/" + type) }

    ) {
        Column {
            ScoreTitle(title = score.title)
            ScoreSubtext(desc = score.id.toString() + "; " + score.tags + "; " + type + "; ")
        }
    }
}
//File("${GetScoreList.scoreList[id].path}/").list().count()
@Composable
fun ScoreTitle(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp),
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
fun ScoreSubtext(desc: String){
    Text(text = desc,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun ScoreList(scores: List<Score>, navController: NavHostController, modifier: Modifier = Modifier){
    val listState = rememberLazyListState()

    LazyColumn (
        state = listState,
        modifier = modifier
    ){
        items(scores) {
                score -> ScoreCard(score, navController, type = "proprium")
        }
    }
}
