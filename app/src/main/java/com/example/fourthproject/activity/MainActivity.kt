package com.example.fourthproject.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fourthproject.R
import com.example.fourthproject.adapter.GithubRVAdapter
import com.example.fourthproject.api.MainViewModel
import com.example.fourthproject.databinding.ActivityMainBinding
import com.example.fourthproject.entity.GithubUserData



class MainActivity : AppCompatActivity() {


    private lateinit var adapter: GithubRVAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        val actionbar = supportActionBar
        actionbar?.title = "Home"
        binding.rvUsers.setHasFixedSize(true)
        getDataUserApiInitial()
        showRecyclerList()

        mainViewModel.getUser().observe(this, { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })

    }
    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = GithubRVAdapter()
        adapter.notifyDataSetChanged()
        binding.rvUsers.adapter = adapter
        showLoading(false)

        adapter.setOnItemClickCallback(object : GithubRVAdapter.OnItemClickCallback {
            override fun onItemClicked(githubUserData: GithubUserData) {
                showSelectedUser(githubUserData)
            }
        })
    }

    private fun showSelectedUser(userGithubUserData: GithubUserData) {
        val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_USER, userGithubUserData)
        startActivity(moveIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                getDataUserFromApi(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.favorite_button -> {

                val moveIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(moveIntent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getDataUserFromApi(username: String) {
        if(username.isEmpty()) return
        showLoading(true)
         mainViewModel.setGithubUser(username)

    }

    fun getDataUserApiInitial(){
        showLoading(true)
        mainViewModel.setGithubUserInitial("A")
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}