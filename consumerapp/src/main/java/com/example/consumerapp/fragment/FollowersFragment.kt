package com.example.consumerapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.activity.DetailActivity
import com.example.consumerapp.adapter.GithubRVAdapter
import com.example.consumerapp.api.FollowersViewModel
import com.example.consumerapp.databinding.FragmentFollowersBinding
import com.example.consumerapp.entity.GithubUserData


class FollowersFragment : Fragment() {


    private var _binding: FragmentFollowersBinding? = null
    private lateinit var adapter: GithubRVAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private val binding get() = _binding!!

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFollowers.setHasFixedSize(true)
        showRecyclerView()
        setFollowers()
    }

    private fun showRecyclerView() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        adapter = GithubRVAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollowers.adapter = adapter

        adapter.setOnItemClickCallback(object : GithubRVAdapter.OnItemClickCallback {
            override fun onItemClicked(githubUserData: GithubUserData) {
                showSelectedUser(githubUserData)
            }
        })
    }

    private fun showSelectedUser(userGithubUserData: GithubUserData) {
        val moveIntent = Intent(activity, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_USER, userGithubUserData)
        startActivity(moveIntent)
    }

    private fun setFollowers() {
        if (arguments != null) {
            val username = arguments?.getString(ARG_USERNAME)
            followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)

            if (username != null)
                followersViewModel.setFollowers(username)
        }

        followersViewModel.getFollowers().observe(viewLifecycleOwner, { listFollowers ->
            if (listFollowers != null) {
                adapter.setData(listFollowers)
            }
        })
    }


}