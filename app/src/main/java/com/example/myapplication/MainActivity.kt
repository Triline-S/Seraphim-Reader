@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import java.io.File


data class BarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon : ImageVector,
    val route: String
)


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
            MyApplicationTheme {
                val viewModel = viewModel<MainViewModel>()
                val dialogueQueue = viewModel.visiblePermissionDialogueQueue
                val navController = rememberNavController()
                val readPermissionResultLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {isGranted ->
                        viewModel.onPermissionResult(
                            permission = android.Manifest.permission.READ_MEDIA_IMAGES,
                            isGranted = isGranted
                        )
                    }
                )

                Scaffold(
                    bottomBar = {
                        var selectedItem by remember { mutableIntStateOf(0) }
                        val barItems = listOf(
                            BarItem(
                                title = "Proprium",
                                selectedIcon = Icons.Filled.List,
                                unselectedIcon = Icons.Outlined.List,
                                route = "browser"
                            ),
                            BarItem(
                                title = "Ordinarium",
                                selectedIcon = Icons.Filled.Build,
                                unselectedIcon = Icons.Outlined.Build,
                                route = "ordinarium"
                            ),
                            BarItem(
                                title = "Opcje",
                                selectedIcon = Icons.Filled.Settings,
                                unselectedIcon = Icons.Outlined.Settings,
                                route = "settings"
                            )
                        )
                        NavigationBar {
                            barItems.forEachIndexed { index, barItem ->
                                val selected = selectedItem == index
                                NavigationBarItem(
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navController.navigate(barItem.route)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) barItem.selectedIcon else barItem.unselectedIcon,
                                            contentDescription = barItem.title
                                        )
                                    },
                                    label = { Text(text = barItem.title) },
                                    alwaysShowLabel = selected
                                )
                            }
                        }
                    }
                ) {pv ->
                    NavHost(navController = navController, startDestination = "browser") {
                        composable("browser") {
                            val browserViewModel = viewModel<BrowserViewModel>()
                            Column{
                                TextField(
                                    value = browserViewModel.search,
                                    onValueChange = { browserViewModel.search = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text(text = "Wyszukaj partytury...")},
                                    singleLine = true
                                )
                                if (checkCallingOrSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                    ScoreList(
                                        scores = GetScoreList.getScores(scoreDir = GetScoreList.scoreDir.absolutePath)
                                            .filter { it.title.lowercase().contains(browserViewModel.search.lowercase()) },
                                        navController = navController,
                                        modifier = Modifier.padding(pv)
                                    )
                                } else {
                                    Button(
                                        onClick = { readPermissionResultLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES) },
                                        modifier = Modifier.padding(top = 32.dp)
                                    ) {
                                        Text(
                                            text = "Zezwól na dostęp do zdjęć",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                        composable("ordinarium") {
                            val massViewModel = viewModel<MassViewModel>()
                            if (checkCallingOrSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                Column(verticalArrangement = Arrangement.SpaceEvenly) {
                                    var expanded by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = !expanded }
                                    ) {
                                        TextField(
                                            readOnly = true,
                                            value = massViewModel.mass,
                                            onValueChange = { },
                                            label = { Text("Wybór Mszy") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                    expanded = expanded
                                                )
                                            },
                                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                                            modifier = Modifier.menuAnchor()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = {
                                                expanded = false
                                            }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text(text = "wg. ks. Pawlaka") },
                                                onClick = {
                                                    massViewModel.mass=
                                                        "Pawlak"; GetScoreList.changeMass("Pawlak")
                                                })
                                            DropdownMenuItem(
                                                text = { Text(text = "wg. ks. Ścibora") },
                                                onClick = {
                                                    massViewModel.mass =
                                                        "Ścibor"; GetScoreList.changeMass("Ścibor")
                                                })
                                            DropdownMenuItem(
                                                text = { Text(text = "Missa de Angelis") },
                                                onClick = {
                                                    massViewModel.mass =
                                                        "de Angelis"; GetScoreList.changeMass("de Angelis")
                                                })
                                            DropdownMenuItem(
                                                text = { Text(text = "Missa XVIII") },
                                                onClick = {
                                                    massViewModel.mass =
                                                        "Missa XVIII"; GetScoreList.changeMass("Missa XVIII")
                                                })
                                        }
                                    }

                                    OrdoList(navController, Modifier.padding(pv))

                                }
                            } else {
                                Button(
                                    onClick = { readPermissionResultLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES) },
                                    modifier = Modifier.padding(top = 32.dp)
                                ) {
                                    Text(
                                        text = "Zezwól na dostęp do zdjęć",
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        composable(
                            route = "browser/{scoreID}/{scoreType}",
                            arguments = listOf(
                                navArgument("scoreID") { type = NavType.IntType; defaultValue = 0},
                                navArgument("scoreType") {type = NavType.StringType; defaultValue = "proprium"}
                            )
                        ) {backStackEntry ->
                            val scoreType = backStackEntry.arguments?.getString("scoreType")
                            var pagerState = rememberPagerState {8}
                            var coroutineScope = rememberCoroutineScope()
                            val id = backStackEntry.arguments?.getInt("scoreID")
                            var bmpOp = BitmapFactory.Options(); bmpOp.inSampleSize = 1
                            var scoreScale = 0
                            var arrangement = arrayOf("F", "C")
                            var arrangementNum by remember { mutableIntStateOf(0) }
                            var pagerControls by remember { mutableStateOf(true) }
                            Column{
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    IconButton(onClick = { navController.navigateUp() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Go back"
                                        )
                                    }
                                    Text(
                                        text = if(scoreType == "ordo") GetScoreList.ordoList[id!!].title else GetScoreList.scoreList[id!!].title)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Button(onClick = {
                                            if (arrangementNum == 1) arrangementNum =
                                                0 else if (arrangementNum == 0) arrangementNum = 1
                                        }) {
                                            Text(text = arrangement[arrangementNum])
                                        }
                                        Button(onClick = { pagerControls = !pagerControls }) {
                                            Text(text = "Wł/Wył kontrolki pagera")
                                        }
                                    }
                                }
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) { page ->
                                    Column {
                                        val pageUri: File = if(scoreType == "ordo"){
                                            File("${GetScoreList.ordoList[id!!].path}/${arrangement[arrangementNum]}0${page + 1}.jpg")
                                        } else {
                                            File("${GetScoreList.scoreList[id!!].path}/${arrangement[arrangementNum]}0${page + 1}.jpg")
                                        }
                                        if (pageUri.exists()) {
                                            val bmp = BitmapFactory.decodeFile(pageUri.absolutePath, bmpOp).asImageBitmap()
                                            Image(bitmap = bmp, contentDescription = "Current Page")
                                        }
                                        else {
                                            Text(text = "Strona nie istnieje!")
                                        }

                                        //Page Controls
                                        if (pagerControls) {
                                            Row(modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .offset(y = (-100).dp)
                                                .background(color = Color.hsv(0F, 1F, 0F, 0.3F))
                                                .height(28.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                IconButton(onClick = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(
                                                            0
                                                        )
                                                    }
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.KeyboardArrowLeft,
                                                        contentDescription = "Przewiń do pierwszej strony"
                                                    )
                                                }
                                                IconButton(onClick = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(
                                                            pagerState.currentPage - 1
                                                        )
                                                    }
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = "Przewiń do poprzedniej strony"
                                                    )
                                                }
                                                IconButton(onClick = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(
                                                            pagerState.currentPage + 1
                                                        )
                                                    }
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.ArrowForward,
                                                        contentDescription = "Przewiń do następnej strony"
                                                    )
                                                }
                                                IconButton(onClick = {
                                                    coroutineScope.launch {
                                                        pagerState.animateScrollToPage(
                                                            pagerState.pageCount
                                                        )
                                                    }
                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Default.KeyboardArrowRight,
                                                        contentDescription = "Przewiń do ostatniej strony"
                                                    )
                                                }
                                            }
                                        }

                                    }

                                }

                            }

                        }
                        composable(route = "settings") {
                            val editor = sharedPref.edit()
                            var isPathTextfieldVisible by remember { mutableStateOf(false) }
                            var pathTextfield by remember { mutableStateOf(sharedPref.getString("path", "").toString()) }
                            Column{
                                TextButton(onClick = {
                                    isPathTextfieldVisible = !isPathTextfieldVisible
                                }) {
                                    Column {
                                        Text(
                                            text = "Ścieżka do partytur:",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = sharedPref.getString("path", "${Environment.getExternalStorageDirectory().absolutePath}/Documents/Scores").toString(),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                }
                                if (isPathTextfieldVisible) {
                                    Row {
                                        TextField(
                                            value = pathTextfield,
                                            onValueChange = { pathTextfield = it })
                                        Button(onClick = {
                                            editor.apply {
                                                putString("path", pathTextfield)
                                                commit()
                                            }
                                            GetScoreList.scoreDir = File("${sharedPref.getString("path", "").toString()}/Proprium")
                                            GetScoreList.ordoDir = File("${sharedPref.getString("path", "").toString()}/Ordinarium")
                                            navController.navigate("settings")
                                        }) {
                                            Text(text = "Zapisz")
                                        }
                                        Button(onClick = {
                                            editor.apply{
                                                putString("path", "${Environment.getExternalStorageDirectory().absolutePath}/Documents/Scores")
                                                commit()
                                            }
                                            GetScoreList.scoreDir = File("${sharedPref.getString("path", "").toString()}/Proprium")
                                            GetScoreList.ordoDir = File("${sharedPref.getString("path", "").toString()}/Ordinarium")
                                            navController.navigate("settings")
                                        }) {
                                            Text(text = "Przywróć domyślne")
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                        }
                    }
                }
            }
        }
    }
}
