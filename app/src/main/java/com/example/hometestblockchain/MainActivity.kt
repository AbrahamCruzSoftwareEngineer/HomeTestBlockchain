package com.example.hometestblockchain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.hometestblockchain.ui.composables.ListDogImages
import com.example.hometestblockchain.ui.theme.HomeTestBlockchainTheme
import com.example.hometestblockchain.ui.viewmodel.MyViewModel
import com.example.hometestblockchain.ui.viewmodel.MyViewModel.Companion.DEFAULT_VALUE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.searchByNameLimited(DEFAULT_VALUE)
        setContent {
            HomeTestBlockchainTheme {
                AppSurface()
            }
        }
    }

    @Composable
    fun AppSurface() {
        val imageEntities by viewModel.images.observeAsState(initial = emptyList())
        val isLoading by viewModel.isLoading.observeAsState(initial = false)

        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
            if (isLoading) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val imageUrls = imageEntities.map { it.url }
                ListDogImages(
                    title = DEFAULT_VALUE,
                    imageUrls = imageUrls,
                    onTitleChange = { newTitle -> viewModel.searchByNameLimited(newTitle) },
                    onReloadClick = { newTitle -> viewModel.searchByNameLimited(newTitle) }
                )
            }
        }
    }
}