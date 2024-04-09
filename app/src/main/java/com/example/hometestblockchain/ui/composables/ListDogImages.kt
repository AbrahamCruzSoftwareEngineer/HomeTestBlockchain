package com.example.hometestblockchain.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListDogImages(
    title: String,
    imageUrls: List<String>,
    onTitleChange: (String) -> Unit,
    onReloadClick: (String) -> Unit,
) {
    var textFieldValue by remember { mutableStateOf(title) }
    var imageItems by remember { mutableStateOf(imageUrls.mapIndexed { index, url -> ImageItem(index, url) }) }
    val reloadTriggered = remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onTitleChange(newValue)
                },
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(fontSize = 24.sp),
                singleLine = true,
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    reloadTriggered.value = true
                    onReloadClick(textFieldValue)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text("Reload")
            }
        }
        TabRowDefaults.Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray,
            thickness = 1.dp,
        )
        ImageListWithSwipeToDismiss(
            imageItems = imageItems,
            isLoading = reloadTriggered.value,
            onRemoveItem = { dismissedItem ->
                imageItems = imageItems.filterNot { it.id == dismissedItem.id }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListDogImagesPreview() {
    ListDogImages(
        title = "Dog Breeds",
        imageUrls = listOf(
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg",
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_1007.jpg",
            "https://images.dog.ceo/breeds/hound-afghan/n02088094_1023.jpg"
        ),
        onTitleChange = {},
        onReloadClick = {}
    )
}