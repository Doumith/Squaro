package com.codem.squaro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation

import androidx.recyclerview.widget.LinearLayoutManager
import com.codem.squaro.R
import com.codem.squaro.adapters.OnItemCLickListener
import com.codem.squaro.adapters.RepositoryListAdapter
import com.codem.squaro.api.Status
import com.codem.squaro.data.entities.RepoModel
import com.codem.squaro.databinding.FragmentReposListBinding
import com.codem.squaro.utils.ConnectivityUtil
import com.codem.squaro.viewmodels.RepositoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by Doumith.A on 8/3/21.
 */
@AndroidEntryPoint
class RepoListFragment @Inject constructor() : Fragment() {

    private lateinit var _binding: FragmentReposListBinding
    private val repositoryViewModel by activityViewModels<RepositoryViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReposListBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val repoAdapter = RepositoryListAdapter()
        _binding.repositoryList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = repoAdapter

        }

        repoAdapter.onItemCLickListener = object : OnItemCLickListener {
            override fun onItemClicked(repoModel: RepoModel?) {
                repoModel?.let { mRepoModel ->
                    repositoryViewModel.selectItem(mRepoModel)
                }
                view?.let { mView ->
                    Navigation.findNavController(mView).navigate(R.id.action_list_to_details)
                }
            }
        }

        subscribeUI(repoAdapter)

    }


    private fun subscribeUI(adapter: RepositoryListAdapter) {
        val data = repositoryViewModel.repoList(ConnectivityUtil.isNetworkAvailable(context))
        data?.networkState?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.RUNNING -> {
                    if (adapter.itemCount == 0)
                        _binding.progressBar.visibility = View.VISIBLE
                }
                Status.FAILED -> {
                    _binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    _binding.progressBar.visibility = View.GONE
                }
            }
        })
        data?.pagedList?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

}