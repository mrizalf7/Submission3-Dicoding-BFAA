package com.example.fourthproject.activity

import android.content.ContentValues
import android.content.Intent
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
import com.example.fourthproject.db.FavoriteUserHelper
import com.example.fourthproject.entity.FavoriteUser
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
        const val EXTRA_FAVORITE_USER = "extra_favorite_user"
//        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteUserHelper:FavoriteUserHelper
    private var statusFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()


        val user = intent.getParcelableExtra<GithubUserData>(EXTRA_FAVORITE_USER) as GithubUserData
//        val favoriteUser = intent.getParcelableExtra<FavoriteUser>(EXTRA_FAVORITE_USER) as FavoriteUser


     val pagerAdapter = PagerAdapter(this)
        pagerAdapter.username = user.idGithub
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                TAB_TITLES[position])
        }.attach()

//        val pagerAdapter2 = PagerAdapter(this)
//        pagerAdapter2.username = favoriteUser.username
//        val viewPager2: ViewPager2 = binding.viewPager
//        viewPager2.adapter = pagerAdapter2
//        val tabs2: TabLayout = binding.tabs
//        TabLayoutMediator(tabs2, viewPager2) { tab, position ->
//            tab.text = resources.getString(
//                TAB_TITLES[position])
//        }.attach()



        supportActionBar?.elevation = 0f
        supportActionBar?.title = user.idGithub
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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


//        favoriteUser.username?.let { detailViewModel.setDetailUser(it) }
//        detailViewModel.getDetailFavoriteUser().observe(this, {
//            binding.apply {
//                it.username.let { it ->
//                    if (it != null) {
//                        detailViewModel.setDetailUser(it)
//                    }
//                }
//                Glide.with(this@DetailActivity)
//                    .load(it.avatar)
//                    .into(imgAvatarDetail)
//////                repositoryDetail.text = StringBuilder("Repository:").append(it.repository)
//////                followersDetail.text = StringBuilder("Followers:").append(it.followers)
//////                followingDetail.text = StringBuilder("Following:").append(it.following)
//////                nameDetail.text = it.name
//////                locationDetail.text = StringBuilder("Location:").append(it.location)
//////                companyDetail.text = StringBuilder("Company:").append(it.company)
////
//            }
//            showLoading(false)
//        })



//        setDetailFavoriteUser(favoriteUser)
        checkUserFavorite(user)
    }

//        private fun setDetailFavoriteUser(user:FavoriteUser){
//
//            binding.nameDetail.text = user.username
//            Glide.with(this@DetailActivity)
//                .load(user.avatar).into(binding.imgAvatarDetail)
//        }

            private fun checkUserFavorite(user: GithubUserData){


                val cursor = favoriteUserHelper.queryByUsername(user.idGithub.toString())
                val favorite = MappingHelper.mapCursorToArrayList(cursor)

                for (data in favorite){
                    if (user.idGithub == data.idGithub){
                        statusFavorite = true
                    }
                }

                setStatusFavorite(statusFavorite)

                binding.fabButton.setOnClickListener {
                    if (!statusFavorite) {

                        val values = ContentValues()
                        values.put(FavoriteUserContract.Columns._username, user.idGithub)
                        values.put(FavoriteUserContract.Columns.avatar, user.avatar)

                       val result= favoriteUserHelper.insert(values)
                        if(result>0){

                            statusFavorite = !statusFavorite
                            setStatusFavorite(statusFavorite)
                            Toast.makeText(this, "Berhasil ditambahkan ke favorite", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else{
                            Toast.makeText(this, "Gagal menambahkan ke favorite", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } else {
                       val result = favoriteUserHelper.deleteByUsername(user.idGithub.toString())
                        if(result>0){
                        Toast.makeText(this, "Berhasil dihapus dari favorite", Toast.LENGTH_SHORT).show()
                            statusFavorite = !statusFavorite
                            setStatusFavorite(statusFavorite)
                        }
                        else{
                            Toast.makeText(this,"Gagal menghapus user dari favorite",Toast.LENGTH_SHORT).show()
                        }
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
                if (statusFavorite){
                    binding.fabButton.setImageResource(R.drawable.ic_favorited)
                }else {
                    binding.fabButton.setImageResource(R.drawable.ic_favorite)
                }

            }

            override fun onCreateOptionsMenu(menu: Menu?): Boolean {
                val inflater = menuInflater
                inflater.inflate(R.menu.menu, menu)
                return true

            }

            override fun onOptionsItemSelected(item: MenuItem): Boolean {
                // Handle item selection
                return when (item.itemId) {
                    R.id.favorite_button -> {

                        val moveIntent = Intent(this, FavoriteUserActivity::class.java)
                        startActivity(moveIntent)
                        true
                    }

                    else -> super.onOptionsItemSelected(item)
                }
            }

            override fun onSupportNavigateUp(): Boolean {
                onBackPressed()
                return true
            }

        }