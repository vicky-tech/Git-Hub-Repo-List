package com.example.githubrepo.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepo.R
import com.example.githubrepo.data.repository.Status
import com.example.githubrepo.databinding.IssueFragmentBinding
import com.example.githubrepo.util.gone
import com.example.githubrepo.util.visible
import com.google.android.material.snackbar.Snackbar

class GithubRepoFragment : Fragment() {

    private lateinit var viewModel: GithubRepoViewModel
    private lateinit var binding : IssueFragmentBinding
    private lateinit var adapter : GithubRepoRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.issue_fragment,container,false)
        adapter = GithubRepoRecyclerAdapter()
        binding.repoList.adapter = adapter
        val decoration = DividerItemDecoration(
            activity,
            RecyclerView.VERTICAL
        )
        binding.repoList.addItemDecoration(decoration)
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(GithubRepoViewModel::class.java)
        viewModel.repoList.observe(viewLifecycleOwner, Observer {
           if(!it.isNullOrEmpty()) {
              // binding.noResultsFound.gone()
               adapter.updateData(it)

           } /*else {
               binding.noResultsFound.visible()
           }*/
        })
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when(it){
                Status.LOADING -> {binding.loadMoreBar.visible()}
                Status.ERROR -> {
                    binding.loadMoreBar.gone()
                    Snackbar.make(view,getString(R.string.network_error),Snackbar.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    binding.loadMoreBar.gone()
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding.repoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    viewModel.loadMore()
                }
            }
        })
    }
}