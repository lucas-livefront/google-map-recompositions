package com.example.mapsrecompositions

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TraversalExample(
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.border(color = Color.Black, width = 2.dp),
    ) {
        Column(
            modifier = Modifier.semantics { isTraversalGroup = true },
        ) {
            Text(
                text = "First",
                modifier = Modifier
                    .padding(16.dp)
            )
            Text(
                text = "Second",
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        Text(
            text = "Third",
            modifier = Modifier
                .padding(16.dp),
        )
    }
}

@Composable
fun TraversalGroupExample(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                repeat(5) {
                    item {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Filter")
                        }
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                repeat(15) {
                    item {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        ) {
                            Text(
                                text = "I am a row item",
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 32.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(32.dp)
        ) {
            Text(text = "I am a button")
        }
    }
}

@Composable
fun MergedSemanticsBugExample(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .border(color = Color.Black, width = 2.dp,)
            .padding(16.dp)
    ) {
        Text(
            text = "First",
            modifier = Modifier.semantics { traversalIndex = 0f },
        )
        Text(
            text = "Third",
            modifier = Modifier.semantics { traversalIndex = 2f },
        )
        Text(
            text = "Second",
            modifier = Modifier.semantics { traversalIndex = 1f },
        )
    }
}

@Composable
fun TalkbackItemFocus(
    modifier: Modifier = Modifier,
) {
    val itemOneText = "Item One"
    val itemTwoText = "Item Two"
    val itemThreeText = "Item Three"
    val itemModifier = Modifier.padding(16.dp)

    Column(modifier = modifier) {
        Item(
            itemText = itemOneText,
            modifier = itemModifier,
        )
        Item(
            itemText = itemTwoText,
            modifier = itemModifier,
        )
        Item(
            itemText = itemThreeText,
            modifier = itemModifier,
        )
    }
}

@Composable
fun Item(
    itemText: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = itemText,
        modifier = modifier
            .onFocusChanged {
                Log.d("Item", "$itemText $it")
            }
            .focusable()
    )
}

@Composable
fun TalkbackItemFocusRequesting(
    modifier: Modifier = Modifier,
) {
    val itemOneText = "Item One"
    val itemOneFocusRequester = remember { FocusRequester() }
    val itemTwoText = "Item Two"
    val itemTwoFocusRequester = remember { FocusRequester() }
    val itemThreeText = "Item Three"
    val itemThreeFocusRequester = remember { FocusRequester() }
    val itemModifier = Modifier.padding(16.dp)

    LaunchedEffect(Unit) {
        delay(1000)
        itemOneFocusRequester.requestFocus()
        delay(1000)
        itemTwoFocusRequester.requestFocus()
        delay(1000)
        itemThreeFocusRequester.requestFocus()
    }

    Column(modifier = modifier) {
        FocusableItem(
            focusRequester = itemOneFocusRequester,
            itemText = itemOneText,
            modifier = itemModifier,
        )
        FocusableItem(
            focusRequester = itemTwoFocusRequester,
            itemText = itemTwoText,
            modifier = itemModifier,
        )
        FocusableItem(
            focusRequester = itemThreeFocusRequester,
            itemText = itemThreeText,
            modifier = itemModifier,
        )
    }
}

@Composable
fun FocusableItem(
    focusRequester: FocusRequester,
    itemText: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = itemText,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                Log.d("Item", "$itemText $it")
            }
            .focusable()
    )
}