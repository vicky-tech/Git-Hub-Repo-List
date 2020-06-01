package com.example.githubrepo.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepo.data.remote.model.RepoItem
import com.example.githubrepo.databinding.RepoItemViewBinding

class GithubRepoRecyclerAdapter :RecyclerView.Adapter<GithubRepoRecyclerAdapter.RepoViewHolder>() {

    var data : List<RepoItem> = mutableListOf()
    private lateinit var binding: RepoItemViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RepoItemViewBinding.inflate(inflater, parent, false)
        return RepoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(data : List<RepoItem>?){
        data?.let {
            val lastIndex = this.data.size
            this.data = data
            notifyDataSetChanged()
          //  notifyItemRangeInserted(lastIndex,this.data.size)
        }
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind((data)[position])
    }
    class RepoViewHolder(private var binding: RepoItemViewBinding ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(repoItem: RepoItem) {
            binding.item = repoItem
            binding.executePendingBindings()
        }
    }
}