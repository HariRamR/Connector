package com.hari.connector.ui

import android.app.Activity
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hari.connector.databinding.FragUserProfileBinding
import com.hari.connector.interfaces.ContactInterface

class UserProfileFrag : Fragment() {

    private var contactInterface: ContactInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragUserProfileBinding.inflate(layoutInflater)

        if (requireActivity().resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            view.userPhotoFragUserProfile.layoutParams = ConstraintLayout.LayoutParams(1, 1)
        }
        view.backCardFragUserProfile.setOnClickListener {
            goBack()
        }
        view.backImgAppBarFragUserProfile.setOnClickListener {
            goBack()
        }

        val bundle = arguments
        bundle.let {
            val userName = bundle!!.getString("USERNAME")
            val email = bundle.getString("EMAIL")
            val avatar = bundle.getString("AVATAR")

            view.apply {
                userNameFragUserProfile.text = userName
                detailNameTvFragUserProfile.text = userName
                emailFragUserProfile.text = email
                detailEmailTvFragUserProfile.text = email
                Glide.with(requireActivity()).load(avatar).circleCrop()
                    .into(userPhotoFragUserProfile)
            }
        }

        return view.root
    }

    private fun goBack() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onAttach(activity: Activity) {

        if (activity is Dashboard) {
            contactInterface = activity
        }
        super.onAttach(activity)
    }

    override fun onDestroyView() {
        contactInterface!!.makeFABVisible()
        super.onDestroyView()
    }
}