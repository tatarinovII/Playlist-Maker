package com.practicum.playlistmaker.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.PlaylistMakerTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val isDarkTheme by viewModel.observeSwitchState().observeAsState(false)
    PlaylistMakerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            //Title
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                text = stringResource(R.string.settings),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily(Font(R.font.ys_display_medium)),
                fontWeight = FontWeight.W400
            )
            //Dark theme switch
            MaterialSwitch(isDarkTheme, {
                viewModel.switchTheme(it)
            })
            //Share
            ItemComponent(stringResource(R.string.share_app), R.drawable.ic_share) {
                viewModel.onShareButtonClicked()
            }
            //Contact support
            ItemComponent(stringResource(R.string.contact_support), R.drawable.ic_support) {
                viewModel.onContactSupportButtonClicked()
            }
            //User agreement
            ItemComponent(stringResource(R.string.user_agreement), R.drawable.ic_arrow_forward) {
                viewModel.onUserAgreementButtonClicked()
            }
        }
    }
}

@Composable
private fun ItemComponent(title: String, iconRes: Int, onItemClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontFamily = FontFamily(Font(R.font.ys_display_regular)),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.colorScheme.primary
        )
        Image(painter = painterResource(iconRes), contentDescription = null)
    }
}

@Composable
fun MaterialSwitch(
    checked: Boolean, onCheckedChange: (Boolean) -> Unit
) {
    val textColor = MaterialTheme.colorScheme.primary.toArgb()
    AndroidView(
        factory = { context ->
            SwitchMaterial(context).apply {
                text = context.getString(R.string.dark_theme)
                textSize = 16f
                setTextColor(textColor)
                typeface = ResourcesCompat.getFont(context, R.font.ys_display_regular)
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChange(isChecked)
                }
                minHeight = 0
                minimumHeight = 0
            }
        }, update = { switch ->
            switch.isChecked = checked
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp)
    )
}

