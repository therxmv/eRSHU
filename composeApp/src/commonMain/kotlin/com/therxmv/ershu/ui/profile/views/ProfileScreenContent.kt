package com.therxmv.ershu.ui.profile.views

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.therxmv.ershu.Res
import com.therxmv.ershu.ui.specialtyinfo.views.RSHUInputsColors
import com.therxmv.ershu.ui.views.RSHUCard
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    facultyName: String,
    specialtyName: String,
    onEditClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserAvatar()
        Spacer(modifier = Modifier.height(36.dp))

        ProfileInfoCard(
            titleText = Res.string.profile_faculty_title,
            nameText = facultyName,
        )
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInfoCard(
            titleText = Res.string.profile_specialty_title,
            nameText = specialtyName,
        )

        Spacer(modifier = Modifier.weight(1f))

        EditButton(onClick = onEditClick)
    }
}

@Composable
private fun ProfileInfoCard(
    titleText: String,
    nameText: String,
) {
    RSHUCard(
        rowModifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f),
            text = titleText,
            fontSize = 20.sp,
        )

        Text(
            text = nameText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
        )
    }
}

@Composable
private fun EditButton(
    onClick: () -> Unit,
) {
    val width = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        width.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400),
        )
    }

    Button(
        modifier = Modifier.fillMaxWidth(width.value),
        colors = RSHUInputsColors.defaultButtonColors(),
        onClick = onClick,
    ) {
        Text(text = Res.string.profile_edit_button)
    }
}

@Composable
private fun UserAvatar() {
    val scale = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        )
    }

    Box(
        modifier = Modifier
            .size(140.dp)
            .scale(scale.value)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .size(56.dp),
            imageVector = FeatherIcons.User,
            contentDescription = "Avatar",
        )
    }
    Spacer(modifier = Modifier.height(8.dp))

    val textStyle = TextStyle(
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    )
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = Res.string.profile_welcome,
        style = textStyle,
    )
}