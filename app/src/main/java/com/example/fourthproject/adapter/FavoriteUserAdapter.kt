package com.example.fourthproject.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fourthproject.CustomOnItemClickListener
import com.example.fourthproject.activity.DetailActivity
import com.example.fourthproject.databinding.ItemGithubFavoriteUserBinding

import com.example.fourthproject.entity.FavoriteUser
import com.example.fourthproject.entity.GithubUserData


class FavoriteUserAdapter(private val activity: Activity):RecyclerView.Adapter<FavoriteUserAdapter.ListViewHolder>() {

    private val listUserFavorite= ArrayList<GithubUserData>()
    private var onItemClickCallback: OnItemClickCallback? = null

    inner class ListViewHolder(private val binding: ItemGithubFavoriteUserBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(userFavorite: GithubUserData) {
            with(binding){
                Glide.with(itemView.context)
                    .load(userFavorite.avatar)
                    .into(imgAvatarFavorite)
                idGithubUserFavorite.text = userFavorite.idGithub

//                itemView.setOnClickListener {
//                    CustomOnItemClickListener(
//                        adapterPosition,
//                        object : CustomOnItemClickListener.OnItemClickCallback {
//                    override fun onItemClicked(view: View, position: Int) {
//                        val intent = Intent(activity, DetailActivity::class.java)
//                        intent.putExtra(DetailActivity.EXTRA_USER,userFavorite)
////                        intent.putExtra(DetailActivity.EXTRA_POSITION,position)
//                        activity.startActivity(intent)
//
//                    }
//                })
//                }
            }
        }
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemGithubFavoriteUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUserFavorite[position])
    }

    override fun getItemCount(): Int {
        return listUserFavorite.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(userFavorite: GithubUserData)
    }

    fun setData(items: ArrayList<GithubUserData>) {
        listUserFavorite.clear()
        listUserFavorite.addAll(items)
        notifyDataSetChanged()
    }

    var userFavoriteList = ArrayList<GithubUserData>()
        set(userFavoriteList) {
            if (userFavoriteList.size > 0) {
                this.userFavoriteList.clear()
            }
            this.userFavoriteList.addAll(userFavoriteList)
            notifyDataSetChanged()
        }

//    fun addItem(userFavorite: FavoriteUser) {
//        this.userFavoriteList.add(userFavorite)
//        notifyItemInserted(this.userFavoriteList.size - 1)
//    }
//    fun updateItem(position: Int, userFavorite: FavoriteUser) {
//        this.userFavoriteList[position] = userFavorite
//        notifyItemChanged(position, userFavorite)
//    }
//    fun removeItem(position: Int) {
//        this.userFavoriteList.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, this.userFavoriteList.size)
//    }
}