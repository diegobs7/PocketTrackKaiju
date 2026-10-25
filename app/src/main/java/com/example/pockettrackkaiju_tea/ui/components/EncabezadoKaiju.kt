package com.example.pockettrackkaiju_tea.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pockettrackkaiju_tea.R
import com.example.pockettrackkaiju_tea.ui.theme.PocketTrackKaiju_TEATheme

private val FuenteEncabezado = FontFamily(
    Font(R.font.unbounded_semibold, FontWeight.SemiBold),
    Font(R.font.unbounded_extrabold, FontWeight.ExtraBold)
)

@Composable
fun EncabezadoKaiju(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = "PocketTrack Kaiju"
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_encabezado),
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
        Column {
            Text(
                text = "POCKETTRACK",
                fontFamily = FuenteEncabezado,
                fontWeight = FontWeight.SemiBold,
                fontSize = 9.sp,
                lineHeight = 11.sp,
                letterSpacing = 2.sp,
                maxLines = 1,
                color = LocalContentColor.current.copy(alpha = 0.85f)
            )
            Text(
                text = "KAIJU",
                fontFamily = FuenteEncabezado,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                lineHeight = 22.sp,
                letterSpacing = 1.sp,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEncabezadoKaiju() {
    PocketTrackKaiju_TEATheme {
        Surface {
            EncabezadoKaiju(modifier = Modifier.padding(16.dp))
        }
    }
}

