package com.example.funiture_shop.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.funiture_shop.R
import com.example.funiture_shop.common.afterTextChanged
import com.example.funiture_shop.common.showToast
import com.example.funiture_shop.data.model.entity.User
import com.example.funiture_shop.data.model.res.Res
import com.example.funiture_shop.databinding.FragmentSignUpBinding
import com.example.funiture_shop.ui.authentication.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {


    private val viewModel: SignUpViewModel by viewModels()
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container, false
        )
        val username = binding.username
        val password = binding.password
        val confirmPassword = binding.confirmPassword
        val signUp = binding.signUp
        val loading = binding.loading
        val name = binding.name
        val address = binding.address
        val phoneNumber = binding.phoneNumber

        viewModel.apply {
            loginFormState.observe(viewLifecycleOwner) {
                val loginState = it

                // disable login button unless both username / password is valid
                signUp.isEnabled = loginState.isDataValid

                if (loginState.usernameError != null) {
                    username.error = getString(loginState.usernameError)
                }
                if (loginState.passwordError != null) {
                    password.error = getString(loginState.passwordError)
                }
            }

            signInRes.observe(viewLifecycleOwner) {
                when (it) {
                    is Res.Success<*> -> {
                        createUser(
                            User(
                                email = username.text.toString(),
                                name = name.text.toString(),
                                phoneNumber = phoneNumber.text.toString(),
                                address = address.text.toString(),
                                permission = 1,
                                imageUrl = ""
                            )
                        )
                    }

                    is Res.Error -> {
                        requireContext().showToast(
                            it.message
                        )
                    }
                }
            }

            createUserRes.observe(viewLifecycleOwner) {
                loading.visibility = View.GONE
                when (it) {
                    is Res.Success<*> -> {
                        requireContext().showToast("Create Account success!")
                        findNavController().popBackStack()
                    }

                    is Res.Error -> {
                        requireContext().showToast(
                            it.message
                        )
                    }
                }
            }
        }

        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.signUp(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }


        }

        signUp.setOnClickListener {
            if (phoneNumber.text.toString().isNotBlank() &&
                address.text.toString().isNotBlank() &&
                name.text.toString().isNotBlank() &&
                username.text.toString().isNotBlank() &&
                password.text.toString().isNotBlank()
            ) {
                if (confirmPassword.text.toString() == password.text.toString()) {
                    loading.visibility = View.VISIBLE
                    viewModel.signUp(username.text.toString(), password.text.toString())
                } else {
                    requireContext().showToast("Not valid confirm password!")
                }
            } else {
                requireContext().showToast("Not enough information!")
            }
        }
        return binding.root
    }


}