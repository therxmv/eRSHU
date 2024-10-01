package com.therxmv.ershu.ui.rating.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.RatingItem
import com.therxmv.ershu.data.models.RatingModel
import com.therxmv.ershu.ui.base.views.inputs.RSHUInputsColors
import com.therxmv.ershu.ui.base.views.inputs.RSHUInputsColors.defaultOutlinedTextFieldColors
import com.therxmv.ershu.ui.rating.viewmodel.utils.InputState
import com.therxmv.ershu.ui.rating.viewmodel.utils.RatingState
import com.therxmv.ershu.utils.keyboardAsState

@Stable
@Composable
fun RatingScreenContent(
    modifier: Modifier,
    data: RatingModel,
    inputs: Map<RatingItem, InputState>,
    onValueChange: (RatingItem, String) -> Unit,
    ratingState: RatingState,
    calculateRating: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isKeyboardOpen by keyboardAsState()

    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen.not()) {
            focusManager.clearFocus()
        }
    }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        items(data.list) { item ->
            RatingInputItem(
                item = item,
                isLast = data.list.last() == item,
                inputState = inputs[item],
                onValueChange = onValueChange,
            )
        }

        item {
            ResultSection(
                calculateRating = calculateRating,
                ratingState = ratingState,
            )
        }
    }
}

@Composable
private fun ResultSection(
    calculateRating: () -> Unit,
    ratingState: RatingState,
) {
    Row(
        modifier = Modifier.padding(bottom = 52.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        CalculateButton(
            onClick = calculateRating,
        )

        Crossfade(
            targetState = ratingState,
            animationSpec = tween(300),
        ) { state ->
            when (state) {
                is RatingState.Success -> {
                    Text(
                        text = "${Res.string.rating_result}${state.rating}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                is RatingState.Error -> {
                    Text(
                        text = Res.string.rating_error,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is RatingState.Initial -> {}
            }
        }
    }
}

@Composable
private fun RatingInputItem(
    item: RatingItem,
    isLast: Boolean,
    inputState: InputState?,
    onValueChange: (RatingItem, String) -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        InputTitle(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp),
            name = item.name,
            credits = item.credits,
        )
        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            item = item,
            isLast = isLast,
            inputState = inputState,
            onValueChange = onValueChange,
        )
    }
}

@Composable
private fun InputTitle(
    modifier: Modifier = Modifier,
    name: String,
    credits: Int,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = "${Res.string.rating_input_credits} $credits",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun InputField(
    item: RatingItem,
    isLast: Boolean,
    inputState: InputState?,
    onValueChange: (RatingItem, String) -> Unit,
) {
    val value = (inputState as? InputState.Value)?.value.orEmpty()
    val isError = inputState is InputState.Error

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            onValueChange(item, it)
        },
        isError = isError,
        placeholder = {
            Text(text = Res.string.rating_input_placeholder)
        },
        shape = RoundedCornerShape(8.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (isLast) ImeAction.Done else ImeAction.Next,
        ),
        maxLines = 1,
        colors = defaultOutlinedTextFieldColors()
    )
}

@Composable
private fun CalculateButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = RSHUInputsColors.defaultButtonColors(),
    ) {
        Text(text = Res.string.rating_button_label)
    }
}