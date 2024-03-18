package com.learnandroid.foodie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.learnandroid.foodie.databinding.ActivityLoginBinding
import com.learnandroid.foodie.model.UserModel

class LoginActivity : AppCompatActivity() {

    private var userName: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // initialize of Firebase auth
        auth = Firebase.auth
        // initialize firebase database
        database = Firebase.database.reference
        // initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        //initialize facebook sign in
        callbackManager = CallbackManager.Factory.create()

        // login with email and password
        binding.btnLogin.setOnClickListener {

            //get text from edittext
            email = binding.etEmailLogin.text.toString().trim()
            password = binding.etPasswordLogin.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please Fill All Details 💁🏻‍♂️", Toast.LENGTH_SHORT).show()
            } else {
                createUser()
                Toast.makeText(this, "Login Successful 👍", Toast.LENGTH_SHORT).show()
            }
        }

        // google sign in
        binding.btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        binding.btnDontHave.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnFacebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        TODO("Not yet implemented")
                    }

                    override fun onError(error: FacebookException) {
                        Log.e("TAG", "onError: $error")
                    }

                    override fun onSuccess(result: LoginResult) {
                        handleFacebookAccessToken(result.accessToken)
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {


        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUi(user)
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign In Successful😄", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, ChooseLocationActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Sign In Failed 🥲", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Sign In Failed 🥲", Toast.LENGTH_SHORT).show()
            }
        }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserData()
                        val user = auth.currentUser
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Sign In Failed 🥲 ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        //get text from edittext
        email = binding.etEmailLogin.text.toString().trim()
        password = binding.etPasswordLogin.text.toString().trim()

        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //save data into database
        database.child("user").child(userId).setValue(user)
    }

    override fun onStart() {
        super.onStart()
        val currentUSer = auth.currentUser
        if (currentUSer != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}