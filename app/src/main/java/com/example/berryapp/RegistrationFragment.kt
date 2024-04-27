package com.example.berryapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.berryapp.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            regButton.setOnClickListener{
                if(username.text.isNotBlank() && password.text.isNotBlank() && confrimPassword.text.isNotBlank()){
                    findNavController().navigate(R.id.action_registrationFragment2_to_loginFragment)
                }
                else {
                    Toast.makeText(requireContext(), "Некорректный ввод", Toast.LENGTH_SHORT).show()
                }
            }
            redirectlogin.setOnClickListener {
                findNavController().navigate(R.id.action_registrationFragment2_to_loginFragment)
            }
        }
    }

}