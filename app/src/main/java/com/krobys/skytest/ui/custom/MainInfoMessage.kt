package com.krobys.skytest.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krobys.skytest.R
import com.krobys.skytest.manager.AppMessage
import com.krobys.skytest.ui.theme.SkyTestTheme
import kotlinx.coroutines.delay

@Composable
fun MainInfoMessage(errorMessageState: State<AppMessage?>) {
    val transitionState = remember { MutableTransitionState(initialState = false) }
    transitionState.targetState = !errorMessageState.value?.message.isNullOrBlank()

    LaunchedEffect(key1 = errorMessageState.value) {
        if (!errorMessageState.value?.message.isNullOrBlank()) {
            transitionState.targetState = true
            delay(errorMessageState.value?.messageDuration ?: 3000L) // Delay for 3 seconds
            transitionState.targetState = false
        }
    }

    AnimatedVisibility(
        visibleState = transitionState,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 8.dp)
                .noRippleClickable { transitionState.targetState = false }, // Hide when user taps on it
        ) {
            MessageItem(message = errorMessageState.value)
        }
    }
}

@Composable
private fun MessageItem(message: AppMessage?) {
    Row(
        modifier = Modifier
            .padding(18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = when(message?.messageType) {
                AppMessage.AppMessageType.ERROR -> {
                    ImageVector.vectorResource(id = R.drawable.icon_error_x)
                }
                AppMessage.AppMessageType.INFO -> {
                    ImageVector.vectorResource(id = R.drawable.icon_error_x)
                }
                else -> {
                    ImageVector.vectorResource(id = R.drawable.icon_error_x)
                }
            },
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message?.message ?: "",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
private fun PreviewMainErrorMessage() {
    val errorMessageState = remember {
        mutableStateOf(
            AppMessage(
                "Something went wrong",
                System.currentTimeMillis(),
                AppMessage.AppMessageType.ERROR
            )
        )
    }
    SkyTestTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            MainInfoMessage(errorMessageState = errorMessageState)
        }
    }
}

private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}