package com.liquotrack.stocksip.features.authentication.adminpanel.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.AccountUsers
import com.liquotrack.stocksip.features.authentication.adminpanel.domain.domain.SubUser

@Composable
fun UsersList(
    users: List<SubUser>,
    isLoading: Boolean,
    onEditUser: (SubUser) -> Unit,
    onDeleteUser: (SubUser) -> Unit
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (users.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(id = com.liquotrack.stocksip.R.string.no_users_found))
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users) { user ->
                UserCard(
                    user = user,
                    onEdit = { onEditUser(user) },
                    onDelete = { onDeleteUser(user) }
                )
            }
        }
    }
}