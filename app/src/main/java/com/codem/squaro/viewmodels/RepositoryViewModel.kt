package com.codem.squaro.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codem.squaro.api.Data
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.data.repositories.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Doumith.A on 8/3/21.
 */
@HiltViewModel
class RepositoryViewModel @Inject constructor(
    private val repoRepository: RepoRepository,
) : ViewModel() {

    private val selectedRepo = MutableLiveData<RepoModel>()

    fun selectItem(repoModel: RepoModel) {
        selectedRepo.value = repoModel
    }

    fun getSelectedRepo(): MutableLiveData<RepoModel> {
        return selectedRepo
    }

    private var reposList: Data<RepoModel>? = null

    fun repoList(connectivityAvailable: Boolean): Data<RepoModel>? {

        if (reposList == null) {
            reposList = repoRepository.observePagedRepos(connectivityAvailable, viewModelScope)
        }
        return reposList
    }


    fun updateRepo(repo: RepoModel): LiveData<RepoModel> {
        viewModelScope.launch {
            repoRepository.updateRepo(repo)
        }
        return repoRepository.getRepoUntilChanged(repo)
    }
}