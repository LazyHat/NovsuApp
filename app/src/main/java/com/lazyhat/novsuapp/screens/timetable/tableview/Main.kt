package com.lazyhat.novsuapp.screens.timetable.tableview

//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.TextStyle
//import com.lazyhat.novsuapp.viewmodels.CellInfo
//import com.lazyhat.novsuapp.viewmodels.TableUiState

//data class TableTime(
//    val start: String,
//    val end: String
//) {
//    override fun toString(): String {
//        return "$start-$end"
//    }
//}

//@Composable
//fun Table(tableUiState: TableUiState, dayIndex: Int) {
//    for (row in 0 until tableUiState.rows) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            tableUiState.days[dayIndex].forEach {
//                Row(modifier = Modifier.weight(1F / tableUiState.rows)) {
//                    CellTime(time = it.key)
//                    Cell(it.value)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Cell(info: CellInfo) {
//    Text(info.text, style = TextStyle(color = MaterialTheme.colorScheme.onBackground))
//}
//
//@Composable
//fun CellTime(time: TableTime) {
//    Text(time.toString(), style = TextStyle(color = MaterialTheme.colorScheme.onBackground))
//}

//@Preview(showBackground = false)
//@Composable
//fun TablePreview() {
//    Table(
//        TableDayUiState(10, 2)
//    )
//}