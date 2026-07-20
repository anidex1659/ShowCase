package com.example.showcase.ui.popup.Dialogs.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.showcase.features.MetaData.data.database.dao.ShowcaseDao
import com.example.showcase.features.MetaData.data.remote.TmdbRemoteDataSource
import com.example.showcase.ui.popup.Dialogs.Repository.ArtworkRepository
import com.example.showcase.ui.popup.Dialogs.Repository.IdentifySeriesRepository

class IdentifySeriesViewModelFactory(

    private val remote: TmdbRemoteDataSource,

    private val repository: IdentifySeriesRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return IdentifySeriesViewModel(

            remote = remote,

            repository = repository

        ) as T
    }
}



class EditArtworkViewModelFactory(

    private val dao: ShowcaseDao,

    private val repository: ArtworkRepository

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return EditArtworkViewModel(

            dao = dao,

            repository = repository

        ) as T
    }
}