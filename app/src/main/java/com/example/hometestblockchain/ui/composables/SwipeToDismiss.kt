package com.example.hometestblockchain.ui.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

data class ImageItem(
    val id: Int,
    val url: String
)

@Composable
fun ImageListWithSwipeToDismiss(
    imageItems: List<ImageItem>,
    isLoading: Boolean,
    onRemoveItem: (ImageItem) -> Unit
) {
    val mutableImageItems = remember { imageItems.toMutableStateList() }
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (imageItems.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("The list is empty", style = MaterialTheme.typography.h5)
        }
    } else {
        LazyColumn {
            items(items = mutableImageItems, key = { it.id }) { item ->
                SwipeDismissImageItem(
                    imageItem = item,
                    onDismiss = { onRemoveItem(item) },
                    foregroundContent = {
                        SwipeForeground(
                            imageUrl = item.url,
                            description = "Nice Puppy",
                            price = "$0.00"
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDismissImageItem(
    imageItem: ImageItem,
    onDismiss: () -> Unit,
    foregroundContent: @Composable () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            when (dismissValue) {
                // Only allow dismissing when swiping from DismissedToStart
                DismissValue.DismissedToStart -> {
                    onDismiss()
                    true
                }
                // Prevent dismissing in other directions
                DismissValue.DismissedToEnd -> false
                else -> false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
                    .fillMaxWidth(),
                elevation = 20.dp
            ) {
                Column {
                    foregroundContent() // Invoke the provided foreground content
                    Image(
                        painter = rememberAsyncImagePainter(model = imageItem.url),
                        contentDescription = "Loaded image from URL",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}

@Composable
fun SwipeForeground(
    imageUrl: String,
    description: String,
    price: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle image on the right
        Box(
            modifier = Modifier
                .clip(CircleShape)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Item Image",
                modifier = Modifier.padding(2.dp)
            )
        }
        // Description in the middle
        Text(
            text = description,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            textAlign = TextAlign.Justify,
            fontSize = 18.sp
        )
        // Price on the left
        Text(
            modifier = Modifier.padding(16.dp),
            text = price,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageListWithSwipeToDismissPreview() {
    val sampleImageItems = listOf(
        ImageItem(id = 1, url = "https://example.com/image1.jpg"),
        ImageItem(id = 2, url = "https://example.com/image2.jpg"),
        ImageItem(id = 3, url = "https://example.com/image3.jpg")
    )

    ImageListWithSwipeToDismiss(
        imageItems = sampleImageItems,
        isLoading = false,
        onRemoveItem = {}
    )
}