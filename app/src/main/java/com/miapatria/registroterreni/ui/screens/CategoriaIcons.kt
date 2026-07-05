package com.miapatria.registroterreni.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Grass
import androidx.compose.ui.graphics.vector.ImageVector
import com.miapatria.registroterreni.data.model.Categorie

fun iconForCategoria(categoria: String): ImageVector = when (categoria) {
    Categorie.POTATURA -> Icons.Filled.ContentCut
    Categorie.SCOCCHIATURA -> Icons.Filled.Grass
    Categorie.ACQUEDOTTO -> Icons.Filled.WaterDrop
    Categorie.MOTORISTA -> Icons.Filled.Build
    Categorie.CONCIMI -> Icons.Filled.Spa
    Categorie.VELENI -> Icons.Filled.Science
    Categorie.TASSE -> Icons.Filled.Receipt
    Categorie.CARBURANTE -> Icons.Filled.LocalGasStation
    else -> Icons.Filled.Category
}
