package com.example.esmnapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class IFirebaseUIAuth : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){ res: FirebaseAuthUIAuthenticationResult ->
        if( res.resultCode === RESULT_OK){
            if( res.idpResponse != null){
                seLogeo(res.idpResponse!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ifirebase_uiauth)
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        btnLogin.setOnClickListener {
            val prviders = arrayListOf(// arreglo de proveedores para el login
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            // construimos el intent de login
            val signIn = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(prviders).build()
            signInLauncher.launch(signIn)
        }
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        btnLogout.setOnClickListener { seDeslogeo() }

    }

    fun registrarUsuarioPorPrimeraVez(
        usuario: IdpResponse
    ){

    }


    fun seLogeo(
        res: IdpResponse
    ){
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)

        btnLogin.visibility = View.VISIBLE
        btnLogout.visibility = View.VISIBLE
        if ( res.isNewUser){// se puede comunicar con cualquier otro servicio de BD

        }
    }

    fun seDeslogeo(){
        val btnLogin = findViewById<Button>(R.id.btn_login_firebase)
        val btnLogout = findViewById<Button>(R.id.btn_logout_firebase)
        btnLogout.visibility = View.INVISIBLE
        btnLogin.visibility = View.VISIBLE
        FirebaseAuth.getInstance().signOut()
    }
}