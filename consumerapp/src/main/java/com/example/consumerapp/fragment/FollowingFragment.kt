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
import com.example.consumerapp.api.FollowingViewModel
import com.example.consumerapp.databinding.FragmentFollowingBinding
import com.example.consumerapp.entity.GithubUserData


class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private lateinit var adapter: GithubRVAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private val binding get() = _binding!!

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
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
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFollowing.setHasFixedSize(true)
        showRecyclerView()
        setFollowing()
    }

    private fun showRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        adapter = GithubRVAdapter()
        adapter.notifyDataSetChanged()
        binding.rvFollowing.adapter = adapter


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

    private fun setFollowing() {
        if (arguments != null) {
            val username = arguments?.getString(ARG_USERNAME)
            followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FollowingViewModel::class.java)

            if (username != null)
                followingViewModel.setFollowing(username)
        }

        followingViewModel.getFollowing().observe(viewLifecycleOwner, { listFollowing ->
            if (listFollowing != null) {
                adapter.setData(listFollowing)
            }
        })
    }

}