package com.codem.squaro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.codem.squaro.R
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.databinding.FragmentRepoDetailsBinding
import com.codem.squaro.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by Doumith.A on 8/3/21.
 */
@AndroidEntryPoint
class RepoDetailsFragment @Inject constructor() : Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentRepoDetailsBinding
    private val repositoryViewModel by activityViewModels<RepositoryViewModel>()
    private var repoModel: RepoModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailsBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.btnBookMark.setOnClickListener(this)
        repositoryViewModel.getSelectedRepo().observe(viewLifecycleOwner, { mRepoModel ->
            repoModel = mRepoModel
            _binding.name.text = mRepoModel.name
            _binding.count.text = mRepoModel.starsCount.toString()
            updateBookmarkButton(mRepoModel)

        })
    }

    private fun updateBookmarkButton(repoModel: RepoModel) {
        if (repoModel.isBookMarked) {
            _binding.btnBookMark.text = getString(R.string.remove_Bookmark_repo)
        } else {
            _binding.btnBookMark.text = getString(R.string.bookmark_repo)

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBookMark -> {
                repoModel?.also { mRepoModel ->
                    mRepoModel.isBookMarked = !mRepoModel.isBookMarked
                    repositoryViewModel.updateRepo(mRepoModel).observe(viewLifecycleOwner, {
                        updateBookmarkButton(it)
                    })
                }
            }
        }
    }

}