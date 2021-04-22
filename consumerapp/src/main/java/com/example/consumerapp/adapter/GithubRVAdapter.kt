package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.entity.GithubUserData
import com.example.consumerapp.databinding.ItemGithubUserBinding

class GithubRVAdapter:RecyclerView.Adapter<GithubRVAdapter.ListViewHolder>(){

    private val listUser = ArrayList<GithubUserData>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemGithubUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    fun setData(items: ArrayList<GithubUserData>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: ItemGithubUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userGithubUserData: GithubUserData) {
            with(binding){
                Glide.with(itemView.context)
                    .load(userGithubUserData.avatar)
                    .into(imgAvatar)
                idGithubUser.text = userGithubUserData.idGithub
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userGithubUserData) }
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(githubUserData: GithubUserData)
    }
}