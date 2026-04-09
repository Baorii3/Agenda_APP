package com.example.agenda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.amplifyframework.core.Amplify
import com.bumptech.glide.Glide
import com.example.agenda.viewmodel.UserViewModel
class PerfilFragment : Fragment() {

    private val viewModelUsuari: UserViewModel by activityViewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val image = view.findViewById<ImageView>(R.id.imgAvatar)

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            Amplify.Auth.signOut { signOutResult ->
                viewModelUsuari.setUser(null)
            }
        }


        Log.d("PerfilFragment", "Usuario logueado: ${viewModelUsuari.user.value}")
        if (viewModelUsuari.user.value != null) {
            tvUserName.text = viewModelUsuari.user.value?.nom
            tvUserEmail.text = viewModelUsuari.user.value?.email
        }

        if (viewModelUsuari.user.value == null) {
            tvUserName.text = ""
            tvUserEmail.text = ""
            image.setImageResource(R.drawable.ic_launcher_background)
            btnLogout.isVisible = false
        }

        viewModelUsuari.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                tvUserName.text = user.nom
                tvUserEmail.text = user.email
                Glide.with(this)
                    .load(user.fotoPerfil)
                    .error(R.drawable.ic_launcher_background)
                    .into(image)
            } else {
                tvUserName.text = ""
                tvUserEmail.text = ""
                image.setImageResource(R.drawable.ic_launcher_background)
                btnLogout.isVisible = false
            }
        }

        return view
    }
}