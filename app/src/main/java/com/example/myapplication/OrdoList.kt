package com.example.myapplication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SectionDivider(text: String) {
    val spacerH = 16.dp
    Spacer(modifier = Modifier.height(spacerH))
    Text(text = text, style = MaterialTheme.typography.labelLarge)
    Divider()
    Spacer(modifier = Modifier.height(spacerH / 2))
}

@Composable
fun OrdoList(navController: NavHostController, modifier: Modifier) {
    LazyColumn(modifier = modifier){

        item { SectionDivider(text = "Obrzędy Wstępne") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[5],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[6],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }

        item { SectionDivider(text = "Liturgia Słowa") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[2],
                navController = navController,
                type = "ordo"
            )
        }

        item { SectionDivider(text = "Przygotowanie Darów") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }

        item { SectionDivider(text = "Modlitwa Eucharystyczna") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[3],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[7],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[4],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[1],
                navController = navController,
                type = "ordo"
            )
        }

        item { SectionDivider(text = "Obrzędy Komunii") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[8],
                navController = navController,
                type = "ordo"
            )
        }

        item {
            ScoreCard(
                score = GetScoreList.ordoList[1],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[9],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[5],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }

        item { SectionDivider(text = "Obrzędy Zakończenia") }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[0],
                navController = navController,
                type = "ordo"
            )
        }
        item {
            ScoreCard(
                score = GetScoreList.ordoList[10],
                navController = navController,
                type = "ordo"
            )
        }
    }
}