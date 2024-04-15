package dev.modena.heroes.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailHeroViewModel @Inject constructor(): ViewModel() {

    private val _share = MutableLiveData<Share>()
    val share: LiveData<Share> = _share

    fun onClickShareHero() {
        _share.value = Share.Image
    }

}

sealed class Share {
    data object Image : Share()

}