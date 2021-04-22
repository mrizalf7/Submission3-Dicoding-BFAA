package com.example.fourthproject.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.fourthproject.R
import com.example.fourthproject.databinding.ActivityDetailBinding
import com.example.fourthproject.entity.GithubUserData
import com.example.fourthproject.adapter.PagerAdapter
import com.example.fourthproject.api.DetailViewModel
import com.example.fourthproject.db.FavoriteUserContract
import com.example.fourthproject.db.FavoriteUserContract.Columns.Companion.CONTENT_URI
import com.example.fourthproject.helper.MappingHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var statusFavorite = false
    private lateinit var uriWithId: Uri
    private lateinit var user:GithubUserData



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataInitial()
        checkFavoriteUser(user)
        crdFavoriteUser(user)
        fragmentSetter(user)
        apiSetterGetter(user)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = user.idGithub
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }

    private fun dataInitial (){
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )
        user = intent.getParcelableExtra<GithubUserData>(EXTRA_USER) as GithubUserData


    }
    private fun fragmentSetter(user:GithubUserData){
        val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = user.idGithub
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                TAB_TITLES[position]
            )
        }.attach()
    }


    private fun apiSetterGetter(user:GithubUserData){

        user.idGithub?.let { detailViewModel.setDetailUser(it) }
        detailViewModel.getDetailUser().observe(this, {
            binding.apply {
                it.name.let { it ->
                    if (it != null) {
                        detailViewModel.setDetailUser(it)
                    }
                }
                Glide.with(this@DetailActivity)
                    .load(it.avatar)
                    .into(imgAvatarDetail)
                repositoryDetail.text = StringBuilder("Repository:").append(it.repository)
                followersDetail.text = StringBuilder("Followers:").append(it.followers)
                followingDetail.text = StringBuilder("Following:").append(it.following)
                nameDetail.text = it.name
                locationDetail.text = StringBuilder("Location:").append(it.location)
                companyDetail.text = StringBuilder("Company:").append(it.company)

            }
            showLoading(false)
        })
    }

    private fun checkFavoriteUser(user: GithubUserData) {

        setStatusFavorite(statusFavorite)
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)

        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        val favorite = MappingHelper.mapCursorToArrayList(cursor)
        for (data in favorite) {
            if (user.idGithub == data.idGithub) {
                    statusFavorite = true
                    setStatusFavorite(statusFavorite)
                }
        }
    }

    private fun crdFavoriteUser(user: GithubUserData) {
        binding.fabButton.setOnClickListener {
            if (!statusFavorite) {

                val values = ContentValues()
                values.put(FavoriteUserContract.Columns._username, user.idGithub)
                values.put(FavoriteUserContract.Columns.avatar, user.avatar)
                values.put(FavoriteUserContract.Columns._id,user.id)
                contentResolver.insert(CONTENT_URI, values)

                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
                Toast.makeText(
                    this,
                    "Berhasil ditambahkan ke favorite",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {


                contentResolver.delete(uriWithId, null, null)


                Toast.makeText(
                    this,
                    "Berhasil dihapus dari favorite",
                    Toast.LENGTH_SHORT
                ).show()
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
            }

        }

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabButton.setImageResource(R.drawable.ic_favorited)
        } else {
            binding.fabButton.setImageResource(R.drawable.ic_favorite)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

