package com.liquotrack.stocksip.features.careguides.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.liquotrack.stocksip.features.careguides.domain.CareGuide

@Composable
fun CareGuideCard(careGuide: CareGuide, onClick: () -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(4.dp)) {
        AsyncImage(
            model = careGuide.title,
            contentDescription = careGuide.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = careGuide.title,
            modifier = Modifier.padding(8.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = careGuide.summary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }
}