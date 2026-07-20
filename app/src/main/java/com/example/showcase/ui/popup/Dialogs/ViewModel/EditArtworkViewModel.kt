package com.example.showcase.ui.popup.Dialogs.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.ui.popup.Dialogs.Model.ArtworkItem
import com.example.showcase.ui.popup.Dialogs.Repository.ArtworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class EditArtworkViewModel(

    private val dao: ShowcaseDao,

    private val repository: ArtworkRepository

) : ViewModel() {

    private val _artworks =
        MutableStateFlow<List<ArtworkItem>>(
            emptyList()
        )

    val artworks =
        _artworks.asStateFlow()

    fun loadArtwork(
        context: Context,
        seriesId: Long
    ) {

        viewModelScope.launch {

            val series =
                dao.getSeriesById(
                    seriesId
                ) ?: return@launch

            _artworks.value =
                repository
                    .getArtworkFiles(
                        context,
                        series.folderPath
                    )
                    .map {

                        ArtworkItem(
                            title = it.first,
                            uri = it.second
                        )
                    }
        }
    }
}