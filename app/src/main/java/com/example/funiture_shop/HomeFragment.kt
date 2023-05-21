package com.example.funiture_shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.funiture_shop.databinding.FragmentHomeBinding
import com.example.funiture_shop.helper.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        binding.buttonFirst.setOnClickListener {
            sharedPreferencesHelper.logout()
            requireActivity().finish()
        }
    }

}