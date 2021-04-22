package com.example.fourthproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fourthproject.adapter.GithubRVAdapter
import com.example.fourthproject.databinding.ActivityFavoriteUserBinding
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.CONTENT_URI
import com.example.fourthproject.entity.GithubUserData
import com.example.fourthproject.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteUserActivity : AppCompatActivity() {


    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: GithubRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRecyclerList()
        loadFavoriteUserAsync()
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Favorite Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
        private fun loadFavoriteUserAsync() {
            GlobalScope.launch(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE

                val deferredNotes = async(Dispatchers.IO) {

                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val favoriteUsers = deferredNotes.await()
                binding.progressBar.visibility = View.INVISIBLE

                if (favoriteUsers.size > 0) {
                    adapter.setData(favoriteUsers)

                } else {
                    showSnackbarMessage("Tidak ada data saat ini")
                }
            }

        }

        private fun showSelectedUser(favoriteUser: GithubUserData) {
            val moveIntent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
            moveIntent.putExtra(DetailActivity.EXTRA_USER, favoriteUser)
            startActivity(moveIntent)
        }

        private fun showRecyclerList() {
            binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
            adapter = GithubRVAdapter()
            binding.rvFavoriteUsers.setHasFixedSize(true)
            binding.rvFavoriteUsers.adapter = adapter
            showLoading(false)

            adapter.setOnItemClickCallback(object : GithubRVAdapter.OnItemClickCallback {
                override fun onItemClicked(githubUserData: GithubUserData) {
                    showSelectedUser(githubUserData)
                }
            })
        }

        private fun showSnackbarMessage(message: String) {
            Snackbar.make(binding.rvFavoriteUsers, message, Snackbar.LENGTH_SHORT).show()
        }

        private fun showLoading(state: Boolean) {
            if (state) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
        }

        override fun onResume() {
            super.onResume()
            loadFavoriteUserAsync()
        }
    }